package com.windcoder.nightbook.services;


import com.windcoder.common.utills.ReturnResult;
import com.windcoder.nightbook.entity.AppInfo;
import com.windcoder.nightbook.entity.SessionInfo;

/**
 * Created by wind on 2016/12/26.
 */
public interface ISessionInfoService {
    public ReturnResult changeCsessioninfo(SessionInfo s);

    public ReturnResult checkSessionForAuth(SessionInfo s, AppInfo a);
}
