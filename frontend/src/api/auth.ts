import request from './request'

export interface LoginResponse {
  token: string
  username: string
  role: string
  employeeId: number | null
}

export function login(username: string, password: string): Promise<LoginResponse> {
  return request.post('/auth/login', { username, password })
}

export function register(data: {
  username: string
  password: string
  email?: string
  employeeId?: number
}): Promise<void> {
  return request.post('/auth/register', data)
}
