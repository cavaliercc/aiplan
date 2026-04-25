<script setup lang="ts">
import { ref, watch } from 'vue'

const props = defineProps<{
  visible: boolean
  employee: {
    name: string
    preference?: {
      preferredShift: string
      maxHoursPerWeek: number
      restDays: string[]
      notes: string
    }
  } | null
}>()

const emit = defineEmits<{
  (e: 'update:visible', val: boolean): void
  (e: 'save', data: any): void
}>()

const form = ref({
  preferredShift: '早班',
  maxHoursPerWeek: 40,
  restDays: [] as string[],
  notes: '',
})

const shiftOptions = ['早班', '中班', '晚班', '灵活']
const dayOptions = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']

watch(
  () => props.employee,
  (emp) => {
    if (emp?.preference) {
      form.value = { ...emp.preference }
    } else {
      form.value = { preferredShift: '早班', maxHoursPerWeek: 40, restDays: [], notes: '' }
    }
  },
  { immediate: true }
)

function handleSave() {
  emit('save', { ...form.value })
  emit('update:visible', false)
}

function handleClose() {
  emit('update:visible', false)
}
</script>

<template>
  <el-dialog
    :model-value="visible"
    :title="`排班偏好设置 - ${employee?.name || ''}`"
    width="480px"
    @update:model-value="$emit('update:visible', $event)"
  >
    <el-form :model="form" label-width="100px" label-position="left">
      <el-form-item label="偏好班次">
        <el-radio-group v-model="form.preferredShift">
          <el-radio-button v-for="s in shiftOptions" :key="s" :value="s">{{ s }}</el-radio-button>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="每周上限">
        <el-input-number v-model="form.maxHoursPerWeek" :min="8" :max="56" :step="4" />
        <span class="text-gray-400 text-sm ml-2">小时/周</span>
      </el-form-item>

      <el-form-item label="休息日">
        <el-checkbox-group v-model="form.restDays">
          <el-checkbox v-for="d in dayOptions" :key="d" :value="d" :label="d" />
        </el-checkbox-group>
      </el-form-item>

      <el-form-item label="备注">
        <el-input v-model="form.notes" type="textarea" :rows="3" placeholder="如有特殊情况请备注" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSave">保存</el-button>
    </template>
  </el-dialog>
</template>
