package com.jee.learn.interfaces.support.jpa.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

/**
 * 
 * <p>
 * Title: IEntityDao
 * </p>
 * <p>
 * Description: 通用实体对象数据库操作接口
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013 ITDCL All right reserved.
 * </p>
 * <p>
 * Company: ITDCL
 * </p>
 * 
 * @author yjf
 * @version 1.0<br/>
 *          修改记录: 下面填写修改的内容以及修改的日期<br/>
 *          1.2013-9-6 下午5:23:04 yjf new
 */
public interface EntityDao {

    String PRIMARY_KEY_NAME = "id";
    String DEL_FLAG_NAME = "del_flag";
    int Y = 1;
    int N = 0;

    /**
     * 获取{@link EntityManager}对象
     * 
     * @return
     */
    EntityManager getEntityManager();

    /**
     * 根据指定类型，条件查询对象记录
     * 
     * @param entityClass
     * @param condition
     * @return
     */
    <T> List<T> find(Class<T> entityClass, Condition condition);

    /**
     * 根据指定类型，条件查询对象记录,支持排序
     * 
     * @param entityClass
     * @param condition
     * @param sort
     * @return
     */
    <T> List<T> find(Class<T> entityClass, Condition condition, Sort sort);

    /**
     * 根据指定类型，条件查询对象记录,支持分页
     * 
     * @param entityClass
     * @param condition
     * @param offset
     * @param limit
     * @return
     */
    <T> List<T> find(Class<T> entityClass, Condition condition, int offset, int limit);

    /**
     * 根据指定类型，条件查询对象记录,支持排序与分页
     * 
     * @param entityClass
     * @param condition
     * @param sort
     * @param offset
     * @param limit
     * @return
     */
    <T> List<T> find(Class<T> entityClass, Condition condition, Sort sort, int offset, int limit);

    /**
     * 根据类型，查询此类型所有记录
     * 
     * @param entityClass
     * @return
     */
    <T> List<T> findAll(Class<T> entityClass);

    /**
     * 根据类型，条件查询返回找到的第一条记录
     * 
     * @param entityClass
     * @param condition
     * @return
     */
    <T> T findOne(Class<T> entityClass, Condition condition);

    /**
     * 根据类型，条件查询返回property=value的第一条记录
     * 
     * @param entityClass
     * @param property
     * @param value
     * @return
     */
    <T> T findOne(Class<T> entityClass, String property, Object value);

    /**
     * 根据类型与主键值，查询对象记录
     * 
     * @param entityClass
     * @param id
     * @return
     */
    <T> T findOne(Class<T> entityClass, Serializable id);

    /**
     * 根据类型与主键值，查询对象记录
     * 
     * @param entityClass
     * @param id
     * @return
     */
    <T> T findOneForUpdate(Class<T> entityClass, Serializable id);

    /**
     * 保存实例对象记录
     * 
     * @param entity
     */
    <T> void save(T entity);

    /**
     * 保存迭代器中所有实例对象记录
     * 
     * @param entities
     */
    <T> void save(Iterable<T> entities);

    /**
     * 更新指定的实例对象记录
     * 
     * @param entity
     */
    <T> void update(T entity);

    /**
     * 根据类型与主键值，删除记录
     * 
     * @param entityClass
     * @param id
     */
    <T> void logicDelete(Class<T> entityClass, Serializable id);

    /**
     * 删除指定的对象记录
     * 
     * @param entity
     */
    <T> void logicDelete(T entity);

    /**
     * 删除迭代器中，所有对象记录
     * 
     * @param entities
     */
    <T> void logicDelete(Iterable<? extends T> entities);

    /**
     * 删除指定类型所有对象记录
     * 
     * @param entityClass
     */
    <T> void logicDeleteAll(Class<T> entityClass);

    /**
     * 根据类型与主键值，删除记录
     * 
     * @param entityClass
     * @param id
     */
    <T> void physicalDelete(Class<T> entityClass, Serializable id);

    /**
     * 删除指定的对象记录
     * 
     * @param entity
     */
    <T> void physicalDelete(T entity);

    /**
     * 删除迭代器中，所有对象记录
     * 
     * @param entities
     */
    <T> void physicalDelete(Iterable<? extends T> entities);

    /**
     * 删除指定类型所有对象记录
     * 
     * @param entityClass
     */
    <T> void physicalDeleteAll(Class<T> entityClass);

    /**
     * 统计指定类型的记录总数
     * 
     * @param entityClass
     * @return
     */
    <T> long count(Class<T> entityClass);

    /**
     * 统计指定类型，给定条件下的记录总数
     * 
     * @param entityClass
     * @param condition
     * @return
     */
    <T> long count(Class<T> entityClass, Condition condition);

    /**
     * 求和计算
     * 
     * @param entityClass
     * @param fieldName
     * @param condition
     * @return
     */
    <T> Number sum(Class<T> entityClass, String fieldName, Condition condition);

    /**
     * 强制缓存与数据库同步
     */
    void flush();
}
