const request = require('../../utils/request')
Page({
  data: { loading: false },
  async login() {
    this.setData({ loading: true })
    try {
      // wx.login 由微信提供，返回一次性 code；后端用它换取用户身份和 JWT。
      const loginResult = await new Promise((resolve, reject) => wx.login({ success: resolve, fail: reject }))
      const user = await request({ url: '/user/user/login', method: 'POST', data: { code: loginResult.code } })
      wx.setStorageSync('token', user.token)
      getApp().globalData.token = user.token
      wx.switchTab({ url: '/pages/index/index' })
    } finally { this.setData({ loading: false }) }
  }
})
