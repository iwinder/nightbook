package com.windcoder.nightbook.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Description:
 * User: WindCoder
 * Date: 2017-12-16
 * Time: 13:07 下午
 */
@NoRepositoryBean
public interface SupportRepository <T, ID extends Serializable>
        extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}
