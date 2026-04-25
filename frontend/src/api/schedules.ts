import request from './request'
import type { PageResult } from './stores'

export interface ScheduleDTO {
  id: number
  storeId: string
  date: string
  shiftType: string
  startTime: string
  endTime: string
  area: string
  vehicle: string
  notes: string
  assignments?: ScheduleAssignmentDTO[]
}

export interface ScheduleAssignmentDTO {
  id: number
  scheduleId: number
  employeeId: number
  employeeName: string
}

export function getSchedules(params?: {
  page?: number
  size?: number
  storeId?: string
  date?: string
}): Promise<PageResult<ScheduleDTO>> {
  return request.get('/schedules', { params })
}

export function getScheduleById(id: number): Promise<ScheduleDTO> {
  return request.get(`/schedules/${id}`)
}

export function createSchedule(data: Partial<ScheduleDTO>): Promise<ScheduleDTO> {
  return request.post('/schedules', data)
}

export function updateSchedule(id: number, data: Partial<ScheduleDTO>): Promise<ScheduleDTO> {
  return request.put(`/schedules/${id}`, data)
}

export function deleteSchedule(id: number): Promise<void> {
  return request.delete(`/schedules/${id}`)
}

export function addAssignment(
  scheduleId: number,
  data: Partial<ScheduleAssignmentDTO>
): Promise<ScheduleAssignmentDTO> {
  return request.post(`/schedules/${scheduleId}/assignments`, data)
}
