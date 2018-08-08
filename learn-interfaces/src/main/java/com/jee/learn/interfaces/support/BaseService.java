package com.jee.learn.interfaces.support;

import java.io.Serializable;

public interface BaseService<T> {

    T findOne(Serializable id);

    void save(T entity);

}
