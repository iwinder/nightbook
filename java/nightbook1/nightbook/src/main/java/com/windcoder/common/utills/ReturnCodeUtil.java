package com.windcoder.common.utills;

/**
 * Created by wind on 2016/12/23.
 */
public class ReturnCodeUtil {
    public static final Integer MA_OK 					              = 0;       //成功返回码
    public static final Integer  MA_MYSQL_ERR                         = 1001;    // Mysql错误等
    public static final Integer  MA_NO_INTERFACE                      = 1002;    // 接口参数不存在
    public static final Integer MA_PARA_ERR                           = 1003;    //参数错误
    public static final Integer MA_DECRYPT_ERR                        = 60021;   //解密失败
    public static final Integer MA_CRYPT_USERINFO_ERR                 = 60022;   //编码用户信息失败
    public static final Integer MA_DECRYPT__USERINFO_ERR              = 60023;   //解密用户信息失败

    public static final Integer MA_WEIXIN_NET_ERR                     = 1005;    //连接微信服务器失败
    public static final Integer MA_WEIXIN_CODE_ERR                    = 40029;   //CODE无效
    public static final Integer MA_CHANGE_SESSION_ERR                 = 1006;    //新增修改SESSION失败
    public static final Integer MA_WEIXIN_RETURN_ERR                  = 1007;    //微信返回值错误
    public static final Integer MA_AUTH_ERR                           = 60012;   //鉴权失败
    public static final Integer MA_UPDATE_LASTVISITTIME_ERR           = 1008;    //更新最近访问时间失败
    public static final Integer MA_REQUEST_ERR                        = 1009;    //请求包不是json
    public static final Integer MA_INTERFACE_ERR                      = 1010;    //接口名称错误
    public static final Integer MA_NO_PARA                            = 1011;    //不存在参数
    public static final Integer MA_NO_APPID                           = 1012;    //不能获取AppID
    public static final Integer MA_INIT_APPINFO_ERR                   = 1013;    //初始化AppID失败

}
