package com.windcoder.nightbook.common.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-12-12
 * Time: 0:15 上午
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable implements Serializable {
    private static final long serialVersionUID = 1L;
    @LastModifiedDate
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    protected Date lastModifiedDate;

    @CreatedDate
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Column(updatable=false)
    protected Date createdDate;
}
