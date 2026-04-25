import request from './request'

export interface StoreDTO {
  id: string
  name: string
  address: string
  manager: string
  phone: string
  businessHours: string
  status: string
  employeeCount: number
  area: number
}

export interface PageResult<T> {
  records: T[]
  total: number
  current: number
  size: number
}

export function getStores(params?: {
  page?: number
  size?: number
  name?: string
  status?: string
}): Promise<PageResult<StoreDTO>> {
  return request.get('/stores', { params })
}

export function getStoreById(id: string): Promise<StoreDTO> {
  return request.get(`/stores/${id}`)
}

export function createStore(data: Partial<StoreDTO>): Promise<StoreDTO> {
  return request.post('/stores', data)
}

export function updateStore(id: string, data: Partial<StoreDTO>): Promise<StoreDTO> {
  return request.put(`/stores/${id}`, data)
}

export function deleteStore(id: string): Promise<void> {
  return request.delete(`/stores/${id}`)
}
