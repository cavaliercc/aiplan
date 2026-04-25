<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getEmployees } from '../api/employees'
import type { EmployeeDTO } from '../api/employees'
import { employees as mockEmployees, departments, statuses } from '../mock/employees'

const loading = ref(false)
const employees = ref<EmployeeDTO[]>([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

const filterStore = ref('')
const filterStatus = ref('')
const searchName = ref('')

const storeOptions = ref([...new Set(mockEmployees.map((e) => e.store))])
const usingApi = ref(false)

async function loadEmployees() {
  loading.value = true
  try {
    const res = await getEmployees({
      page: currentPage.value,
      size: pageSize.value,
      name: searchName.value || undefined,
      storeId: filterStore.value || undefined,
    })
    employees.value = res.records
    total.value = Number(res.total)
    usingApi.value = true
  } catch {
    if (!usingApi.value) {
      // fallback to mock
      const filtered = mockEmployees.filter((e) => {
        if (filterStore.value && e.store !== filterStore.value) return false
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

function handleSearch() {
  currentPage.value = 1
  loadEmployees()
}

function handleReset() {
  filterStore.value = ''
  filterStatus.value = ''
  searchName.value = ''
  currentPage.value = 1
  loadEmployees()
}

function statusTag(status: string) {
  const map: Record<string, string> = { '在职': 'success', '休假': 'warning', '离职': 'danger' }
  return map[status] || 'info'
}

function getInitials(name: string) {
  return name.charAt(0)
}

onMounted(() => loadEmployees())
</script>

<template>
  <div>
    <div class="mb-6 flex items-center justify-between">
      <div>
        <h1 class="text-2xl font-bold text-gray-800 m-0">员工管理</h1>
        <p class="text-gray-400 mt-1 text-sm">共 {{ total }} 名员工</p>
      </div>
      <el-button type="primary">
        <el-icon class="mr-1"><Plus /></el-icon>
        添加员工
      </el-button>
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
          @change="handleSearch"
        />
        <el-select v-model="filterStore" placeholder="门店" clearable class="w-36" @change="handleSearch">
          <el-option v-for="s in storeOptions" :key="s" :label="s" :value="s" />
        </el-select>
        <el-select v-model="filterStatus" placeholder="状态" clearable class="w-28" @change="handleSearch">
          <el-option v-for="s in statuses" :key="s" :label="s" :value="s" />
        </el-select>
        <el-button @click="handleReset">重置</el-button>
      </div>
    </el-card>

    <!-- Table -->
    <el-card shadow="never">
      <el-table v-loading="loading" :data="employees" stripe style="width: 100%">
        <el-table-column label="员工" min-width="180">
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
        <el-table-column prop="department" label="部门" width="100" />
        <el-table-column prop="store" label="所属门店" width="140" />
        <el-table-column prop="phone" label="联系电话" width="150" />
        <el-table-column prop="hireDate" label="入职日期" width="120" />
        <el-table-column label="状态" width="90">
          <template #default="{ row }">
            <el-tag :type="statusTag(row.status)" size="small" effect="plain">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default>
            <el-button type="primary" link size="small">编辑</el-button>
            <el-button type="primary" link size="small">排班</el-button>
            <el-button type="danger" link size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
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
  </div>
</template>
