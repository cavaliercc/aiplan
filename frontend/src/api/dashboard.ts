import request from './request'

export interface DashboardKpiDTO {
  totalScheduledHours: number
  estimatedLaborCost: number
  avgEfficiency: number
  attendanceRate: number
}

export interface TrendDataDTO {
  day: string
  scheduled: number
  actual: number
}

export interface AlertDTO {
  type: string
  title: string
  desc: string
  time: string
  icon: string
}

export function getKpi(): Promise<DashboardKpiDTO> {
  return request.get('/dashboard/kpi')
}

export function getTrend(): Promise<TrendDataDTO[]> {
  return request.get('/dashboard/trend')
}

export function getAlerts(): Promise<AlertDTO[]> {
  return request.get('/dashboard/alerts')
}
