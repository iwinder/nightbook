package com.windcoder.nightbook.common.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-12-14
 * Time: 23:50 下午
 */
@Data
@Entity
@IdClass(value=UserBookPK.class)
@Table(name="sys_user_book")
public class UserBook extends Auditable{
    private static final long serialVersionUID = 1L;


    @Id
    private Long userId;
    @Id
    private Long bookId;

    private String title;//书名
    private String author;//作者
    private String headImage;//封面图
    private String isbn;
    private Integer isRead;//1 在读 2 未读 3 已读
    @ColumnDefault("0")
    private boolean isDelete;//删除状态 0正常  1删除

    @Transient
    private Long countNum;
}
