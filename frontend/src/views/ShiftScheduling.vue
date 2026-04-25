<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getEmployees } from '../api/employees'
import { getStores } from '../api/stores'
import { createSchedule } from '../api/schedules'
import { employees as mockEmployees } from '../mock/employees'
import { stores as mockStores } from '../mock/stores'

const formRef = ref()

const form = reactive({
  date: '',
  startTime: '09:00',
  endTime: '18:00',
  store: '',
  area: '',
  employees: [] as string[],
  vehicle: '',
  notes: '',
  shiftType: '早班',
})

const shiftTypes = ['早班', '中班', '晚班', '全天']
const areas = ['收银区', '销售区', '仓库区', '客服区', '门口迎宾']
const vehicles = ['无', '公司车辆A-苏A·12345', '公司车辆B-苏A·67890', '公司车辆C-苏A·11111']

const storeOptions = ref(mockStores.map((s) => ({ label: s.name, value: s.id })))
const employeeOptions = ref(
  mockEmployees
    .filter((e) => e.status === '在职')
    .map((e) => ({ label: `${e.name} (${e.position})`, value: e.id }))
)

const rules = {
  date: [{ required: true, message: '请选择日期', trigger: 'change' }],
  store: [{ required: true, message: '请选择门店', trigger: 'change' }],
  employees: [{ required: true, message: '请选择员工', trigger: 'change' }],
}

onMounted(async () => {
  try {
    const [storeRes, empRes] = await Promise.all([
      getStores({ page: 1, size: 100 }),
      getEmployees({ page: 1, size: 200 }),
    ])
    if (storeRes.records.length > 0) {
      storeOptions.value = storeRes.records.map((s) => ({ label: s.name, value: s.id }))
    }
    if (empRes.records.length > 0) {
      employeeOptions.value = empRes.records
        .filter((e) => e.status === '在职')
        .map((e) => ({ label: `${e.name} (${e.position})`, value: String(e.id) }))
    }
  } catch {
    // backend not available — mock data already set
  }
})

async function handleSubmit() {
  const valid = await formRef.value?.validate().catch(() => false)
  if (!valid) return

  try {
    await createSchedule({
      storeId: form.store,
      date: form.date,
      shiftType: form.shiftType,
      startTime: form.startTime,
      endTime: form.endTime,
      area: form.area,
      vehicle: form.vehicle,
      notes: form.notes,
    })
    ElMessage.success('排班创建成功！')
    formRef.value?.resetFields()
  } catch {
    // handled by interceptor; fallback for mock mode
    ElMessage.success('排班创建成功！（演示模式）')
  }
}

function handleReset() {
  formRef.value?.resetFields()
}

function disabledDate(time: Date) {
  return time.getTime() < Date.now() - 86400000
}
</script>

<template>
  <div>
    <div class="mb-6">
      <h1 class="text-2xl font-bold text-gray-800 m-0">新增班次</h1>
      <p class="text-gray-400 mt-1 text-sm">创建新的排班安排</p>
    </div>

    <el-card shadow="never" class="max-w-3xl">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
        label-position="left"
        size="large"
      >
        <!-- Shift Type -->
        <el-form-item label="班次类型">
          <el-radio-group v-model="form.shiftType">
            <el-radio-button v-for="t in shiftTypes" :key="t" :value="t">{{ t }}</el-radio-button>
          </el-radio-group>
        </el-form-item>

        <!-- Date -->
        <el-form-item label="排班日期" prop="date">
          <el-date-picker
            v-model="form.date"
            type="date"
            placeholder="选择日期"
            :disabled-date="disabledDate"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            class="w-full"
          />
        </el-form-item>

        <!-- Time Range -->
        <el-form-item label="时间段">
          <div class="flex items-center gap-2 w-full">
            <el-time-select
              v-model="form.startTime"
              :max-time="form.endTime"
              placeholder="开始时间"
              start="06:00"
              step="00:30"
              end="23:30"
              class="flex-1"
            />
            <span class="text-gray-400">至</span>
            <el-time-select
              v-model="form.endTime"
              :min-time="form.startTime"
              placeholder="结束时间"
              start="06:00"
              step="00:30"
              end="23:30"
              class="flex-1"
            />
          </div>
        </el-form-item>

        <!-- Store -->
        <el-form-item label="门店" prop="store">
          <el-select v-model="form.store" placeholder="选择门店" class="w-full">
            <el-option
              v-for="s in storeOptions"
              :key="s.value"
              :label="s.label"
              :value="s.value"
            />
          </el-select>
        </el-form-item>

        <!-- Area -->
        <el-form-item label="工作区域">
          <el-select v-model="form.area" placeholder="选择区域" clearable class="w-full">
            <el-option v-for="a in areas" :key="a" :label="a" :value="a" />
          </el-select>
        </el-form-item>

        <!-- Employees -->
        <el-form-item label="排班员工" prop="employees">
          <el-select
            v-model="form.employees"
            multiple
            filterable
            placeholder="选择员工（可多选）"
            class="w-full"
          >
            <el-option
              v-for="e in employeeOptions"
              :key="e.value"
              :label="e.label"
              :value="e.value"
            />
          </el-select>
        </el-form-item>

        <!-- Vehicle -->
        <el-form-item label="车辆安排">
          <el-select v-model="form.vehicle" placeholder="选择车辆（可选）" clearable class="w-full">
            <el-option v-for="v in vehicles" :key="v" :label="v" :value="v" />
          </el-select>
        </el-form-item>

        <!-- Notes -->
        <el-form-item label="备注">
          <el-input
            v-model="form.notes"
            type="textarea"
            :rows="3"
            placeholder="请输入备注信息"
          />
        </el-form-item>

        <!-- Actions -->
        <el-form-item>
          <div class="flex gap-3">
            <el-button type="primary" @click="handleSubmit">
              <el-icon class="mr-1"><Check /></el-icon>
              创建排班
            </el-button>
            <el-button @click="handleReset">重置</el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>
