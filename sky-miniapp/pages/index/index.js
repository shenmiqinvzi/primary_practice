const request = require('../../utils/request')

Page({
  data: {
    open: false,
    categories: [],
    activeCategoryId: null,
    activeType: 1,
    goods: []
  },

  async onShow() {
    if (!wx.getStorageSync('token')) {
      wx.navigateTo({ url: '/pages/login/login' })
      return
    }

    const [status, dishCategories, setmealCategories] = await Promise.all([
      request({ url: '/user/shop/status' }),
      request({ url: '/user/category/list', data: { type: 1 } }),
      request({ url: '/user/category/list', data: { type: 2 } })
    ])
    const categories = [...(dishCategories || []), ...(setmealCategories || [])]

    this.setData({
      open: Number(status) === 1,
      categories,
      activeCategoryId: categories[0]?.id || null,
      activeType: categories[0]?.type || 1
    })

    if (categories[0]) {
      this.loadGoods(categories[0])
    }
  },

  async loadGoods(category) {
    const selected = category.currentTarget
      ? category.currentTarget.dataset
      : category
    const id = Number(selected.id)
    const type = Number(selected.type)

    const goods = await request({
      url: type === 1 ? '/user/dish/list' : '/user/setmeal/list',
      data: { categoryId: id }
    })

    this.setData({
      goods: (goods || []).map(item => ({ ...item, type })),
      activeCategoryId: id,
      activeType: type
    })
  },

  async addCart(event) {
    const item = event.currentTarget.dataset.item
    const data = item.type === 2 ? { setmealId: item.id } : { dishId: item.id }

    await request({
      url: '/user/shoppingCart/add',
      method: 'POST',
      data
    })

    wx.showToast({ title: '已加入购物车' })
  },

  openDetail(event) {
    const item = event.currentTarget.dataset.item
    const url = item.type === 2
      ? `/pages/setmeal/setmeal?id=${item.id}`
      : `/pages/dish/dish?id=${item.id}&categoryId=${this.data.activeCategoryId}`
    wx.navigateTo({ url })
  }
})
