package com.windcoder.nightbook.entity;

import lombok.Data;

import java.sql.Timestamp;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-17
 * Time: 20:20 下午
 */
@Data
public class UserBook {
    private Integer id;
    private String uuid;
    private Integer bid;
    private String isbn;
    private Integer readStatus;//1 在读 2 未读 3 已读 -1 所有
    private Integer deleteStatus;//删除状态 -1 删除 1正常
    private Timestamp updatetime;//更新时间
    private Timestamp createtime;//创建时间
}
