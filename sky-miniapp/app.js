// 小程序的入口文件。globalData 适合保存整个小程序都需要用到的数据。
App({ globalData: { token: wx.getStorageSync('token') || '' } })
