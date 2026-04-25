import request from './request'
import type { PageResult } from './stores'

export interface EmployeeDTO {
  id: number
  name: string
  gender: string
  phone: string
  position: string
  department: string
  store: string
  storeId: string
  status: string
  hireDate: string
  avatar: string
  preference?: PreferenceDTO
}

export interface PreferenceDTO {
  preferredShift: string
  maxHoursPerWeek: number
  restDays: string[]
  notes: string
}

export function getEmployees(params?: {
  page?: number
  size?: number
  name?: string
  storeId?: string
}): Promise<PageResult<EmployeeDTO>> {
  return request.get('/employees', { params })
}

export function getEmployeeById(id: number): Promise<EmployeeDTO> {
  return request.get(`/employees/${id}`)
}

export function createEmployee(data: Partial<EmployeeDTO>): Promise<EmployeeDTO> {
  return request.post('/employees', data)
}

export function updateEmployee(id: number, data: Partial<EmployeeDTO>): Promise<EmployeeDTO> {
  return request.put(`/employees/${id}`, data)
}

export function deleteEmployee(id: number): Promise<void> {
  return request.delete(`/employees/${id}`)
}

export function getPreferences(id: number): Promise<PreferenceDTO> {
  return request.get(`/employees/${id}/preferences`)
}

export function updatePreferences(id: number, data: PreferenceDTO): Promise<PreferenceDTO> {
  return request.put(`/employees/${id}/preferences`, data)
}
