var constants = require('./constants');
var utils = require('./utils');
var Session = require('./session');
var loginLib = require('./login');


/***
 * @class
 * 表示请求过程中发生的异常
 */
var RequestError = (function () {
    function RequestError(type, message) {
      Error.call(this, message);
      this.name = type;
      this.message = message;
    }
    RequestError.prototype = new Error();
    RequestError.prototype.constructor = RequestError;
    return RequestError;
  })();


/**
 * 一个空函数,此方法不接受任何参数
 * 
 * @description 当某些时候你需要传入函数参数，而且希望它什么也不做的时候，你可以使用该函数，也无需再新建一个空的函数。
 */
var noop = function noop() { };

/**
 * header中添加授权信息
 * @description 若Session存在，则在header中添加其id和key值
 * @param {*} session 
 */
var buildAuthHeader = function buildAuthHeader(session) {
    var header = {};
  
    if (session && session.id && session.skey) {
      header[constants.WX_HEADER_ID] = session.id;
      header[constants.WX_HEADER_SKEY] = session.skey;
    }
  
    return header;
};


function request(options) {
    
    if (typeof options !== 'object') {
      var message = '请求传参应为 object 类型，但实际传了 ' + (typeof options) + ' 类型';
      throw new RequestError(constants.ERR_INVALID_PARAMS, message);
    }
    
    // 是否需要登录
    var requireLogin = options.login;
    // 接口调用成功的回调函数
    var success = options.success || noop;
    // 接口调用成功的回调函数
    var fail = options.fail || noop;
    //  接口调用结束的回调函数（调用成功、失败都会执行）
    var complete = options.complete || noop;
    // 请求头header
    var originHeader = options.header || {};
  
    // 成功回调
    var callSuccess = function () {
      success.apply(null, arguments);
      complete.apply(null, arguments);
    };
  
    // 失败回调
    var callFail = function (error) {
      fail.call(null, error);
      complete.call(null, error);
    };
  
    // 是否已经进行过重试
    var hasRetried = false;
    
    // 根据是否需要登录选择请求方式
    if (requireLogin) {
      doRequestWithLogin();
    } else {
      doRequest();
    }
  
    // 登录后再请求
    function doRequestWithLogin() {
      loginLib.login({ success: doRequest, fail: callFail });
    }
  
    // 实际进行请求的方法
    function doRequest() {
      var authHeader = buildAuthHeader(Session.get());
  
      wx.request( utils.extend({}, options, {
        header: utils.extend({}, originHeader, authHeader),
  
        success: function (response) {
            var data = response.data;
       
            // 如果响应的数据里面包含 SDK Magic ID，表示被服务端 SDK 处理过，此时一定包含登录态失败的信息
            if (data && data[constants.WX_SESSION_MAGIC_ID]) {
                // 清除登录态
                Session.clear();

                var error, message;
                if (data.error === constants.ERR_INVALID_SESSION) {
                    // 如果是登录态无效，并且还没重试过，会尝试登录后刷新凭据重新请求
                    if (!hasRetried) {
                    hasRetried = true;
                    doRequestWithLogin();
                    return;
                    }

                    message = '登录态已过期';
                    error = new RequestError(data.error, message);

                } else {
                    message = '鉴权服务器检查登录态发生错误(' + (data.error || 'OTHER') + ')：' + (data.message || '未知错误');
                    error = new RequestError(constants.ERR_CHECK_LOGIN_FAILED, message);
                }

                callFail(error);
                return; 
            }
            if(data && data.error){
                error = new RequestError(data.error, data.status+": "+ data.message);
                callFail(error);
                return; 
            }
  
          callSuccess.apply(null, arguments);
        },
  
        fail: callFail,
        complete: noop,
      } )
      );
    };
  
  };

  module.exports = {
    RequestError: RequestError,
    request: request,
  };
