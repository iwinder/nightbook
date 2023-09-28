package com.windcoder.nightbook.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-26
 * Time: 0:18 上午
 */
@Data
public class Issues {
    private Integer id;
    private String uid;
    private String issues;
    private Timestamp createtime;
}
