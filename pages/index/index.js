// pages/userInfo/userInfo.js
var Login = require("../../lib/login") 
var qyloud = require("../../lib/index") 
var config = require("../../config")
var constants = require('../../lib/constants');
//获取应用实例
const app = getApp()
var sliderWidth = 96; // 需要设置slider的宽度，用于计算中间位置
var defaultPageInfo = new qyloud.PageInfo(null);
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
    isOnLoad: true,
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
      pageInfo: defaultPageInfo,
      bottomType: false,
      readObjs:[],
    },
    readNoObj:{
      pageInfo: defaultPageInfo,
      bottomType:false,
      readObjs: [],
    },
    readFinishObj:{
      pageInfo: defaultPageInfo,
      bottomType: false,
      readObjs: [],
    },
    readAllObj:{
      pageInfo: defaultPageInfo,
      bottomType: false,
      readObjs: [],
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // 手动更新页面
    var that = this;
    wx.getSystemInfo({
      success: function (res) {
        that.setData({
          scrollHeight: 100,
          sliderLeft: (res.windowWidth / that.data.tabs.length - sliderWidth) / 2,
          sliderOffset: res.windowWidth / that.data.tabs.length * that.data.activeIndex,
          sliderOffsetUnit: res.windowWidth / that.data.tabs.length,
        });
      }
    });
    // 
    var readStatus = that.data.activeIndex + 1;
    if (readStatus == 4) {
      readStatus = -1;
    }
    this.checkUserIsLogin();
    this.getReadObjInfos(that,readStatus);

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    this.setData({
      isOnLoad:false
    });
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

    if(!this.data.isOnLoad){  
      this.getReadObjInfos(this,1);
    }
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
  onShareAppMessage: function (res) {
    if (res.from === 'button') {
      // 来自页面内转发按钮
      console.log(res.target)
    }
    return {
      title: "我有"+  this.data.readNowObj.pageInfo.totalElements+"本书正在阅读",
      path: "/pages/index/index",
      success: function (res) {
        // 转发成功
        wx.reportAnalytics('share', {
          page: "index",
          flag: "success"
        })
      },
      fail: function (res) {
        // 转发失败
        wx.reportAnalytics('share', {
          page: "index",
          flag: "fail"

        })
      }
    }
  },
  // 自定义函数start
  /**
   *  登录授权Button事件
   * @param {*} e 
   */
  getUserInfo: function(e) {
    if(e.detail.userInfo) {
      let that = this;
      wx.showLoading({
        title: '加载中',
      })
      qyloud.login({ success: doRequest, fail: callFail });
      function doRequest(successda) {
        wx.hideLoading();
        app.globalData.userInfo = e.detail.userInfo;
        that.setData({
          userInfo: successda,
          hasUserInfo: true
        })
        var readStatus = that.data.activeIndex + 1;
        if (readStatus == 4) {
          readStatus = -1;
        }
        that.getReadObjInfos(that,readStatus);
      }
      function callFail(err){
        wx.hideLoading();
        wx.showToast({
          title: err.message,
          icon: 'none',
          duration: 2000
        })
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
       readStatusIndex: e.currentTarget.dataset.id,
       readType: this.data.tabs[e.currentTarget.dataset.id]
     });
     var readStatus = e.currentTarget.dataset.id+1;
    
     if (readStatus == 4){
       readStatus = -1;
       var flg = this.getReadStatusOfText(readStatus);
       this.data[flg].pageInfo.number = this.data[flg].pageInfo.number == 0? this.data[flg].pageInfo.number : 0;
       this.data[flg].pageInfo.last = false;
     }
     this.getReadNumOfStatus(readStatus);
     this.getReadObjInfos(that, readStatus);
   },
   getReadNumOfStatus: function (readStatus){
    var flg = this.getReadStatusOfText(readStatus);
    var readNum = this.data[flg].pageInfo.totalElements ? this.data[flg].pageInfo.totalElements :0;
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
    var data = {};
    if(readStatus!=-1){
      data.isRead = readStatus;
    }
    if (this.getIsAlllondStatus(readStatus)){
      return false;
    }
    wx.showLoading({
      title: '加载中',
    })
    var flg = this.getReadStatusOfText(readStatus);
    var defaultPage = this.data[flg].pageInfo.number ? this.data[flg].pageInfo.number:0;
    data.size = this.data.readObjs.pageSize;
    data.page = currentPage ? currentPage : defaultPage;
    data.sort = "createdDate,DESC"
    qyloud.request({
      login: true,
      url: config.service.apiUrlhead+"findUserBook",
      data: data,
      success: function (response) {
        wx.hideLoading();
        //修改成功
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

        var newarray = response.data.content;
        var pageInfo = new qyloud.PageInfo(response.data);
        
        var newarrays = response.data.content;
        //重新加载,对应newReadStatus的列表
        var newFlg = that.getReadStatusOfText(readStatus);
        if (pageInfo.first){
          that.setData({
            "readNum": pageInfo.totalElements,
          })
        }else {
          newarrays = that.data[newFlg].readObjs.length > 0 ? that.data[newFlg].readObjs.concat(newarray) : newarray;
        }

        // if (pageInfo.last) {
        //   // 判断是否已加载完
        //   that.data[newFlg].isAlllond = true;
        // }
        var pageParm = newFlg + ".pageInfo";
        var readObjsParm = newFlg + ".readObjs";
        that.setData({
          [pageParm]: pageInfo,
          [readObjsParm]: newarrays,
       
        })

      },
      fail: function (err) {
        wx.hideLoading();
        if(err.type==constants.ERR_WX_GET_USER_INFO){
          that.reSetAllisAlllondStatus();
        }
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
    return this.data[flg].pageInfo.last;
  },

  open: function () {
    //是否已授权登录，
    if (app.globalData.userInfo){
      //是--获取用户信息，调用toshowActionSheet
      this.toshowActionSheet(app.globalData.userInfo);
    }else{
      //否，调用login
      qyloud.login({ success: this.toshowActionSheet, fail: this.doLoginFail });
    }

    //
  },

  bindDownLoadOne:function(){
    var that = this;
    this.downLoadAddDataFunc(this, "readNowObj", 1);
   },
   bindDownLoadTwo: function () {
     var that = this;
     this.downLoadAddDataFunc(this, "readNoObj", 2);
   },
   bindDownLoadThree: function () {
     var that = this;
     this.downLoadAddDataFunc(this, "readFinishObj", 3);
   },
   bindDownLoadFour: function () {
     var that = this;
   
     this.downLoadAddDataFunc(this,"readAllObj",-1);
   },
   downLoadAddDataFunc:function(that,obj,num){
     var pageInfo = that.data[obj].pageInfo;
     var currentPage = pageInfo.number + 1;
     if (!pageInfo.last) {
       that.data[obj].pageInfo.number = currentPage;
       that.getReadObjInfos(that, num);
     } else {
       if (!that.data[obj].bottomType){
         that.data[obj].bottomType = true;
         wx.showToast({
           title: '没有更多内容了',
           icon: 'success',
           duration: 2000
         })
       }    
     }
   },
  /**
   * 
   */
   checkUserIsLogin(){
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse) {
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
   toshowActionSheet:function(userInfo){
    app.globalData.userInfo = userInfo;
    var that = this;
    wx.showActionSheet({
      itemList: ['扫码加书', '搜索加书'],
      success: function (res) {
        if (!res.cancel) {
          //0扫码
          if (res.tapIndex==0){
            
            that.toScanCode();
            // that.toCheckAndUseScanCode();
          }else{
            wx.navigateTo({
              url: '../search/search'
            });
            that.reSetAllisAlllondStatus();
          }
          //1搜索
        }
      }
    });
  },
  toCheckAndUseScanCode:function() {
    let that = this;
    wx.getSetting({
      success(res) {
        if (!res.authSetting['scope.camera']) {
          wx.authorize({
            scope: 'scope.camera',
            success() {
              that.toScanCode();
            }
          })
        }else {
          that.toScanCode();
        }
      }
    })
  },
  toScanCode:function(){
    var that = this;
    this.setData({
      readStatusIndex:0,
      "addBookObj.newReadStatus":1,
    })
    wx.showLoading({
      title: '加载中',
    })
    wx.scanCode({
      success: (res) => {
       
        qyloud.request({
          login: true,
          url: config.service.apiUrlhead+"getBookByISBN/"+res.result,
          data:{
          },
          success: function (response) {
            wx.hideLoading();
            // 设置新增对象
            that.setData({
              "addBookObj.info": response.data,
              "addBookObj.oldReadStatus": response.data.isRead,
              "addBookObj.addType": 1,
              // "readStatusIndex": response.data.result.readStatus - 1,
            });
            // 弹出图书阅读状态修改窗口
            that.powerDrawer("open");
         
          },
          fail: function (err) {
            wx.hideLoading()
            wx.showToast({
              title: err.message,
              icon: 'none',
              duration: 2000
            })
          }
        })
      }
    })
  },
  reSetAllisAlllondStatus:function(){
   
    this.data[this.getReadStatusOfText(1)].pageInfo.last = false;
    this.data[this.getReadStatusOfText(2)].pageInfo.last = false;
    this.data[this.getReadStatusOfText(3)].pageInfo.last = false;
    this.data[this.getReadStatusOfText(4)].pageInfo.last = false;
  },
  reSetisAlllondStatus: function (readStatus){
    var flg = this.getReadStatusOfText(readStatus);
    this.data[flg].pageInfo.last = false;
  },

  /**
   * 弹窗显示动画 
   * @param {*} currentStatu 
   */
  navDiyUtil: function (currentStatu) {
    /* 动画部分 */
    // 第1步：创建动画实例   
    var animation = wx.createAnimation({
      duration: 200,  //动画时长  
      timingFunction: "linear", //线性  
      delay: 0  //0则不延迟  
    });
    // 第2步：这个动画实例赋给当前的动画实例  
    this.animation = animation;
    // 第3步：执行第一组动画  
    animation.opacity(0).rotateX(-100).step();
    // 第4步：导出动画对象赋给数据对象储存  
    this.setData({
      animationData: animation.export()
    })
    // 第5步：设置定时器到指定时候后，执行第二组动画  
    setTimeout(function () {
      // 执行第二组动画  
      animation.opacity(1).rotateX(0).step();
      // 给数据对象储存的第一组动画，更替为执行完第二组动画的动画对象  
      this.setData({
        animationData: animation
      })
      //关闭  
      if (currentStatu == "close") {
        this.setData(
          {
            showNavStatus: false
          }
        );
      }
    }.bind(this), 200)
    // 显示  
    if (currentStatu == "open") {
      this.setData(
        {
          showNavStatus: true
        }
      );
    }
  },//util,
  /**
   * 点击列表中的书籍的弹窗事件
   * @description 展示和关闭均会执行
   * @param {*} e 
   */
  powerNavDrawer: function (e) {
    var currentStatu = e.currentTarget.dataset.statu;
    if (currentStatu !="close"){
      this.data.nowBookIbj.info = e.currentTarget.dataset.info;
      this.data.nowBookIbj.oldReadStatus = e.currentTarget.dataset.info.isRead;
      this.data.nowBookIbj.newReadStatus = e.currentTarget.dataset.info.isRead;
      this.data.addType = 2;
    }
    this.navDiyUtil(currentStatu)
  }, 
  /**
   * 阅读状态
   */
  modalDiyUtil: function (currentStatu) {
    /* 动画部分 */
    // 第1步：创建动画实例   
    var animation = wx.createAnimation({
      duration: 200,  //动画时长  
      timingFunction: "linear", //线性  
      delay: 0  //0则不延迟  
    });
    // 第2步：这个动画实例赋给当前的动画实例  
    this.animation = animation;
    // 第3步：执行第一组动画  
    animation.opacity(0).rotateX(-100).step();
    // 第4步：导出动画对象赋给数据对象储存  
    this.setData({
      animationData: animation.export()
    })
    // 第5步：设置定时器到指定时候后，执行第二组动画  
    setTimeout(function () {
      // 执行第二组动画  
      animation.opacity(1).rotateX(0).step();
      // 给数据对象储存的第一组动画，更替为执行完第二组动画的动画对象  
      this.setData({
        animationData: animation
      })
      //关闭  
      if (currentStatu == "close") {
        this.setData(
          {
            showModalStatus: false
          }
        );
      }
      this.setData( {
        readStatusIndex: this.data.addBookObj.oldReadStatus-1,
      });
      //
      if (currentStatu == "confirm") {
        var newReadStatus = this.data.addBookObj.newReadStatus;
        var oldReadStatus = this.data.addBookObj.oldReadStatus;
        var that = this;
        if (newReadStatus != oldReadStatus) {
          //不同，更新
          wx.showLoading({
            title: '修改中',
          })
          qyloud.request({
            login: true,
            url: config.service.apiUrlhead+"updateReadStatus",
            data: {
              bookId: that.data.addBookObj.info.bookId,
              isRead: newReadStatus
            },
            success: function (response) {
              wx.hideLoading();
              //修改成功
              //重新加载,对应newReadStatus的列表
              that.reSetisAlllondStatus(newReadStatus);
              that.getReadObjInfos(that, newReadStatus, 0);
            },
            fail: function (err) {
              wx.hideLoading();
              wx.showToast({
                title: err.message,
                icon: 'none',
                duration: 2000
              })
            }
          });//wcloud.request end
        } else {
          //不需要更新阅读状态
          //若本身为新添加，重新获取对应的列表，反之不更改          
          var addType = that.data.addBookObj.addType;
          if (addType == 1) {
            //新添加，重新获取对应列表
            that.reSetisAlllondStatus(newReadStatus);
            that.getReadObjInfos(that, newReadStatus, 0);
          }
        }
        this.setData( {
            showModalStatus: false
        });
      }//confirm
    }.bind(this), 200)
    // 显示  
    if (currentStatu == "open") {
      this.setData(
        {
          showModalStatus: true
        }
      );
    }
  },//util,
  /**
   * 选择阅读状态
   * @param {*} e 
   */
  bindReadStatusChange: function (e) {
    this.setData({
      readStatusIndex: e.detail.value,
      "addBookObj.newReadStatus":  parseInt(e.detail.value)+1,
    })
  },

  powerDrawer: function (e) {
    
    if (e != null && e == "open") {
      this.modalDiyUtil(e);
    } else {
      var currentStatu = e.currentTarget.dataset.statu;
      if (currentStatu == "openByNav"){
        this.setData({
          showNavStatus : false,
          addBookObj: this.data.nowBookIbj
        })
        currentStatu = "open"
     
      }
      this.modalDiyUtil(currentStatu);
      
      
    }

  },
  /**
   *  查看图书详情
   * @param {*} e 
   */
  lookInfo:function(e){
    var bid = this.data.nowBookIbj.info.bookId;
    wx.navigateTo({
      url: '../bookInfo/bookInfo?bid='+bid
    });
    this.setData({
      showNavStatus: false
    })
  },
  tabClickByJS:function(obj,that){
    that.setData({
      sliderOffset: obj.offsetLeft,
      activeIndex: obj.datasetId,
      readType: that.data.tabs[obj.datasetId]
    });
    this.getReadNumOfStatus(obj.datasetId);
  },
})
