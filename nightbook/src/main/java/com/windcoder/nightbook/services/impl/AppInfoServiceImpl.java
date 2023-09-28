package com.windcoder.nightbook.services.impl;


import com.windcoder.common.utills.ReturnResult;
import com.windcoder.nightbook.entity.AppInfo;
import com.windcoder.nightbook.services.IAppInfoService;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wind on 2016/12/24.
 */
@Service("appInfoService")
public class AppInfoServiceImpl implements IAppInfoService {
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public ReturnResult addAppInfo(AppInfo appInfo){
        ReturnResult r= new ReturnResult();
        return r;
    }

    public AppInfo findAppInfoByPid(Integer pid){
        AppInfo appInfo = sqlSessionTemplate.selectOne("com.windcoder.nightbook.entity.AppInfo.findAppInfoByPid",pid);
        return appInfo;
    }
}
