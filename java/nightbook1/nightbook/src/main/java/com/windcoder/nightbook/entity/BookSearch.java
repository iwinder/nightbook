package com.windcoder.nightbook.entity;

import lombok.Data;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-22
 * Time: 23:32 下午
 */
@Data
public class BookSearch {
    private String keys;
    private Integer start;
    private Integer count;
    private Integer total;
    private String uuid;
}
