var constants = require('./constants');
var Login = require('./login');
var Session = require('./session');
var Request = require('./request');


var exports = module.exports = {
    login: Login.login,
    setLoginUrl: Login.setLoginUrl,
    LoginError: Login.LoginError,
  
    clearSession: Session.clear,
  
    request: Request.request,
    RequestError: Request.RequestError,
  
  };

// 导出错误类型码
Object.keys(constants).forEach(function (key) {
    if (key.indexOf('ERR_') === 0) {
      exports[key] = constants[key];
    }
});
