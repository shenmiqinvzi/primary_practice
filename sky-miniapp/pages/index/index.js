const request = require('../../utils/request')

Page({
  data: {
    open: false,
    categories: [],
    activeCategoryId: null,
    goods: []
  },

  async onShow() {
    if (!wx.getStorageSync('token')) {
      wx.navigateTo({ url: '/pages/login/login' })
      return
    }

    const [status, categories] = await Promise.all([
      request({ url: '/user/shop/status' }),
      request({ url: '/user/category/list', data: { type: 1 } })
    ])

    this.setData({
      open: Number(status) === 1,
      categories,
      activeCategoryId: categories[0]?.id || null
    })

    if (categories[0]) {
      this.loadDishes(categories[0].id)
    }
  },

  async loadDishes(categoryId) {
    // 直接调用时传入数字；点击分类时需要从 data-id 取出数字。
    const id = typeof categoryId === 'object'
      ? categoryId.currentTarget.dataset.id
      : categoryId

    const goods = await request({
      url: '/user/dish/list',
      data: { categoryId: id }
    })

    this.setData({
      goods,
      activeCategoryId: id
    })
  },

  async addCart(event) {
    const dishId = event.currentTarget.dataset.id

    await request({
      url: '/user/shoppingCart/add',
      method: 'POST',
      data: { dishId }
    })

    wx.showToast({ title: '已加入购物车' })
  }
})
