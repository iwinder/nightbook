var config = require("../../config")
var wcloud = require("../../lib/index")


Page({

  /**
   * 页面的初始数据
   */
  data: {
    colorObj: ["#337ab7", "#31b0d5", "#5cb85c","#f0ad4e"],
    pageHieght:0,
    redObj:{
      all:{
        countNum:0
      },
      now:{
        countNum: 0
      },
      no:{
        countNum: 0
      },
      finish:{
        countNum: 0
      }
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    wcloud.request({
      login: true,
      url: config.service.apiUrlhead + "findByUserBookNums",
      success: function (res) {
        if(res.data.code>0){
         

          var datas = res.data.result;
          var len = datas.length;
          var tmp = null;
          for(var i=0;i<len;i++){
            tmp  = datas[i];
            if (tmp.readStatus == 1){
              that.setData({
                "redObj.now": tmp
              });
            } else if (tmp.readStatus == 2){
              that.setData({
                "redObj.no": tmp
              });
            } else if (tmp.readStatus == 3){
              that.setData({
                "redObj.finish": tmp
              });
            } else if (tmp.readStatus == 4){
              that.setData({
                "redObj.all": tmp
              });
            }
          }
        }
      },
      fail:function(err){
        wx.showToast({
          title: err.Msg,
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
    
  }
})