package com.windcoder.nightbook.common.dto;

import lombok.Data;

@Data
public class UserBookDto {
    private Long userId;
    private Long bookId;
    private String title;//书名
    private String author;//作者
    private String headImage;//封面图
    private String isbn;
    private Integer isRead;//1 在读 2 未读 3 已读
}
