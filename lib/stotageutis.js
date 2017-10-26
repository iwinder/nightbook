var StotageUtils = {
  get: function (key) {
    return wx.getStorageSync() || null;
  },

  set: function (key,value) {
    wx.setStorageSync(key, value);
  },

  clear: function (key) {
    wx.removeStorageSync(key);
  },
};

module.exports = StotageUtils;