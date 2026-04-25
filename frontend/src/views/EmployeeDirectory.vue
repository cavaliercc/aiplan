<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getEmployees, getPreferences, updatePreferences } from '../api/employees'
import type { EmployeeDTO, PreferenceDTO } from '../api/employees'
import { employees as mockEmployees, departments, statuses } from '../mock/employees'
import PreferenceModal from '../components/PreferenceModal.vue'

const loading = ref(false)
const employees = ref<EmployeeDTO[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const searchName = ref('')
const filterDept = ref('')
const filterStatus = ref('')

const usingApi = ref(false)
const prefVisible = ref(false)
const selectedEmployee = ref<EmployeeDTO | null>(null)

async function loadEmployees() {
  loading.value = true
  try {
    const res = await getEmployees({
      page: currentPage.value,
      size: pageSize.value,
      name: searchName.value || undefined,
    })
    employees.value = res.records
    total.value = Number(res.total)
    usingApi.value = true
  } catch {
    if (!usingApi.value) {
      const filtered = mockEmployees.filter((e) => {
        if (filterDept.value && e.department !== filterDept.value) return false
        if (filterStatus.value && e.status !== filterStatus.value) return false
        if (searchName.value && !e.name.includes(searchName.value)) return false
        return true
      })
      total.value = filtered.length
      const start = (currentPage.value - 1) * pageSize.value
      employees.value = filtered.slice(start, start + pageSize.value) as unknown as EmployeeDTO[]
    }
  } finally {
    loading.value = false
  }
}

function statusTag(status: string) {
  const map: Record<string, string> = { '在职': 'success', '休假': 'warning', '离职': 'danger' }
  return map[status] || 'info'
}

function getInitials(name: string) {
  return name.charAt(0)
}

async function openPreference(emp: EmployeeDTO) {
  selectedEmployee.value = emp
  if (usingApi.value && emp.id && !emp.preference) {
    try {
      const pref = await getPreferences(emp.id)
      emp.preference = pref
    } catch {
      // leave preference undefined
    }
  }
  prefVisible.value = true
}

async function handlePrefSave(data: PreferenceDTO) {
  if (usingApi.value && selectedEmployee.value?.id) {
    try {
      await updatePreferences(selectedEmployee.value.id, data)
      selectedEmployee.value.preference = data
    } catch {
      // handled by interceptor
    }
  }
}

onMounted(() => loadEmployees())
</script>

<template>
  <div>
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-800 m-0">排班计划 - 员工列表</h1>
      <p class="text-gray-400 mt-1 text-sm">管理员工排班偏好与排班安排</p>
    </div>

    <!-- Filters -->
    <el-card shadow="never" class="mb-4">
      <div class="flex items-center gap-3 flex-wrap">
        <el-input
          v-model="searchName"
          placeholder="搜索姓名"
          clearable
          class="w-44"
          :prefix-icon="'Search'"
          @change="loadEmployees"
        />
        <el-select v-model="filterDept" placeholder="部门" clearable class="w-32" @change="loadEmployees">
          <el-option v-for="d in departments" :key="d" :label="d" :value="d" />
        </el-select>
        <el-select v-model="filterStatus" placeholder="状态" clearable class="w-28" @change="loadEmployees">
          <el-option v-for="s in statuses" :key="s" :label="s" :value="s" />
        </el-select>
      </div>
    </el-card>

    <!-- Table -->
    <el-card shadow="never">
      <el-table v-loading="loading" :data="employees" stripe style="width: 100%">
        <el-table-column label="员工" min-width="160">
          <template #default="{ row }">
            <div class="flex items-center gap-3">
              <el-avatar :size="36" class="bg-blue-500 shrink-0">{{ getInitials(row.name) }}</el-avatar>
              <div>
                <div class="font-medium text-gray-800">{{ row.name }}</div>
                <div class="text-xs text-gray-400">{{ row.id }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="position" label="职位" width="100" />
        <el-table-column prop="store" label="所属门店" width="140" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)" size="small" effect="plain">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="偏好班次" width="100">
          <template #default="{ row }">
            <span v-if="row.preference">{{ row.preference.preferredShift }}</span>
            <span v-else class="text-gray-300">未设置</span>
          </template>
        </el-table-column>
        <el-table-column label="周上限" width="90">
          <template #default="{ row }">
            <span v-if="row.preference">{{ row.preference.maxHoursPerWeek }}h</span>
            <span v-else class="text-gray-300">-</span>
          </template>
        </el-table-column>
        <el-table-column label="休息日" min-width="140">
          <template #default="{ row }">
            <div v-if="row.preference?.restDays?.length" class="flex gap-1 flex-wrap">
              <el-tag
                v-for="d in row.preference.restDays"
                :key="d"
                size="small"
                effect="plain"
                type="info"
              >{{ d }}</el-tag>
            </div>
            <span v-else class="text-gray-300">未设置</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="openPreference(row)">
              <el-icon class="mr-1"><Setting /></el-icon>
              偏好设置
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="flex justify-end mt-4">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          background
          @current-change="loadEmployees"
          @size-change="loadEmployees"
        />
      </div>
    </el-card>

    <!-- Preference Modal -->
    <PreferenceModal
      v-model:visible="prefVisible"
      :employee="selectedEmployee"
      @save="handlePrefSave"
    />
  </div>
</template>
