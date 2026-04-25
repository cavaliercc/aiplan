import request from './request'

export interface NotificationItem {
  id: number
  userId: number
  title: string
  content: string
  type: string
  isRead: boolean
  createTime: string
}

export function getNotifications(): Promise<NotificationItem[]> {
  return request.get('/notifications')
}

export function markAsRead(id: number): Promise<void> {
  return request.put(`/notifications/${id}/read`)
}
