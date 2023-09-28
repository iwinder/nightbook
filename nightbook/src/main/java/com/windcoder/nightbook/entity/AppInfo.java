package com.windcoder.nightbook.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Created by wind on 2016/12/23.
 */
@Data
public class AppInfo {
    private String appid;//申请微信小程序开发者时，微信分配的 appId
    private String secret;//申请微信小程序开发者时，微信分配的 appSecret
    private Integer pid;//程序id
    private long loginDuration;//登录过期时间，单位为天，默认 30 天
    private long sessionDuration;//会话过期时间，单位为秒，默认为 2592000 秒(即30天)
    private Timestamp createtime;

}
