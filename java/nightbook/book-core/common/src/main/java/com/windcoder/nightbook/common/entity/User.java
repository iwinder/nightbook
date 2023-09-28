package com.windcoder.nightbook.common.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-11-27
 * Time: 0:17 上午
 */
@Data
@Entity
@Table(name="sys_user")
public class User extends Auditable{
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;
    private String uuid;
    private String username;
    private String password;
    private String salt;
    private String avatar;
    private String displayName;
    private String nickname;
    private String urlname;

}
