var config = require("../../config")
var qyloud = require("../../lib/index")

Page({

  /**
   * 页面的初始数据
   */
  data: {
    inputVal:"",
    maxLen:200,
    nowlen:0,
    butnFlag:true
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
  inputConfirm:function(e){
    this.setData({
      inputVal: e.detail.value,
      nowlen: e.detail.value.length
    });
 
   

    if (e.detail.value.length==0){
      this.setData({
        butnFlag: true,
      }); 
    } else if (e.detail.value.length > 0 && this.data.butnFlag){
      this.setData({
        butnFlag: false,
      }); 
    }
   
  },
  butnOk: function(e){
    var that = this;
    wx.showLoading({
      title: '提交中',
    })
    qyloud.request({
      login: true,
      url: config.service.apiUrlhead + "addIssues",
      data: {
        content: that.data.inputVal
      },
      success: function (res) {
        wx.hideLoading();
        
        wx.showToast({
          title: "提交成功",
          duration: 2000
        });
        that.setData({
          inputVal:"",
          butnFlag: true,
        });

      },
      fail:function(e){
        wx.showToast({
          title: e.message,
          icon: 'none',
          duration: 2000
        })
      }
    });
  }
})
