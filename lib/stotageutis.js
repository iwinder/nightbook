var StotageUtils = {
    getSync: function (key) {
      return wx.getStorageSync(key) || null;
    },
  
    setSync: function (key,value) {
      wx.setStorageSync(key, value);
    },
  
    clearSync: function (key) {
      wx.removeStorageSync(key);
    },
};
  
module.exports = StotageUtils;