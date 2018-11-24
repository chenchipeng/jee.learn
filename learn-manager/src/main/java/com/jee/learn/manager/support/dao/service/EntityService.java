package com.jee.learn.manager.support.dao.service;

import java.io.Serializable;
import java.util.List;

import com.jee.learn.manager.support.dao.EntityDao;
import com.jee.learn.manager.support.dao.Page;

/**
 * {@link EntityDao} 通用sevice接口
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月12日 下午3:37:47 ccp 新建
 */
public interface EntityService<T, ID extends Serializable> {

    /**
     * 获取{@link EntityDao}对象
     * 
     * @return
     */
    EntityDao getEntityDao();

    /**
     * 查询一条记录
     * 
     * @param id
     * @return
     */
    T findOne(ID id);

    /**
     * 查询一条记录
     * 
     * @param entity
     * @return
     */
    T findOne(T entity);

    /**
     * 查询一条记录
     * 
     * @param property
     * @param value
     * @return
     */
    T findOne(String property, Object value);

    /**
     * 列表查询
     * 
     * @param entity
     * @return
     */
    List<T> findList(T entity);

    /**
     * 列表查询
     * 
     * @param entity
     * @param orderBy
     * @return
     */
    List<T> findList(T entity, String orderBy);

    /**
     * 列表查询
     * 
     * @param property
     * @param value
     * @return
     */
    List<T> findList(String property, Object value);

    /**
     * 列表查询
     * 
     * @param property
     * @param value
     * @param orderBy
     * @return
     */
    List<T> findList(String property, Object value, String orderBy);

    /**
     * 分页查询
     * 
     * @param entity
     * @param page
     * @return
     */
    Page<T> findPage(T entity, Page<T> page);

    /**
     * 分页查询
     * 
     * @param entity
     * @param page
     * @param orderBy
     * @return
     */
    Page<T> findPage(T entity, Page<T> page, String orderBy);

    /**
     * 分页查询
     * 
     * @param entity
     * @param offset
     * @param limit
     * @return
     */
    Page<T> findPage(T entity, int offset, int limit);

    /**
     * 分页查询
     * 
     * @param entity
     * @param offset
     * @param limit
     * @param orderBy
     * @return
     */
    Page<T> findPage(T entity, int offset, int limit, String orderBy);

    /**
     * 新增或更新, 默认主键自增
     * 
     * @param entity
     */
    void saveOrUpdate(T entity);


    /**
     * 批量新增或更新, 默认主键自增
     * 
     * @param entities
     */
    void saveOrUpdate(Iterable<T> entities);


    /**
     * 逻辑删除
     * 
     * @param entity
     */
    void logicDelete(T entity);

    /**
     * 逻辑删除
     * 
     * @param entities
     */
    void logicDelete(Iterable<T> entities);

    /**
     * 物理删除
     * 
     * @param entity
     */
    void physicalDelete(T entity);

    /**
     * 物理删除
     * 
     * @param entities
     */
    void physicalDelete(Iterable<T> entities);

}
