package com.jee.learn.manager.support.spec.service;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.id.IdentifierGenerationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.manager.support.spec.Filter;
import com.jee.learn.manager.support.spec.QueryParams;
import com.jee.learn.manager.support.spec.repository.SpecRepository;
import com.jee.learn.manager.util.Constants;
import com.jee.learn.manager.util.reflect.ReflectUtils;

/**
 * {@link Specification} 通用实体对象数据库操作接口实现
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月10日 上午10:47:25 ccp 新建
 */
@Service
@Transactional(readOnly = true)
public class SpecSerciceImpl<T, ID extends Serializable> implements SpecSercice<T, ID> {

    @Autowired
    protected SpecRepository<T, ID> specRepository;

    /**
     * 勾子方法，由子类去解释查询参数，并转换成{@link QueryParams}对象
     * 
     * @param entity
     * @return
     */
    protected QueryParams<T> analyzeQueryParams(T entity) {
        return new QueryParams<T>();
    }

    /**
     * 勾子方法，由子类去解释排序参数，并转换成{@link Sort}对象
     * 
     * @param direction Direction.ASC 或 Direction.DESC
     * @param properties 需要排序的属性
     * @return
     */
    protected Sort analyzeSort(Direction direction, String... properties) {
        return Sort.by(direction, properties);
    }

    @Override
    public SpecRepository<T, ID> getSpecRepository() {
        return this.specRepository;
    }

    @Override
    public T findOne(ID id) {
        return specRepository.findById(id).orElseGet(() -> {
            return null;
        });
    }

    @Override
    public T findOne(QueryParams<T> spec) {
        return specRepository.findOne(spec).orElseGet(() -> {
            return null;
        });
    }

    @Override
    public T findOne(T entity) {
        return findOne(analyzeQueryParams(entity));
    }

    @Override
    public T findOne(String property, Object value) {
        return findOne(new QueryParams<T>().and(Filter.eq(property, value)));
    }

    @Override
    public List<T> findList(QueryParams<T> spec) {
        return specRepository.findAll(spec);
    }

    @Override
    public List<T> findList(QueryParams<T> spec, Sort sort) {
        return specRepository.findAll(spec, sort);
    }

    @Override
    public List<T> findList(T entity) {
        return findList(analyzeQueryParams(entity));
    }

    @Override
    public List<T> findList(T entity, Sort sort) {
        return findList(analyzeQueryParams(entity), sort);
    }

    @Override
    public List<T> findList(String property, Object value) {
        return findList(new QueryParams<T>().and(Filter.eq(property, value)));
    }

    @Override
    public List<T> findList(String property, Object value, Sort sort) {
        return findList(new QueryParams<T>().and(Filter.eq(property, value)), sort);
    }

    @Override
    public Page<T> findPage(QueryParams<T> spec, Pageable pageable) {
        return specRepository.findAll(spec, pageable);
    }

    @Override
    public Page<T> findPage(QueryParams<T> spec, int page, int size) {
        return findPage(spec, PageRequest.of(page, size));

    }

    @Override
    public Page<T> findPage(QueryParams<T> spec, int page, int size, Sort sort) {
        return findPage(spec, PageRequest.of(page, size, sort));
    }

    @Override
    public Page<T> findPage(T entity, Pageable pageable) {
        return findPage(analyzeQueryParams(entity), pageable);
    }

    @Override
    public Page<T> findPage(T entity, int page, int size) {
        return findPage(analyzeQueryParams(entity), PageRequest.of(page, size));
    }

    @Override
    public Page<T> findPage(T entity, int page, int size, Sort sort) {
        return findPage(analyzeQueryParams(entity), PageRequest.of(page, size, sort));
    }

    @Override
    @Transactional(readOnly = false)
    public void save(T entity) {
        specRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(Iterable<T> entities) {
        specRepository.saveAll(entities);
    }

    @Override
    @Transactional(readOnly = false)
    public T saveAndFlush(T entity) {
        return specRepository.saveAndFlush(entity);
    }

    @Override
    public void flush() {
        specRepository.flush();
    }

    @Override
    @Transactional(readOnly = false)
    public void physicsDelete(T entity) {
        specRepository.delete(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public void physicsDelete(Iterable<T> entities) {
        specRepository.deleteAll(entities);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false)
    public void logicDelete(T entity) {
        entity = this.findOne((ID) primaryKeyValidation(entity));
        ReflectUtils.invokeSetter(entity, Constants.DEL_FLAG_NAME, Constants.YES_NO_1);
        this.save(entity);
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = false)
    public void logicDelete(Iterable<T> entities) {
        for (T entity : entities) {
            entity = this.findOne((ID) primaryKeyValidation(entity));
            ReflectUtils.invokeSetter(entity, Constants.DEL_FLAG_NAME, Constants.YES_NO_1);
        }
        this.save(entities);
    }

    @Override
    public boolean existsById(ID id) {
        return id == null ? false : specRepository.existsById(id);
    }

    /**
     * primary key validation, 实体主键检查
     * 
     * @param entity
     * @return primaryKey
     * @throws IdentifierGenerationException when the entity does not have
     *             primary key
     */
    private Object primaryKeyValidation(T entity) {
        Object primaryKey = ReflectUtils.invokeGetter(entity, Constants.PRIMARY_KEY_NAME);
        if (primaryKey == null || (primaryKey instanceof String && StringUtils.isBlank(String.valueOf(primaryKey)))) {
            throw new IdentifierGenerationException(
                    "ids for this class must be manually assigned before calling primaryKeyValidation()");
        }
        return primaryKey;
    }

}
