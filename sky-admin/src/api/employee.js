import request from '@/utils/request'

export const login = (data) => {
  return request({ url: '/admin/employee/login', method: 'post', data })
}
