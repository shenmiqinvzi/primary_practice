const request = require('../../utils/request')

Page({
  data: { list: [], formVisible: false, editing: false, form: {} },

  async onShow() {
    const list = await request({ url: '/user/addressBook/list' })
    this.setData({ list: list || [] })
  },

  openAdd() {
    this.setData({
      editing: false,
      formVisible: true,
      form: { consignee: '', sex: '1', phone: '', detail: '', label: '家' }
    })
  },

  openEdit(event) {
    this.setData({ editing: true, formVisible: true, form: { ...event.currentTarget.dataset.item } })
  },

  close() {
    this.setData({ formVisible: false })
  },

  input(event) {
    this.setData({ [`form.${event.currentTarget.dataset.field}`]: event.detail.value })
  },

  async save() {
    const form = this.data.form
    if (!form.consignee || !form.phone || !form.detail) {
      wx.showToast({ title: '请填写完整地址', icon: 'none' })
      return
    }
    await request({ url: '/user/addressBook' + (this.data.editing ? '' : '/save'), method: this.data.editing ? 'PUT' : 'POST', data: form })
    this.setData({ formVisible: false })
    this.onShow()
  },

  async remove(event) {
    await request({ url: `/user/addressBook?id=${event.currentTarget.dataset.id}`, method: 'DELETE' })
    this.onShow()
  },

  async setDefault(event) {
    await request({ url: '/user/addressBook/default', method: 'PUT', data: { id: event.currentTarget.dataset.id } })
    this.onShow()
  },

  select(event) {
    const pages = getCurrentPages()
    const previous = pages[pages.length - 2]
    if (previous && previous.route === 'pages/submit/submit') {
      previous.setData({ address: event.currentTarget.dataset.item })
      wx.navigateBack()
    }
  }
})
