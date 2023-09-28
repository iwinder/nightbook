package com.windcoder.nightbook.mina.entity;

import com.windcoder.nightbook.common.entity.Auditable;
import lombok.Data;

import javax.persistence.*;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-12-03
 * Time: 22:32 下午
 */
@Data
@Entity
@Table(name = "mina_session")
public class MinaSession extends Auditable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;	//会话 ID（自增长）

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "mina_id")
    private MinaInfo mina;

    private Long userId;//会话 uuid
    private String skey;//会话 Skey
    private String openId;//L	微信服务端返回的 `open_id` 值
    private String sessionKey;//	微信服务端返回的 `session_key` 值
    private String userInfo;	//已解密的用户数据
   	//createTime 会话创建时间，用于判断会话对应的 open_id 和 session_key 是否过期（是否超过 `cAppInfo` 表中字段 `login_duration` 配置的天数）
   	//lastVisitTime最近访问时间，用于判断会话是否过期（是否超过 `cAppInfo` 表中字段 `session_duration` 的配置的秒数）
}
