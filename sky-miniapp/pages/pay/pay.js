const request = require('../../utils/request')

Page({
  data: { id: null, orderNumber: '', amount: '0.00', loading: false },

  onLoad(options) {
    this.setData({ id: options.id, orderNumber: options.orderNumber, amount: options.amount })
  },

  async pay() {
    this.setData({ loading: true })
    try {
      await request({ url: '/user/order/payment', method: 'PUT', data: { orderNumber: this.data.orderNumber } })
      wx.showToast({ title: '支付成功' })
      setTimeout(() => wx.switchTab({ url: '/pages/order/order' }), 600)
    } finally {
      this.setData({ loading: false })
    }
  },

  later() {
    wx.switchTab({ url: '/pages/order/order' })
  }
})
