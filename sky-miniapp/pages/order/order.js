const request = require('../../utils/request')

Page({
  data: {
    orders: []
  },

  async onShow() {
    if (!wx.getStorageSync('token')) return

    const result = await request({
      url: '/user/order/historyOrders',
      data: {
        page: 1,
        pageSize: 20
      }
    })

    this.setData({
      orders: result?.records || []
    })
  },

  async cancel(event) {
    await request({
      url: '/user/order/cancel',
      method: 'PUT',
      data: {
        id: event.currentTarget.dataset.id,
        cancelReason: '用户取消'
      }
    })

    wx.showToast({ title: '订单已取消' })
    this.onShow()
  },

  detail(event) {
    wx.navigateTo({ url: `/pages/order-detail/order-detail?id=${event.currentTarget.dataset.id}` })
  },

  pay(event) {
    const order = event.currentTarget.dataset.order
    wx.navigateTo({ url: `/pages/pay/pay?id=${order.id}&orderNumber=${order.number}&amount=${order.amount}` })
  },

  async reminder(event) {
    await request({
      url: `/user/order/reminder/${event.currentTarget.dataset.id}`
    })

    wx.showToast({ title: '已提醒商家' })
  }
})
