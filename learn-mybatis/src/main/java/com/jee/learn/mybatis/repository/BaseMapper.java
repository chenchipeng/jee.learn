package com.jee.learn.mybatis.repository;

import java.io.Serializable;
import java.util.List;

/**
 * 通用mapper
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月20日 上午9:57:44 ccp 新建
 */
public interface BaseMapper<T, ID extends Serializable> {

    /**
     * 根据主键查询
     * 
     * @param id
     * @return
     */
    T get(ID id);

    /**
     * 列表查询
     * 
     * @param entity
     * @return
     */
    List<T> findList(T entity);

    /**
     * 新增
     * 
     * @param entity
     * @return
     */
    void insert(T entity);

    /**
     * 修改
     * 
     * @param entity
     * @return
     */
    void update(T entity);

    /**
     * 删除
     * 
     * @param id
     */
    void delete(ID id);

    /**
     * 统计记录总数
     * 
     * @param entity
     * @return
     */
    long count(T entity);
}
