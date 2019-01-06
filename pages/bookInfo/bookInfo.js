var qyloud = require("../../lib/index")
var config = require("../../config")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    bookObj:{},
    showModalStatus: false,
    addBookObj: {
      id: 0,
      info: null,
      oldReadStatus: 1,
      newReadStatus: 1,
      addType: 1,//2 之前已存在 1新添加
    },
    readStatuss: ["在读", "未读", "已读"],
    readStatusIndex: 0,
    readerType:"user",
    isAuth: true
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.showLoading({
      title: '加载中',
    })
    var bookid = options.bid;
    this.setData({
      readerType:  options.type ? options.type : 'user'
    });
    // if(options.type == "share") {
      this.getByShare();
    // }
    var logflag = (options.type && options.type == "share") ? false :true;
    var that = this;
    qyloud.request({
      login: logflag,
      url: config.service.apiUrlhead +bookid,
      data: {
        
      },
      success: function (res) { 
        wx.hideLoading();
          that.setData({
            bookObj : res.data,
          });
      },
      fail: function (err) {
        wx.hideLoading();
        wx.showToast({
          title: err.message,
          icon: 'none',
          duration: 2000
        })
      }
    })
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
    
    return {
      title: "《"+  this.data.bookObj.title+"》这本书还不错，也推荐给你",
      path: "/pages/bookInfo/bookInfo?bid="+this.data.bookObj.id+"&type=share",
      success: function (res) {
        // 转发成功
        wx.reportAnalytics('share', {
          page: "bookInfo",
          flag: "success"
        })
      },
      fail: function (res) {
        // 转发失败
        wx.reportAnalytics('share', {
          page: "bookInfo",
          flag: "fail"

        })
      }
    }
    
  },
  buttonTap: function (e) {
    this.setData({
      'addBookObj.info': this.data.bookObj,
      readStatusIndex: 0,
      "addBookObj.newReadStatus": 1,
    });

    this.data.addBookObj.id = e.currentTarget.id;

    this.powerDrawer("open");


  },
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
      //
      if (currentStatu == "confirm") {
        var newReadStatus = this.data.addBookObj.newReadStatus;
        var oldReadStatus = this.data.addBookObj.oldReadStatus;
        var that = this;
        //不同，更新
        wx.showLoading({
          title: '加载中',
        })
        qyloud.request({
          login: true,
          url: config.service.apiUrlhead + "addReadStatus",
          data: {
            bookId: that.data.addBookObj.info.id,
            isRead: newReadStatus
          },
          success: function (response) {
            wx.hideLoading();
            //修改成功
            var tmp = "bookObj.hasStatus";
            that.setData({
              [tmp]: 1
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
        });//qyloud.request end
        wx.hideLoading();

        this.setData({
            showModalStatus: false
          });
      }//confirm
    }.bind(this), 200)
    // 显示  
    if (currentStatu == "open") {
      this.setData({
          showModalStatus: true
      });
    }


  },//util,
  powerDrawer: function (e) {

    if (e != null && e == "open") {
      this.modalDiyUtil(e);
    } else {
      var currentStatu = e.currentTarget.dataset.statu;
      this.modalDiyUtil(currentStatu);
    }

  },

  bindReadStatusChange: function (e) {
 
    this.setData({
      readStatusIndex: e.detail.value,
      "addBookObj.newReadStatus": parseInt(e.detail.value) + 1,
    })
  },
  getByShare:function(){
     // 获取用户信息
     wx.getSetting({
      success: res => {
        if (!res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
            this.setData({
              isAuth:  false
            });
        }else {
          this.setData({
            isAuth:  true
          });
        }
      }
    })
  },
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
        // app.globalData.userInfo = e.detail.userInfo;
        that.setData({
          // userInfo: successda,
          isAuth: true
        })
        var tmp = {};
        tmp.currentTarget.id = this.bookObj.info.id;
        this.buttonTap(tmp);
      }
      function callFail(err){
        console("getUserInfo",err);
        wx.hideLoading();
        wx.showToast({
          title: err.message,
          icon: 'none',
          duration: 2000
        })
      }

    }
  }, 
  goindex:function(e){
    wx.reLaunch({
      url: '/pages/index/index'
    });
  }
})
