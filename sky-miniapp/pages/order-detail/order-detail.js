const request = require('../../utils/request')

Page({
  data: { order: null },

  async onLoad(options) {
    const order = await request({ url: `/user/order/orderDetail/${options.id}` })
    this.setData({ order })
  },

  async cancel() {
    await request({ url: '/user/order/cancel', method: 'PUT', data: { id: this.data.order.id } })
    wx.showToast({ title: '订单已取消' })
    this.onLoad({ id: this.data.order.id })
  },

  async reminder() {
    await request({ url: `/user/order/reminder/${this.data.order.id}` })
    wx.showToast({ title: '已提醒商家' })
  }
})
