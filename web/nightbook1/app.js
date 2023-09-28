//app.js
var config = require("./config")
var wcloud = require("./lib/index") 
App({
  onLaunch: function () {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)
    wcloud.setLoginUrl(config.service.loginUrl);
    // var that = this;

    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo

              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
        }
      }
    })
  },
  doLoginSuccess:function(obj){
    this.globalData.userInfo = obj;

  },
  doLoginFail:function(obj){
    console.log(obj)
  },
  doLogin:function(userInfo,callback){
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        if (res.code) {
         var loginCode = res.code;
         wx.getUserInfo({
           success: res => {
             // 可以将 res 发送给后台解码出 unionId
             this.globalData.userInfo = res.userInfo
             wx.request({
               url: config.service.loginUrl,
               data:{
                pid:config.pid, 
                jcode:loginCode, 
                encryptData:res.encryptedData, 
                iv:res.iv
               },
               success:res =>{
                 
                 console.log(res);
               }
             })
             // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
             // 所以此处加入 callback 以防止这种情况
             if (this.userInfoReadyCallback) {
               this.userInfoReadyCallback(res);
               callback(null, res);
             }
           },
           fail : res =>{
             wx.showModal({ title:"无法完成授权",content:"需要授权才可进行"}) // 当拒绝授权的时候，提示用户需要开启权限，确认时调用微信的权限管理界面
             callback('fail to modify scope', null)
           }
         })
        } else {
          wx.showModal({ title: "获取用户登录态失败！", content: res.errMsg })
          console.log('获取用户登录态失败！' + res.errMsg)
        }
      }
    })
  },
  globalData: {
    userInfo: null
  }
})