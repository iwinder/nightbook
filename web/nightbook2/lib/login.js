var config = require("../config");
var utils = require('./utils');
var constants = require('./constants');
var Session = require('./session');

/***
 * @class
 * 表示登录过程中发生的异常
 */
var LoginError = (function () {
    function LoginError(type, message) {
      Error.call(this, message);
      this.type = type;
      this.message = message;
    }
    LoginError.prototype = new Error();
    LoginError.prototype.constructor = LoginError;
    return LoginError;
})();

/**
 * 一个空函数,此方法不接受任何参数
 * 
 * @description 当某些时候你需要传入函数参数，而且希望它什么也不做的时候，你可以使用该函数，也无需再新建一个空的函数。
 */
var noop = function noop() { };

/**
 * 默认配置参数
 */
var defaultOptions = {
  method: 'POST',
  success: noop,
  fail: noop,
  loginUrl: null,
};


/**
 * 设置默认登录地址
 * @param {*} loginUrl 
 */
var setLoginUrl = function (loginUrl) {
    defaultOptions.loginUrl = loginUrl;
};

/**
 * 核心登录函数
 * 
 * @description 传入回调函数callback,当执行登录操作后，无论成功与否均会执行callback
 * @param {*} callback 
 */
var getWxLoginResult = function (callback) {
    // 登录
    wx.login({
        success: res => {
            // 发送 res.code 到后台换取 openId, sessionKey, unionId
            if (res.code) {
                // 获取用户信息
                wx.getUserInfo({
                    success: userResult => {
                      //执行回调函数
                      callback(null,{
                        pid: config.pid,
                        code: res.code,
                        encryptedData: userResult.encryptedData,
                        iv: userResult.iv,
                        userInfo: userResult.userInfo,
                      });
                    },
                    fail: userError => {
                        var error = new LoginError(constants.ERR_WX_GET_USER_INFO, '若体验完整功能，请登录授权');
                        callback(error, null);
                    }
                  })
            }else {
                var error = new LoginError(constants.ERR_WX_LOGIN_FAILED, '微信登录失败，请检查网络状态');
                callback(error, null);
            }
        }
    })
}

/**
 * 对外的登录接口实现
 * @description 传入参数options，执行登录
 * @param {*} options 
 */
var Login = function login(options) {
  options = utils.extend({}, defaultOptions, options);
    // 如无默认登录地址，返回错误
    if (!defaultOptions.loginUrl) {
        options.fail(new LoginError(constants.ERR_INVALID_PARAMS, '登录错误：缺少登录地址，请通过 setLoginUrl() 方法设置登录地址'));
        return;
    };

    /**
     *  创建实现登录方法的对象，实现getWxLoginResult
     */
    var doLogin = () => getWxLoginResult(function(wxLoginError, wxLoginResult) {
        if (wxLoginError) {
            options.fail(wxLoginError);
            return;
        }

        var userInfo = wxLoginResult.userInfo;

        // 构造请求头，包含 code、encryptedData 和 iv
        var code = wxLoginResult.code;
        var encryptedData = wxLoginResult.encryptedData;
        var iv = wxLoginResult.iv;
        var pid = wxLoginResult.pid;
        var header = {};

        header[constants.WX_HEADER_CODE] = code;
        header[constants.WX_HEADER_PID] = pid;
        header[constants.WX_HEADER_ENCRYPTED_DATA] = encryptedData;
        header[constants.WX_HEADER_IV] = iv;

        // 请求服务器登录地址，获得会话信息
        wx.request({
            url: options.loginUrl,
            header: header,
            method: options.method,
            data: options.data,
            success: function (result) {
              var data = result.data;
              // 成功地响应会话信息
              if (data && data[constants.WX_SESSION_MAGIC_ID]) {
                if (data.session) {
                  data.session.userInfo = userInfo;
                  Session.set(data.session);
                  options.success(userInfo);
                } else {
                  var errorMessage = '登录失败(' + data.error + ')：' + (data.message || '未知错误');
                  var noSessionError = new LoginError(constants.ERR_LOGIN_SESSION_NOT_RECEIVED, errorMessage);
                  options.fail(noSessionError);
                }
      
                // 没有正确响应会话信息
              } else {
                var errorMessage = '登录请求没有包含会话响应，请确保服务器处理 `' + options.loginUrl + '` 的时候正确使用了 SDK 输出登录结果';
                var noSessionError = new LoginError(constants.ERR_LOGIN_SESSION_NOT_RECEIVED, errorMessage);
                options.fail(noSessionError);
              }
            },
            fail: function (loginResponseError) {
                // 响应错误
                var error = new LoginError(constants.ERR_LOGIN_FAILED, '登录失败，可能是网络错误或者服务器发生异常');
                options.fail(error);
            },
          });

    });

    // 通过Session获取登录信息，若未登录或登录超时则重新请求登录。
    var session = Session.get();
    if (session) {
      wx.checkSession({
        success: function () {
          options.success(session.userInfo);
        },
        fail: function () {
          Session.clear();
          doLogin();
        },
      });
    } else {
      doLogin();
    }
}

// 输出
module.exports = {
    LoginError: LoginError,
    login: Login,
    setLoginUrl: setLoginUrl,
};
