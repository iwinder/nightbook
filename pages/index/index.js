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
    if(e.detail.userInfo) {
      app.globalData.userInfo = e.detail.userInfo
      this.setData({
        userInfo: e.detail.userInfo,
        hasUserInfo: true
      })
    }

  }
})
