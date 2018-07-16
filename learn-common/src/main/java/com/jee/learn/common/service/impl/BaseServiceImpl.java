package com.jee.learn.common.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.common.persist.Condition;
import com.jee.learn.common.persist.Condition.Operator;
import com.jee.learn.common.persist.jpa.CustomJpaRepository;
import com.jee.learn.common.persist.SpecificationFactory;
import com.jee.learn.common.service.BaseService;
import com.jee.learn.common.util.Constant;

@Service
@Transactional(readOnly = true)
public abstract class BaseServiceImpl<T, ID extends Serializable> implements BaseService<T, ID> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected SpecificationFactory<T> factory;
    @Autowired
    protected CustomJpaRepository<T, ID> customJpaRepository;

    @Override
    public Page<T> load(Map<String, String> params, Pageable pageable) {
        Condition con = parseQueryParams(params);
        return findAll(con, pageable);
    }

    @Override
    public Page<T> load(Map<String, String> params, int pageNo, int pageSize, String orderBy) {
        PageRequest pageable = new PageRequest(pageNo > 0 ? pageNo - 1 : 0, pageSize, parseSort(orderBy));
        return load(params, pageable);
    }

    /**
     * 勾子方法，由子类实现封装查询条件对象
     * 
     * @param params
     * @return
     */
    protected Condition parseQueryParams(Map<String, String> params) {
        return new Condition();
    }

    /**
     * 勾子方法，由子类实现封装查询条件对象
     * 
     * @param entity
     * @return
     */
    protected Condition parseQueryParams(T entity) {
        return new Condition();
    }

    @Override
    public T findOne(Condition con) {
        Specification<T> spec = factory.createSpecifications(con);
        return customJpaRepository.findOne(spec);
    }

    @Override
    public T findOne(String property, Object value) {
        Condition con = new Condition();
        con.add(property, Operator.EQ, value);
        Specification<T> spec = factory.createSpecifications(con);
        return customJpaRepository.findOne(spec);
    }

    @Override
    public List<T> findAll(Condition con) {
        Specification<T> spec = factory.createSpecifications(con);
        return customJpaRepository.findAll(spec);
    }

    @Override
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return customJpaRepository.findAll(spec, pageable);
    }

    @Override
    public Page<T> findAll(Condition con, Pageable pageable) {
        Specification<T> spec = factory.createSpecifications(con);
        return customJpaRepository.findAll(spec, pageable);
    }

    @Override
    public List<T> findAll(Condition con, Sort sort) {
        Specification<T> spec = factory.createSpecifications(con);
        return customJpaRepository.findAll(spec, sort);
    }

    @Override
    public List<T> findAll(Map<String, String> params, String orderBy) {
        Condition con = parseQueryParams(params);
        Sort sort = parseSort(orderBy);
        return findAll(con, sort);
    }

    @Override
    public long count(Condition con) {
        Specification<T> spec = factory.createSpecifications(con);
        return customJpaRepository.count(spec);
    }

    @Override
    public List<T> findAll(Sort sort) {
        return customJpaRepository.findAll(sort);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return customJpaRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public <S extends T> S save(S entity) {
        return customJpaRepository.save(entity);
    }

    @Transactional
    @Override
    public <S extends T> Iterable<S> save(Iterable<S> entities) {
        return customJpaRepository.save(entities);
    }

    @Override
    public T findOne(ID id) {
        return customJpaRepository.findOne(id);
    }

    @Override
    public boolean exists(ID id) {
        return customJpaRepository.exists(id);
    }

    @Override
    public List<T> findAll() {
        return customJpaRepository.findAll();
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        return customJpaRepository.findAll(ids);
    }

    @Override
    public long count() {
        return customJpaRepository.count();
    }

    @Transactional
    @Override
    public void delete(ID id) {
        customJpaRepository.delete(id);
    }

    @Transactional
    @Override
    public void delete(T entity) {
        customJpaRepository.delete(entity);
    }

    @Transactional
    @Override
    public void delete(Iterable<? extends T> entities) {
        customJpaRepository.delete(entities);
    }

    @Transactional
    @Override
    public void deleteAll() {
        customJpaRepository.deleteAll();
    }

    protected Sort parseSort(String orderBy) {
        return parseSort(orderBy, Constant.UPDATE_DATE_NAME);
    }

    protected Sort parseSort(String orderBy, String propertyName) {
        Sort sort = new Sort(Direction.DESC, propertyName);

        if (StringUtils.isNotBlank(orderBy)) {
            String[] arrs = orderBy.split(" ");// 用空格分隔
            if (arrs.length == 2) {
                if (arrs[1].toUpperCase().equals("DESC")) {
                    sort = new Sort(Direction.DESC, arrs[0]);
                } else {
                    sort = new Sort(arrs[0]);
                }
            }
        }
        return sort;
    }

}
