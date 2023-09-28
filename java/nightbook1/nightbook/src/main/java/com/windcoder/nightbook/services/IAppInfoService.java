package com.windcoder.nightbook.services;

import com.windcoder.nightbook.entity.AppInfo;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-08
 * Time: 19:02 下午
 */
public interface IAppInfoService {
    /**
     * 根据pid(项目id)查询小程序信息
     * @param pid
     * @return
     */
    public AppInfo findAppInfoByPid(Integer pid);
}
