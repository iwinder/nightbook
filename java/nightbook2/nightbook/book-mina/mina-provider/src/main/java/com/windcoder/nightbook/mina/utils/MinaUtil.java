package com.windcoder.nightbook.mina.utils;

import com.windcoder.nightbook.common.utils.Base64Util;
import com.windcoder.nightbook.common.utils.Pkcs7Encoder;
import com.windcoder.nightbook.mina.exception.MinaAuthorizationAPIException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class MinaUtil {
    /**
     * 解密用户数据
     * @param appid
     *          小程序唯一标识
     * @param sessionKey
     *          会话密钥-通过前端登陆的code换取
     * @param encryptedData
     *          包括敏感数据在内的完整用户信息的加密数据-前端wx.getUserInfo获取
     * @param iv
     *          加密算法的初始向量-前端wx.getUserInfo获取
     * @return
     */
    public static JSONObject decrypt(String appid, String sessionKey, String encryptedData, String iv) throws MinaAuthorizationAPIException {
        byte[] sessionKeyBy = Base64Util.decode(sessionKey);
        byte[] encryptedDataBy = Base64Util.decode(encryptedData);
        byte[] ivBy = Base64Util.decode(iv);

        byte[] dec = Pkcs7Encoder.decryptOfDiyIV(encryptedDataBy, sessionKeyBy,ivBy);
        JSONObject j = null;
        try {
            j = new JSONObject(new String(dec,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new MinaAuthorizationAPIException("DECRYPT__USERINFO_ERR");
        }
        return j;
    }
}
