package com.jee.learn.manager.support.spec.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Component;

/**
 * {@link JpaSpecificationExecutor}
 * 的简单通用Repository。在具体的实体Repository中继承该SpecRepository,
 * 即可使用封装过的JpaSpecificationExecutor
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月10日 上午11:52:49 ccp 新建
 */
@NoRepositoryBean
@Component
public interface SpecRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

}