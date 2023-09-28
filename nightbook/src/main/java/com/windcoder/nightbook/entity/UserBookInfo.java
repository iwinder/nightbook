package com.windcoder.nightbook.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-19
 * Time: 1:45 上午
 */
@Data
public class UserBookInfo {
    private Integer id;
    private String uuid;
    private Integer bid;
    private String isbn;
    private Integer readStatus;//1 在读 2 未读 3 已读
    private Integer deleteStatus;//删除状态 -1 删除 1正常
    private Timestamp createtime;


    private String title;//书名
    private String author;//作者
    private String headImage;//封面图
}
