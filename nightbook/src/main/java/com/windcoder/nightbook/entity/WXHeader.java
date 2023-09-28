package com.windcoder.nightbook.entity;

import lombok.Data;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-10-15
 * Time: 23:18 下午
 */
@Data
public class WXHeader {
    private String id;
    private Integer pid;
    private String jcode;
    private String encryptData;
    private String iv;
}
