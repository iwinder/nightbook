package com.windcoder.nightbook.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-12-14
 * Time: 23:51 下午
 */
@Data
public class UserBookPK implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long userId;
    private Long bookId;
}
