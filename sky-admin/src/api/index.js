/**
 * 管理端接口集中管理。
 * 页面只负责展示和收集数据，HTTP 请求统一放在这里。
 */
import request from '@/utils/request'

// 所有分页接口都使用 page 和 pageSize，因此抽出公共方法。
const getPage = (url, params) => {
  return request({
    url,
    method: 'get',
    params
  })
}

export const employeeApi = {
  page: (params) => getPage('/admin/employee/page', params),
  save: (data) => request({ url: '/admin/employee', method: 'post', data }),
  update: (data) => request({ url: '/admin/employee', method: 'put', data }),
  changeStatus: (status, id) => request({
    url: `/admin/employee/status/${status}`,
    method: 'post',
    params: { id }
  })
}

export const categoryApi = {
  page: (params) => getPage('/admin/category/page', params),
  list: (type) => request({
    url: '/admin/category/list',
    method: 'get',
    params: { type }
  }),
  save: (data) => request({ url: '/admin/category', method: 'post', data }),
  update: (data) => request({ url: '/admin/category', method: 'put', data }),
  remove: (id) => request({
    url: '/admin/category',
    method: 'delete',
    params: { id }
  }),
  changeStatus: (status, id) => request({
    url: `/admin/category/status/${status}`,
    method: 'post',
    params: { id }
  })
}

export const dishApi = {
  page: (params) => getPage('/admin/dish/page', params),
  detail: (id) => request({ url: `/admin/dish/${id}`, method: 'get' }),
  save: (data) => request({ url: '/admin/dish', method: 'post', data }),
  update: (data) => request({ url: '/admin/dish', method: 'put', data }),
  remove: (ids) => request({
    url: '/admin/dish',
    method: 'delete',
    params: { ids: ids.join(',') }
  }),
  changeStatus: (status, id) => request({
    url: `/admin/dish/status/${status}`,
    method: 'post',
    params: { id }
  }),
  upload: (file) => {
    const form = new FormData()
    form.append('file', file)
    return request({
      url: '/admin/common/upload',
      method: 'post',
      data: form
    })
  }
}

export const setmealApi = {
  page: (params) => getPage('/admin/setmeal/page', params),
  detail: (id) => request({ url: `/admin/setmeal/${id}`, method: 'get' }),
  save: (data) => request({ url: '/admin/setmeal', method: 'post', data }),
  update: (data) => request({ url: '/admin/setmeal', method: 'put', data }),
  remove: (ids) => request({
    url: '/admin/setmeal',
    method: 'delete',
    params: { ids: ids.join(',') }
  }),
  upload: (file) => {
    const form = new FormData()
    form.append('file', file)
    return request({ url: '/admin/common/upload', method: 'post', data: form })
  },
  changeStatus: (status, id) => request({
    url: `/admin/setmeal/status/${status}`,
    method: 'post',
    params: { id }
  })
}

export const shopApi = {
  status: () => request({ url: '/admin/shop/status', method: 'get' }),
  setStatus: (status) => request({
    url: `/admin/shop/status/${status}`,
    method: 'put'
  })
}

export const orderApi = {
  page: (params) => getPage('/admin/order/conditionSearch', params),
  statistics: () => request({ url: '/admin/order/statistics', method: 'get' }),
  detail: (id) => request({ url: `/admin/order/details/${id}`, method: 'get' }),
  action: (name, data) => request({
    url: `/admin/order/${name}`,
    method: 'put',
    data
  }),
  delivery: (id) => request({ url: `/admin/order/delivery/${id}`, method: 'put' }),
  complete: (id) => request({ url: `/admin/order/complete/${id}`, method: 'put' })
}

export const reportApi = {
  turnover: (params) => getPage('/admin/report/turnoverStatistics', params),
  users: (params) => getPage('/admin/report/userStatistics', params),
  orders: (params) => getPage('/admin/report/ordersStatistics', params),
  top10: (params) => getPage('/admin/report/top10', params),
  export: (params) => request({ url: '/admin/report/export', method: 'get', params, responseType: 'blob' })
}
