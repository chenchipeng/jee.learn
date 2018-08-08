package com.jee.learn.interfaces.support;

import java.io.Serializable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

/**
 * 
 * <p>
 * Title: EntityDaoImpl<br/>
 * Description: 通用实体对象数据库操作实现类，通过调用JPA实现,{@link ConditionHandler}<br/>
 * Copyright: Copyright (c) 2013 ITDCL All right reserved.<br/>
 * Company: ITDCL<br/>
 * </p>
 * 
 * @author yjf
 * @version 1.0
 *
 *          修改记录: 下面填写修改的内容以及修改的日期 <br/>
 *          1.2018-8-5 下午5:23:04 ccp new
 */

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityDaoImpl implements EntityDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public <T> void save(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public <T> T findOne(Class<T> entityClass, Serializable id) {
        return entityManager.find(entityClass, id);
    }

}
