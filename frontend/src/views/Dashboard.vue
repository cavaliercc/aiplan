<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { use } from 'echarts/core'
import { CanvasRenderer } from 'echarts/renderers'
import { BarChart } from 'echarts/charts'
import { GridComponent, TooltipComponent, LegendComponent } from 'echarts/components'
import VChart from 'vue-echarts'
import { getKpi, getTrend, getAlerts } from '../api/dashboard'
import type { DashboardKpiDTO, TrendDataDTO, AlertDTO } from '../api/dashboard'
import { kpiData as mockKpi, weeklyTrend as mockTrend, todayAlerts as mockAlerts } from '../mock/dashboard'

use([CanvasRenderer, BarChart, GridComponent, TooltipComponent, LegendComponent])

const kpiCards = ref(mockKpi)
const trendDays = ref(mockTrend.days)
const trendScheduled = ref(mockTrend.scheduled)
const trendActual = ref(mockTrend.actual)
const alerts = ref(mockAlerts)

function buildKpiCards(dto: DashboardKpiDTO) {
  return [
    {
      title: '本周总排班',
      value: dto.totalScheduledHours.toLocaleString(),
      unit: '小时',
      change: 0,
      icon: 'Clock',
      color: '#409eff',
    },
    {
      title: '预估人力成本',
      value: `¥${dto.estimatedLaborCost.toLocaleString()}`,
      unit: '',
      change: 0,
      icon: 'Money',
      color: '#67c23a',
    },
    {
      title: '平均P/H效率',
      value: `¥${dto.avgEfficiency}`,
      unit: '/h',
      change: 0,
      icon: 'TrendCharts',
      color: '#e6a23c',
    },
    {
      title: '今日出勤率',
      value: String(dto.attendanceRate),
      unit: '%',
      change: 0,
      icon: 'UserFilled',
      color: '#f56c6c',
      isProgress: true,
      progressValue: dto.attendanceRate,
    },
  ]
}

onMounted(async () => {
  try {
    const [kpi, trend, alertList] = await Promise.all([getKpi(), getTrend(), getAlerts()])
    kpiCards.value = buildKpiCards(kpi)
    trendDays.value = trend.map((t: TrendDataDTO) => t.day)
    trendScheduled.value = trend.map((t: TrendDataDTO) => t.scheduled)
    trendActual.value = trend.map((t: TrendDataDTO) => t.actual)
    alerts.value = alertList as unknown as typeof mockAlerts
  } catch {
    // backend not available — mock data already loaded
  }
})

const chartOption = computed(() => ({
  tooltip: { trigger: 'axis' },
  legend: { data: ['计划排班', '实际出勤'], top: 10 },
  grid: { left: 40, right: 20, top: 50, bottom: 30 },
  xAxis: { type: 'category', data: trendDays.value },
  yAxis: { type: 'value', name: '小时' },
  series: [
    {
      name: '计划排班',
      type: 'bar',
      data: trendScheduled.value,
      itemStyle: { color: '#409eff', borderRadius: [4, 4, 0, 0] },
      barWidth: 20,
    },
    {
      name: '实际出勤',
      type: 'bar',
      data: trendActual.value,
      itemStyle: { color: '#67c23a', borderRadius: [4, 4, 0, 0] },
      barWidth: 20,
    },
  ],
}))
</script>

<template>
  <div>
    <!-- Page Header -->
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-800 m-0">仪表板概览</h1>
      <p class="text-gray-400 mt-1 text-sm">第17周 | 2026年4月20日 - 4月26日</p>
    </div>

    <!-- KPI Cards -->
    <div class="grid grid-cols-4 gap-4 mb-6">
      <el-card v-for="(kpi, idx) in kpiCards" :key="idx" shadow="hover" class="kpi-card">
        <div class="flex items-start justify-between">
          <div>
            <div class="text-xs text-gray-400 mb-2">{{ kpi.title }}</div>
            <div class="text-2xl font-bold text-gray-800">
              {{ kpi.value }}<span class="text-sm font-normal text-gray-500">{{ kpi.unit }}</span>
            </div>
            <div v-if="!kpi.isProgress" class="mt-2">
              <el-tag
                :type="kpi.change > 0 ? 'success' : kpi.change < 0 ? 'danger' : 'info'"
                size="small"
                effect="plain"
              >
                <el-icon v-if="kpi.change > 0"><Top /></el-icon>
                <el-icon v-else-if="kpi.change < 0"><Bottom /></el-icon>
                {{ Math.abs(kpi.change) }}%
              </el-tag>
              <span class="text-xs text-gray-400 ml-1">vs 上周</span>
            </div>
            <div v-else class="mt-2">
              <el-progress
                :percentage="(kpi as any).progressValue"
                :stroke-width="8"
                :show-text="false"
                color="#409eff"
              />
            </div>
          </div>
          <div
            class="w-10 h-10 rounded-lg flex items-center justify-center"
            :style="{ backgroundColor: kpi.color + '15' }"
          >
            <el-icon :size="20" :style="{ color: kpi.color }">
              <component :is="kpi.icon" />
            </el-icon>
          </div>
        </div>
      </el-card>
    </div>

    <!-- Charts & Alerts -->
    <div class="grid grid-cols-3 gap-4">
      <!-- Trend Chart -->
      <el-card shadow="hover" class="col-span-2">
        <template #header>
          <div class="flex items-center justify-between">
            <span class="font-semibold text-gray-700">排班趋势</span>
            <el-tag size="small" effect="plain">本周</el-tag>
          </div>
        </template>
        <v-chart :option="chartOption" style="height: 320px" autoresize />
      </el-card>

      <!-- Alerts -->
      <el-card shadow="hover">
        <template #header>
          <div class="flex items-center justify-between">
            <span class="font-semibold text-gray-700">今日提醒</span>
            <el-badge :value="alerts.length" type="danger" />
          </div>
        </template>
        <div class="space-y-3">
          <div
            v-for="(alert, idx) in alerts"
            :key="idx"
            class="p-3 rounded-lg border border-gray-100 hover:bg-gray-50 cursor-pointer transition-colors"
          >
            <div class="flex items-start gap-2">
              <el-icon
                :size="16"
                class="mt-0.5"
                :style="{
                  color:
                    alert.type === 'danger'
                      ? '#f56c6c'
                      : alert.type === 'warning'
                        ? '#e6a23c'
                        : alert.type === 'success'
                          ? '#67c23a'
                          : '#909399',
                }"
              >
                <component :is="alert.icon" />
              </el-icon>
              <div class="flex-1 min-w-0">
                <div class="text-sm font-medium text-gray-700">{{ alert.title }}</div>
                <div class="text-xs text-gray-400 mt-1 truncate">{{ alert.desc }}</div>
                <div class="text-xs text-gray-300 mt-1">{{ alert.time }}</div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.kpi-card :deep(.el-card__body) {
  padding: 20px;
}
</style>
