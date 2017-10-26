var config = require("../../config")
var wcloud = require("../../lib/index")

Page({

  /**
   * 页面的初始数据
   */
  data: {
    inputShowed: false,
    inputVal: "",
    icon60:"../../image/test_book.jpg",
    pageObj:{
      start:0,
      count:10,
      total:0,
      currentPage: 0,
      totalPageSize: 0,
    },
    books: {},
    showModalStatus: false,
    addBookObj:{
      id:0,
      info:null,
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
    
  },
  showInput: function () {
    this.setData({
      inputShowed: true
    });
  },
  hideInput: function () {
    this.setData({
      inputVal: "",
      inputShowed: false
    });
  },
  clearInput: function () {
    this.setData({
      inputVal: ""
    });
  },
  inputTyping: function (e) {
    //搜索开始,初始化相关字段
    this.setData({
      inputVal: e.detail.value,
      start:0,
      total:0,
      currentPage: 0,
      totalPageSize:0,
      books: {}
    });
    this.findBookInfo();
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
    //若是最后一页直接返回，若不是则继续

    if (this.data.pageObj.currentPage < this.data.pageObj.totalPageSize){
      //不是最后一页，计算应该的start值
      this.data.pageObj.start = this.data.pageObj.currentPage * this.data.pageObj.count;
      this.data.pageObj.currentPage++;
      this.findBookInfo();
    }else{
      wx.showToast({
        title: '已到最底端',
      })
    }
  },

  findBookInfo:function(){
    var data = {
      keys: this.data.inputVal,
      start: this.data.pageObj.start,
      count: this.data.pageObj.count,
    };
    var that = this;
    wcloud.request({
      login: true,
      url: config.service.apiUrlhead +"getBookByKeys",
      data: data,
      success: function (res) {
        wx.hideLoading();
        
        if (res.data.code > 0) {
          var newarray = res.data.result.books;
          var newarrays = that.data.books.length > 0 ? that.data.books.concat(newarray) : newarray;
          that.setData({
            books: newarrays,
          });
          that.countPage(res.data.result.pageInfo);
        } else {
          wx.showToast({
            title: res.data.msg,
          })
        }
      },
      fail: function (err) {
        wx.hideLoading()
        wx.showToast({
          title: err.errMsg,
        })
      }
    })
  },
  countPage:function(pageInfo){
    this.data.pageObj.total = pageInfo.total;
    this.data.pageObj.totalPageSize = Math.ceil(pageInfo.total / this.data.pageObj.count);
    this.data.pageObj.currentPage = this.data.pageObj.start / this.data.pageObj.count + 1;
  },
  viewTap:function(e){
    var bid = e.currentTarget.dataset.bid;
    wx.navigateTo({
      url: '../bookInfo/bookInfo?bid=' + bid
    });
  },
  buttonTap:function(e){
    var id = e.currentTarget.id;
    var obj = this.data.books[id];
    this.setData({
      'addBookObj.info': obj,  
      readStatusIndex: 0,
      "addBookObj.newReadStatus": 1,
    });
    this.data.addBookObj.id = e.currentTarget.id;
    // 
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
            url: config.service.apiUrlhead +"addReadStatus",
            data: {
              bid: that.data.addBookObj.info.bid,
              readStatus: newReadStatus
            },
            success: function (response) {
              wx.hideLoading();
              //修改成功
              var tmp = "books[" + that.data.addBookObj.id + "].hasStatus";
              that.setData({
                [tmp]: 1
              })
            },
            fail: function (err) {
              wx.hideLoading();
              wx.showToast({
                title: err.errMsg,
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