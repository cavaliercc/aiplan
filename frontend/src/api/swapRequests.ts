import request from './request'

export interface ShiftSwapRequestDTO {
  id: number
  requesterId: number
  targetId: number
  requesterAssignmentId: number
  targetAssignmentId: number
  reason: string
  status: string
}

export function createSwapRequest(data: Partial<ShiftSwapRequestDTO>): Promise<ShiftSwapRequestDTO> {
  return request.post('/swap-requests', data)
}

export function approveSwap(id: number): Promise<ShiftSwapRequestDTO> {
  return request.put(`/swap-requests/${id}/approve`)
}

export function rejectSwap(id: number): Promise<ShiftSwapRequestDTO> {
  return request.put(`/swap-requests/${id}/reject`)
}
