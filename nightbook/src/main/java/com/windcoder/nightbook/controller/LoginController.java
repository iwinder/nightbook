package com.windcoder.nightbook.controller;

import com.windcoder.common.utills.*;
import com.windcoder.common.utills.https.HttpsUtil;
import com.windcoder.nightbook.entity.AppInfo;
import com.windcoder.nightbook.entity.SessionInfo;
import com.windcoder.nightbook.entity.WXHeader;
import com.windcoder.nightbook.entity.WaferReturn;
import com.windcoder.nightbook.services.IAppInfoService;
import com.windcoder.nightbook.services.ISessionInfoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-08
 * Time: 18:38 下午
 */
@Controller
@RequestMapping("/auth/")
public class LoginController {
    private static Logger logger = LogManager.getLogger(LoginController.class);
    @Autowired
    private IAppInfoService appInfoService;
    @Autowired
    private ISessionInfoService sessionInfoService;


    @ResponseBody
    @RequestMapping("home")
    public String home(HttpServletRequest request) {
        logger.error("jsapi/home.do");
        WXHeader header = new WXHeader();
        header.setPid(Integer.valueOf(request.getHeader(Constants.WX_HEADER_PID)));
        header.setJcode(request.getHeader(Constants.WX_HEADER_CODE));
        header.setEncryptData(request.getHeader(Constants.WX_HEADER_ENCRYPTED_DATA));
        header.setIv(request.getHeader(Constants.WX_HEADER_IV));

       return doLoginMain( header.getPid(), header.getJcode(), header.getEncryptData(), header.getIv());
    }

    /**
     * 登录主过程
     * @param pid
     * @param jcode
     * @param encryptData
     * @param iv
     * @return
     */
    public String doLoginMain(Integer pid,String jcode,String encryptData,String iv){
        if (!StringUtillZ.allIsNotEmpyty(jcode)){
            return "ss";
        }
        AppInfo appInfo = appInfoService.findAppInfoByPid(pid);
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appInfo.getAppid()+"&secret="+appInfo.getSecret()+"&js_code="+jcode+"&grant_type=authorization_code";
        JSONObject resultData = HttpsUtil.httpRequest(url, "GET", "");
        System.out.println("sjs error:"+resultData.toString());
        if(resultData.has("data")){
            String dataStr = resultData.getString("data");
            JSONObject result = new JSONObject(dataStr);
            if(result.has("openid")&&result.has("session_key")&&result.has("expires_in")){
                SessionInfo s = new SessionInfo();
                s.setPid(pid);
                s.setUuid(UUIDUtil.newUUID());
                s.setSkey(UUIDUtil.newUUID());
                s.setCreateTime(DateUtillZ.getTimestampOfNow());
                s.setLastVisitTime(DateUtillZ.getTimestampOfNow());
                s.setOpenId(result.getString("openid"));
                s.setSessionKey(result.getString("session_key"));
                JSONObject j = decrypt(appInfo.getAppid(),s.getSessionKey(),encryptData,iv);
                if(j.has("openId")){
                    String userStr = Base64Util.encode(j.toString());
                    System.out.println("userStr:"+j.toString());
                    if(userStr!=null){
                        s.setUserInfo(userStr);
                        ReturnResult r = sessionInfoService.changeCsessioninfo(s);
                        if(r.getCode()>0){
                            JSONObject sobj = new JSONObject();
                            sobj.put("id",r.getMsg());
                            sobj.put("skey",s.getSkey());
                            JSONObject returnResult = new JSONObject();
                            returnResult.put(Constants.WX_SESSION_MAGIC_ID,1);
                            returnResult.put("session",sobj);
//                            sobj.put("pid",pid);
//                            sobj.put("openId",j.getString("openId"));
//                            sobj.put("duration",result.getInt("expires_in"));
//                            WaferReturn w = new WaferReturn();
//                            w.setReturnCode(ReturnCodeUtil.MA_OK);
//                            if(r.getCode()==1){
//                                w.setReturnMessage("NEW_SESSION_SUCCESS");
//                            }else {
//                                w.setReturnMessage("UPDATE_SESSION_SUCCESS");
//                            }
//                            w.setReturnData(sobj.toString());
                            return returnResult.toString();
                        }else{
                            WaferReturn w = new WaferReturn();
                            w.setReturnCode(ReturnCodeUtil.MA_CHANGE_SESSION_ERR);
                            w.setReturnMessage("CHANGE_SESSION_ERR");
                            w.setReturnData("");
                            return w.toJsonString();
                        }
                    }else{
                        WaferReturn w = new WaferReturn();
                        w.setReturnCode(ReturnCodeUtil.MA_CRYPT_USERINFO_ERR);
                        w.setReturnMessage("CRYPT_USERINFO_ERR");
                        w.setReturnData("");
                        return w.toJsonString();
                    }
                }else{
                    WaferReturn w = new WaferReturn();
                    w.setReturnCode(ReturnCodeUtil.MA_DECRYPT_ERR);
                    w.setReturnMessage("DECRYPT_ERR");
                    w.setReturnData("");
                    return w.toJsonString();
                }
            }
        }

        JSONObject j =new JSONObject();
        j.put("code",jcode);
        j.put("xixi",1);
        return j.toString();
    }


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
    public JSONObject decrypt(String appid,String sessionKey,String  encryptedData ,String iv){
        byte[] sessionKeyBy = Base64Util.decode(sessionKey);
        byte[] encryptedDataBy = Base64Util.decode(encryptedData);
        byte[] ivBy = Base64Util.decode(iv);

        byte[] dec = Pkcs7Encoder.decryptOfDiyIV(encryptedDataBy, sessionKeyBy,ivBy);
        JSONObject j = null;
        try {
            j = new JSONObject(new String(dec,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return j;
    }
}
