import request from '@/utils/request'

// 登录接口
export const login = (data) => {
  return request({
    url: '/admin/employee/login',
    method: 'post',
    data
  })
}

// 获取员工列表（分页）
export const getEmployeeList = (params) => {
  return request({
    url: '/admin/employee/page',
    method: 'get',
    params   // GET 请求的参数放在 params 里
  })
}