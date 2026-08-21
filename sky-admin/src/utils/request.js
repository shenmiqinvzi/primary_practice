import axios from 'axios'
import { ElMessage } from 'element-plus'

// 1. 创建一个 axios 实例（配置好基地地址和超时时间）
const request = axios.create({
  baseURL: '/api',          // 所有请求自动加 /api 前缀（配合 Vite 代理）
  timeout: 10000
})

// 2. 请求拦截器（发请求前自动执行）
request.interceptors.request.use(config => {
  // 从 localStorage 拿 token
  const token = localStorage.getItem('token')
  if (token) {
    // 把 token 塞进请求头（后端 JwtTokenAdminInterceptor 从 header 取 token）
    config.headers.token = token
  }
  return config
})

// 3. 响应拦截器（收到响应后自动执行）
request.interceptors.response.use(
  response => {
    const res = response.data
    // 后端返回格式：{ code: 1, msg: 'success', data: ... }
    if (res.code !== 1) {
      // 失败：自动弹出错误消息
      ElMessage.error(res.msg || '请求失败')
      return Promise.reject(res)
    }
    // 成功：直接返回 data（这样业务代码里拿到的就是 data，不用写 .data.data）
    return res.data
  },
  error => {
    // 401 表示 token 过期或无效，跳回登录页
    if (error.response?.status === 401) {
      ElMessage.error('登录已过期，请重新登录')
      localStorage.removeItem('token')
      window.location.href = '/login'
    } else {
      ElMessage.error(error.message || '网络错误')
    }
    return Promise.reject(error)
  }
)

export default request