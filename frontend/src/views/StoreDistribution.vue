<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getStores } from '../api/stores'
import type { StoreDTO } from '../api/stores'
import { stores as mockStores } from '../mock/stores'

const stores = ref<StoreDTO[]>(mockStores as unknown as StoreDTO[])

onMounted(async () => {
  try {
    const res = await getStores({ page: 1, size: 100 })
    if (res.records.length > 0) {
      stores.value = res.records
    }
  } catch {
    // backend not available — mock data already loaded
  }
})

function statusColor(status: string) {
  const map: Record<string, string> = {
    '营业中': 'success',
    '休息中': 'warning',
    '装修中': 'info',
  }
  return map[status] || 'info'
}
</script>

<template>
  <div>
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-800 m-0">门店分布</h1>
      <p class="text-gray-400 mt-1 text-sm">共 {{ stores.length }} 家门店</p>
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
      <el-card
        v-for="store in stores"
        :key="store.id"
        shadow="hover"
        class="store-card relative"
      >
        <!-- Status Badge -->
        <el-tag
          :type="statusColor(store.status)"
          size="small"
          class="absolute top-3 right-3"
          effect="dark"
        >
          {{ store.status }}
        </el-tag>

        <div class="mb-3">
          <span class="text-xs text-gray-400">{{ store.id }}</span>
          <h3 class="text-lg font-semibold text-gray-800 mt-1 m-0">{{ store.name }}</h3>
        </div>

        <div class="space-y-2 text-sm">
          <div class="flex items-start gap-2 text-gray-500">
            <el-icon class="mt-0.5 shrink-0"><Location /></el-icon>
            <span>{{ store.address }}</span>
          </div>
          <div class="flex items-center gap-2 text-gray-500">
            <el-icon><User /></el-icon>
            <span>店长: {{ store.manager }}</span>
          </div>
          <div class="flex items-center gap-2 text-gray-500">
            <el-icon><Phone /></el-icon>
            <span>{{ store.phone }}</span>
          </div>
          <div class="flex items-center gap-2 text-gray-500">
            <el-icon><Clock /></el-icon>
            <span>{{ store.businessHours }}</span>
          </div>
        </div>

        <div class="flex items-center justify-between mt-4 pt-3 border-t border-gray-100">
          <div class="text-xs text-gray-400">
            <span class="text-blue-500 font-semibold text-base">{{ store.employeeCount }}</span> 名员工
          </div>
          <div class="text-xs text-gray-400">
            {{ store.area }}㎡
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.store-card :deep(.el-card__body) {
  padding: 20px;
}
</style>
