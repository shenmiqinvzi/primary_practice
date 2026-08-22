const request = require('../../utils/request')

Page({
  data: { dish: null, flavor: '', categoryId: null },

  async onLoad(options) {
    const categoryId = Number(options.categoryId)
    const list = await request({ url: '/user/dish/list', data: { categoryId } })
    const dish = (list || []).find(item => Number(item.id) === Number(options.id))
    this.setData({ dish, categoryId })
  },

  inputFlavor(event) {
    this.setData({ flavor: event.detail.value })
  },

  async addCart() {
    const dish = this.data.dish
    if (!dish) return
    await request({
      url: '/user/shoppingCart/add',
      method: 'POST',
      data: { dishId: dish.id, dishFlavor: this.data.flavor }
    })
    wx.showToast({ title: '已加入购物车' })
  }
})
