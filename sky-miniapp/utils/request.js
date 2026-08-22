/**
 * 小程序版请求封装。
 * 浏览器里的 axios 在小程序中不能用，所以这里用 wx.request 包装成 Promise。
 */
const baseURL = 'http://localhost:8080'
const request = ({ url, method = 'GET', data = {} }) => new Promise((resolve, reject) => {
  const token = wx.getStorageSync('token')
  wx.request({
    url: baseURL + url,
    method,
    data,
    // 用户端后端拦截器要求的请求头名字是 authentication，不是管理端的 token。
    header: token ? { authentication: token } : {},
    success: (response) => {
      const result = response.data
      if (result.code === 1) resolve(result.data)
      else { wx.showToast({ title: result.msg || '请求失败', icon: 'none' }); reject(result) }
    },
    fail: reject
  })
})
module.exports = request
