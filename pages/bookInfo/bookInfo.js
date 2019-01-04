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
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var bookid = options.bid;
    console.log("bookid",bookid);
    var that = this;
    qyloud.request({
      login: true,
      url: config.service.apiUrlhead +bookid,
      data: {
        
      },
      success: function (res) { 
        wx.hideLoading();
        console.log("res",res)
        // if (res.data.code>0){
          that.setData({
            bookObj : res.data,
          });
          
        // }else{
        //   wx.showToast({
        //     title: res.data.msg,
        //     duration: 2000
        //   })
        // }
      },
      fail: function (err) {
        wx.hideLoading();
        wx.showToast({
          title: err.message.toString(),
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
        wcloud.request({
          login: true,
          url: config.service.apiUrlhead + "addReadStatus",
          data: {
            bid: that.data.addBookObj.info.bid,
            readStatus: newReadStatus
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
              title: err.errMsg,
              duration: 2000
            })
          }
        });//wcloud.request end
        wx.hideLoading();

        this.setData(
          {
            showModalStatus: false
          }
        );
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
})