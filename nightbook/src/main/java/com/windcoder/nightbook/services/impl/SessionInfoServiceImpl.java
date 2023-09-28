package com.windcoder.nightbook.services.impl;


import com.windcoder.common.utills.DateUtillZ;
import com.windcoder.common.utills.ReturnResult;
import com.windcoder.nightbook.entity.AppInfo;
import com.windcoder.nightbook.entity.SessionInfo;
import com.windcoder.nightbook.services.ISessionInfoService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * Created by wind on 2016/12/26.
 */
@Service("sessionInfoService")
public class SessionInfoServiceImpl implements ISessionInfoService {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public ReturnResult addSessionInfo(SessionInfo s){
        ReturnResult r = new ReturnResult();
        int bol = 0;
        try{
            bol = sqlSessionTemplate.insert("SessionInfo.addSessionInfo",s);
            if(bol>0){
                bol = 1;
                r.setMsg(s.getUuid());
            }else{
                bol = -1;
                r.setMsg("addSessionInfo error");
            }
        }catch (Exception e){
            bol = -1;
            r.setMsg("addSessionInfo error");
        }
        r.setCode(bol);

        return r;
    }
    public ReturnResult updateCsessioninfoTime(SessionInfo s){
        ReturnResult r = new ReturnResult();
        int bol = 0;
        try{
            bol = sqlSessionTemplate.update("SessionInfo.updateCsessioninfoTime",s);
            if(bol>0){
                bol = 3;
            }else{
                bol = -3;
            }
        }catch (Exception e){
            bol = -3;

        }
        r.setCode(bol);
        return r;
    }
    public ReturnResult updateSessionInfoByUid(SessionInfo s){
        ReturnResult r = new ReturnResult();
        int bol = 0;
        try{
            bol = sqlSessionTemplate.update("SessionInfo.updateSessionInfoByUid",s);
            if(bol>0){
                bol = 2;
                r.setMsg(s.getUuid());
            }else{
                bol = -2;
                r.setMsg("updateSessionInfoByUid error");
            }
        }catch (Exception e){
            bol = -2;
            r.setMsg("updateSessionInfoByUid error");
        }
        r.setCode(bol);
        return r;
    }

    public SessionInfo findSessionByPidAndOpenId(SessionInfo s){
        return sqlSessionTemplate.selectOne("SessionInfo.findSessionByPidAndOpenId",s);
    }
    public SessionInfo findSessionByUuidAndSkey(SessionInfo s){
        return sqlSessionTemplate.selectOne("SessionInfo.findSessionByUuidAndSkey",s);
    }

    public SessionInfo checkSessionForLogin(SessionInfo s){
        SessionInfo oldS = findSessionByPidAndOpenId(s);
        if(oldS!=null){
            s.setId(oldS.getId());
            s.setUuid(oldS.getUuid());
            return s;
        }
        return oldS;
    }

    public ReturnResult checkSessionForAuth(SessionInfo s, AppInfo a){
        SessionInfo oldS = findSessionByUuidAndSkey(s);
        ReturnResult r = new ReturnResult();
        if(oldS!=null){
            Timestamp now = DateUtillZ.getTimestampOfNow();
            double st = 86400.0;
            if((DateUtillZ.getDaySubOfTimestamp(oldS.getCreateTime(),now)/st)>a.getLoginDuration()){
                //过期
                r.setCode(-23);
                r.setMsg("创建时间超时");
                return r;
            }else if(DateUtillZ.getDaySubOfTimestamp(oldS.getLastVisitTime(),now)>a.getSessionDuration()){
                //过期
                r.setCode(-24);
                r.setMsg("session时间超时");
                return r;
            }else{
                s.setLastVisitTime(DateUtillZ.getTimestampOfNow());
                r = updateCsessioninfoTime(s);
                if(r.getCode()>0){
                    r.setCode(20);
                    r.setMsg(oldS.getUserInfo());
                    return r;
                }else{
                    r.setCode(-25);
                    r.setMsg("更新最近登陆失败");
                    return r;
                }
            }
        }
        r.setCode(-22);
        r.setMsg("用户session不存在");
        return r;
    }
    public ReturnResult changeCsessioninfo(SessionInfo s){
        ReturnResult r = new ReturnResult();
        SessionInfo oldS = checkSessionForLogin(s);
        if(oldS!=null){
            //更新
           r = updateSessionInfoByUid(oldS);
        }else{
            //添加
            r = addSessionInfo(s);
        }
        return r;
    }
}
