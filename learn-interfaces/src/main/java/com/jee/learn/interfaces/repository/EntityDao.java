package com.jee.learn.interfaces.repository;

import java.io.Serializable;

import javax.persistence.EntityManager;

/**
 * 
 * <p>
 * Title: IEntityDao<br/>
 * Description: 通用实体对象数据库操作接口<br/>
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
