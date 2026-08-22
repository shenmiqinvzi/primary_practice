const request = require('../../utils/request')

Page({
  data: { setmeal: null, dishes: [] },

  async onLoad(options) {
    const categories = await request({ url: '/user/category/list', data: { type: 2 } })
    let setmeal = null
    for (const category of categories || []) {
      const list = await request({ url: '/user/setmeal/list', data: { categoryId: category.id } })
      setmeal = (list || []).find(item => Number(item.id) === Number(options.id))
      if (setmeal) break
    }
    const dishes = await request({ url: `/user/dish/setmeal/${options.id}` })
    this.setData({ setmeal, dishes: dishes || [] })
  },

  async addCart() {
    if (!this.data.setmeal) return
    await request({
      url: '/user/shoppingCart/add',
      method: 'POST',
      data: { setmealId: this.data.setmeal.id }
    })
    wx.showToast({ title: '已加入购物车' })
  }
})
