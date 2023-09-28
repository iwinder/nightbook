package com.windcoder.nightbook.mina.entity;



import com.windcoder.nightbook.common.entity.Auditable;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 小程序实体类
 * Created by wind on 2016/12/23.
 */
@Data
@Entity
@Table(name = "mina_info")
public class MinaInfo extends Auditable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    private String appid;//申请微信小程序开发者时，微信分配的 appId
    private String secret;//申请微信小程序开发者时，微信分配的 appSecret
    private long loginDuration;//登录过期时间，单位为天，默认 30 天
    private long sessionDuration;//会话过期时间，单位为秒，默认为 2592000 秒(即30天)

}
