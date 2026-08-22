const request = require('../../utils/request')

Page({
  data: { items: [], total: '0.00', address: null, remark: '' },

  async onShow() {
    const [items, address] = await Promise.all([
      request({ url: '/user/shoppingCart/list' }),
      request({ url: '/user/addressBook/default' })
    ])
    this.setData({ items: items || [], address })
    this.calculate(items || [])
  },

  calculate(items) {
    const total = items.reduce((sum, item) => sum + Number(item.amount || 0) * Number(item.number || 0), 0)
    this.setData({ total: total.toFixed(2) })
  },

  inputRemark(event) {
    this.setData({ remark: event.detail.value })
  },

  chooseAddress() {
    wx.navigateTo({ url: '/pages/address/address' })
  },

  async submit() {
    if (!this.data.address) {
      wx.showToast({ title: '请先选择收货地址', icon: 'none' })
      return
    }
    const result = await request({
      url: '/user/order/submit',
      method: 'POST',
      data: { addressBookId: this.data.address.id, payMethod: 1, remark: this.data.remark }
    })
    wx.redirectTo({ url: `/pages/pay/pay?id=${result.id}&orderNumber=${result.orderNumber}&amount=${result.orderAmount}` })
  }
})
