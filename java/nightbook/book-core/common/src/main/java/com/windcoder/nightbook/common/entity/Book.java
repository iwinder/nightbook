package com.windcoder.nightbook.common.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-11-27
 * Time: 0:18 上午
 */
@Data
@Entity
@Table(name="sys_book")
public class Book extends Auditable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private Long id;

    private String title;//书名
    private String originTitle;//原书名
    private String author;//作者
    private String translator;//译者
    private String headImage;//封面图
    @Type(type="text")
    private String summary;//简介
    private String price;//定价
    private String isbn;//isbn号
    private String publisher;//出版社
    private String pubdate;//出版日期
    private String pages;//页数
    private String average;//豆瓣评分

}
