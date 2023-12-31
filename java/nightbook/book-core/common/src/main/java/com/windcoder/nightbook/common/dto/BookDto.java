package com.windcoder.nightbook.common.dto;

import lombok.Data;

@Data
public class BookDto {
    private Long id;
    private String title;//书名
    private String originTitle;//原书名
    private String author;//作者
    private String translator;//译者
    private String headImage;//封面图
    private String summary;//简介
    private String price;//定价
    private String isbn;//isbn号
    private String publisher;//出版社
    private String pubdate;//出版日期
    private String pages;//页数
    private String average;//豆瓣评分
    private Integer hasStatus;//用户从豆瓣查询到后，检测是否在数据库的用户表中， 1存在 0不存在
}
