package com.jee.learn.interfaces.support;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseServiceImpl<T> implements BaseService<T> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntityDao entityDao;

    /** 获取泛型类型 */
    @SuppressWarnings("unchecked")
    private Class<T> getGenericType() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) type.getActualTypeArguments()[0];
    }

    @Override
    public T findOne(Serializable id) {
        return entityDao.findOne(getGenericType(), id);
    }

    @Transactional(readOnly = false)
    @Override
    public void save(T entity) {
        entityDao.save(entity);
    }

}
