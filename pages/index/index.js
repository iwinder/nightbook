// pages/userInfo/userInfo.js
//获取应用实例
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    tabs: ["在读", "未读", "已读", "所有"],
    activeIndex: 0,
    sliderOffset: 0,
    sliderOffsetUnit:0,
    sliderLeft: 0,
    readType:"在读",
    readNum:0,
    grids: [0, 1, 2, 3, 4, 5, 6, 7, 8],
    imgMode: 'aspectFit',
    imgWith:300,
    imgHeigh:400,
    imgSrc:'../../image/test_book.jpg',
    scrollTop: 0,
    scrollHeight:0,

    sessionStr:null,
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),

    showModalStatus: false,
    showNavStatus:false,
    readStatuss: ["在读", "未读", "已读"],
    readStatusIndex: 0,
    nowBookIbj:{

    },
    addBookObj:{
      info:null,
      oldReadStatus:1,
      newReadStatus:1,
      addType:1,//2 之前已存在 1新添加
    },
    readObjs:{
      pageSize:10
    },
    readNowObj:{
      pageInfo:{
        pageSize:0,
        currentPage:1,
        totalResultSize:0
      },
      bottomType: false,
      isAlllond: false,
      readObjs:[],
    },
    readNoObj:{
      pageInfo: {
        pageSize: 0,
        currentPage: 1,
        totalResultSize: 0
      },
      bottomType:false,
      isAlllond: false,
      readObjs: [],
    },
    readFinishObj:{
      pageInfo: {
        pageSize: 0,
        currentPage: 1,
        totalResultSize: 0
      },
      bottomType: false,
      isAlllond:false,
      readObjs: [],
    },
    readAllObj:{
      pageInfo: {
        pageSize: 0,
        currentPage: 1,
        totalResultSize: 0
      },
      bottomType: false,
      isAlllond: false,
      readObjs: [],
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse){
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  },
  getUserInfo: function(e) {
    console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  }
})
