package com.jee.learn.interfaces.service;

import java.io.Serializable;

public interface BaseService<T> {

    T findOne(Serializable id);

    void save(T entity);

}
