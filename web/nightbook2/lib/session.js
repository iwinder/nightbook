var constants = require('./constants');
var StotageUtils = require('./stotageutis');
var SESSION_KEY = 'weapp_session_' + constants.WX_SESSION_MAGIC_ID;

var Session = {
  get: function () {
    return StotageUtils.getSync(SESSION_KEY);
  },

  set: function (session) {
    StotageUtils.setSync(SESSION_KEY, session);
  },

  clear: function () {
    StotageUtils.clearSync(SESSION_KEY);
  },
};

module.exports = Session;