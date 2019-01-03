// pages/userInfo/userInfo.js
var Login = require("../../lib/login") 
var wcloud = require("../../lib/index") 
var config = require("../../config")
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
  // 自定义函数start
  getUserInfo: function(e) {
    console.log(e)
    if(e.detail.userInfo) {
      let that = this;
      wcloud.login({ success: doRequest, fail: callFail });
      function doRequest(successda) {
        console.log("successda",successda);
        app.globalData.userInfo = e.detail.userInfo
        that.setData({
          userInfo: successda,
          hasUserInfo: true
        })
      }

      function callFail(err){
        console.log("callFail",err);
      }

    }
  },
  /**
   * tab切换
   * @param {*} e 
   */
  tabClick: function (e) {
    var that = this;
 
     this.setData({
       sliderOffset: e.currentTarget.offsetLeft,
       activeIndex: e.currentTarget.dataset.id,
       readType: this.data.tabs[e.currentTarget.dataset.id]
     });
     var readStatus = e.currentTarget.dataset.id+1;
     if (readStatus==4){
       readStatus = -1;
     }
     this.getReadNumOfStatus(readStatus);
     this.getReadObjInfos(that, readStatus);
   },
   getReadNumOfStatus: function (readStatus){
    var flg = this.getReadStatusOfText(readStatus);
    var readNum = this.data[flg].pageInfo.totalResultSize ? this.data[flg].pageInfo.totalResultSize :0;
    this.setData({
      readNum: readNum
    })
  },
  /**
   * 
   * @param {*} readStatus 
   */
  getReadStatusOfText: function (readStatus){
    var flg = "";
    if (readStatus == 1) {
      //在读
      flg = "readNowObj";
    } else if (readStatus == 2) {
        //未读
      flg = "readNoObj";
    } else if (readStatus == 3) {
       //已读
      flg = "readFinishObj";
    } else {
      //所有
      flg = "readAllObj";
    }
    return flg;
  },
  /**
   * 获取书籍列表信息
   * @param {*} that 
   * @param {*} readStatus 
   * @param {*} currentPage 
   */
  getReadObjInfos: function (that,readStatus, currentPage){
   
    // var currentPage = 0;
    var data = {};
    data.readStatus = readStatus;
    if (this.getIsAlllondStatus(readStatus)){
      return false;
    }
    wx.showLoading({
      title: '加载中',
    })
    var flg = this.getReadStatusOfText(readStatus);
 
    data.pageSize = this.data.readObjs.pageSize;
    data.currentPage = currentPage ? currentPage : this.data[flg].pageInfo.currentPage;
   
    wcloud.request({
      login: true,
      url: config.service.apiUrlhead+"findUserBook",
      data: data,
      success: function (response) {
        wx.hideLoading();
        //修改成功
        console.log("response",response);
        //判断是否需要切换table
        var activeIndexTmp = that.data.activeIndex+1;
        var readStatusTmp = readStatus;
        if (readStatusTmp<0){
          readStatusTmp = 4;
        }

        var readStatusTmpt = readStatusTmp -1;
        var newOffSetLeft = that.data.sliderOffsetUnit * readStatusTmpt;
        var oldOffSetLeft = that.data.sliderOffset;
        if ((activeIndexTmp != readStatusTmp) || ((activeIndexTmp == readStatusTmp)&&(newOffSetLeft != oldOffSetLeft))){
          //当前展示与所需不同时，需要切换
          that.tabClickByJS({
            "offsetLeft": newOffSetLeft,
            "datasetId": readStatusTmpt,
          },that);
        }

        var newarray = response.data.result.rows;
        var pageInfo = response.data.result.pageInfo;
        var newarrays = response.data.result.rows;
        //重新加载,对应newReadStatus的列表
        var newFlg = that.getReadStatusOfText(readStatus);

        if (pageInfo.currentPage == 1){
          that.setData({
            "readNum": pageInfo.totalResultSize,
          })
        }
        if (pageInfo.currentPage > 1) {
          newarrays = that.data[newFlg].readObjs.length > 0 ? that.data[newFlg].readObjs.concat(newarray) : newarray;
        }
        if (pageInfo.currentPage == pageInfo.totalPageSize) {
          that.data[newFlg].isAlllond = true;
        }
        var pageParm = newFlg + ".pageInfo";
        var readObjsParm = newFlg + ".readObjs";
        that.setData({
          [pageParm]: pageInfo,
          [readObjsParm]: newarrays,
       
        })

      },
      fail: function (err) {
        wx.hideLoading();
        wx.showToast({
          title: err.message,
          icon: 'none',
          duration: 2000
        })
      }
    });
  },
  /**
   * 检测是否已加载
   * @param {*} readStatus 
   */
  getIsAlllondStatus: function (readStatus){
    var flg = this.getReadStatusOfText(readStatus);
    return this.data[flg].isAlllond;
  },

})
