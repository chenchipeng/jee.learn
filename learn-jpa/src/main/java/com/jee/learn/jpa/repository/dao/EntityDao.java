package com.jee.learn.jpa.repository.dao;

import java.io.Serializable;

import javax.persistence.EntityManager;

public interface EntityDao {

    /**
     * 获取entityManager
     * 
     * @return
     */
    public EntityManager getEntityManager();

    /**
     * 根据类型与主键值，查询对象记录
     * 
     * @param entityClass
     * @param id
     * @return
     */
    <T> T findOne(Class<T> entityClass, Serializable id);

    /**
     * 保存实例对象记录
     * 
     * @param entity
     */
    <T> void save(T entity);

}
