const request = require('../../utils/request')

Page({
  data: {
    items: [],
    total: 0
  },

  async onShow() {
    const items = await request({
      url: '/user/shoppingCart/list'
    })

    const total = (items || [])
      .reduce((sum, item) => {
        const price = Number(item.amount || item.price || 0)
        return sum + price * item.number
      }, 0)
      .toFixed(2)

    this.setData({ items, total })
  },

  async change(event) {
    const item = event.currentTarget.dataset.item
    const add = event.currentTarget.dataset.add

    await request({
      url: add ? '/user/shoppingCart/add' : '/user/shoppingCart/sub',
      method: 'POST',
      data: {
        dishId: item.dishId,
        setmealId: item.setmealId,
        dishFlavor: item.dishFlavor
      }
    })

    this.onShow()
  },

  async clean() {
    await request({
      url: '/user/shoppingCart/clean',
      method: 'DELETE'
    })

    this.onShow()
  }
})
