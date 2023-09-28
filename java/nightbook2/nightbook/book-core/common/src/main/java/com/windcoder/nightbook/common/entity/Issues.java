package com.windcoder.nightbook.common.entity;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@Entity
@Table(name="sys_issues")
public class Issues extends Auditable {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private User user;
    @Type(type="text")
    private String issues;
}
