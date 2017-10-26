
const app = getApp()
Page({
  data:{
    userInfo: {},
  },
  onLoad:function(){
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
    
      })
    } else if (this.data.canIUse) {
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
    
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
    
          })
        }
      })
    }
  },
  onShareAppMessage: function (res ) {
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target)
    }
    return {
      title: "我的书架",
      path: "/pages/userInfo/userInfo",
      success: function (res) {
        // 转发成功
        wx.reportAnalytics('share', {
          page: "userInfo",
          flag:"success"
        })
      },
      fail: function (res) {
        // 转发失败
        wx.reportAnalytics('share', {
          page: "userInfo",
          flag: "fail"

        })
      }
    }
  },
});