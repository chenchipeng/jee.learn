package com.jee.learn.jpa.support.spec.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.jee.learn.jpa.support.spec.QueryParams;
import com.jee.learn.jpa.support.spec.repository.SpecRepository;

/**
 * {@link Specification} 通用实体对象数据库操作接口
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月10日 上午10:44:33 ccp 新建
 */
public interface SpecSercice<T, ID extends Serializable> {

    /**
     * 获取{@link SpecRepository}对象
     * 
     * @return
     */
    SpecRepository<T, ID> getSpecRepository();

    /**
     * 根据id查询一条记录
     * 
     * @param id
     * @return
     */
    T findOne(ID id);

    /**
     * 自定义组合条件查询一条记录
     * 
     * @param spec
     * @return
     */
    T findOne(QueryParams<T> spec);

    /**
     * 自定义组合条件查询一条记录
     * 
     * @param entity
     * @return
     */
    T findOne(T entity);

    /**
     * 自定义组合条件查询一条记录
     * 
     * @param property
     * @param value
     * @return
     */
    T findOne(String property, Object value);

    /**
     * 自定义组合条件查询
     * 
     * @param spec
     * @return
     */
    List<T> findList(QueryParams<T> spec);

    /**
     * 自定义组合条件查询并排序
     * 
     * @param spec
     * @param sort
     * @return
     */
    List<T> findList(QueryParams<T> spec, Sort sort);

    /**
     * 自定义组合条件查询
     * 
     * @param entity
     * @return
     */
    List<T> findList(T entity);

    /**
     * 自定义组合条件查询并排序
     * 
     * @param entity
     * @param sort
     * @return
     */
    List<T> findList(T entity, Sort sort);

    /**
     * 根据单个属性查询
     * 
     * @param property
     * @param value
     * @return
     */
    List<T> findList(String property, Object value);

    /**
     * 根据单个属性查询并排序
     * 
     * @param property
     * @param value
     * @param sort
     * @return
     */
    List<T> findList(String property, Object value, Sort sort);

    /**
     * 自定义组合条件分页查询
     * 
     * @param spec
     * @param pageable
     * @return
     */
    Page<T> findPage(QueryParams<T> spec, Pageable pageable);

    /**
     * 自定义组合条件分页查询
     * 
     * @param spec
     * @param page
     * @param size
     * @return
     */
    Page<T> findPage(QueryParams<T> spec, int page, int size);

    /**
     * 自定义组合条件分页查询并排序
     * 
     * @param spec
     * @param page
     * @param size
     * @param sort
     * @return
     */
    Page<T> findPage(QueryParams<T> spec, int page, int size, Sort sort);

    /**
     * 自定义组合条件分页查询
     * 
     * @param entity
     * @param pageable 可使用PageRequest.of()创建
     * @return
     */
    Page<T> findPage(T entity, Pageable pageable);

    /**
     * 自定义组合条件分页查询
     * 
     * @param entity
     * @param page
     * @param size
     * @return
     */
    Page<T> findPage(T entity, int page, int size);

    /**
     * 自定义组合条件分页查询并排序
     * 
     * @param entity
     * @param page
     * @param size
     * @param sort
     * @return
     */
    Page<T> findPage(T entity, int page, int size, Sort sort);

    /**
     * 新增或保存
     * 
     * @param entity
     * @return
     */
    void save(T entity);

    /**
     * 批量新增或保存
     * 
     * @param entities
     * @return
     */
    void save(Iterable<T> entities);

    /**
     * 新增或保存后立即强制缓存与数据库同步
     * 
     * @param entity
     * @return
     */
    T saveAndFlush(T entity);

    /**
     * 强制缓存与数据库同步
     */
    void flush();

    /**
     * 物理删除
     * 
     * @param entity
     */
    void physicsDelete(T entity);

    /**
     * 批量物理删除
     * 
     * @param entities
     */
    void physicsDelete(Iterable<T> entities);

    /**
     * 逻辑删除
     * 
     * @param entity
     */
    void logicDelete(T entity);

    /**
     * 批量逻辑删除
     * 
     * @param entities
     */
    void logicDelete(Iterable<T> entities);

    /**
     * 根据主键判断记录是否存在
     * 
     * @param id
     * @return
     */
    boolean existsById(ID id);

}
