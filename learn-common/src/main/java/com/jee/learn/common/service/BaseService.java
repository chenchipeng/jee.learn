package com.jee.learn.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.jee.learn.common.persist.Condition;

/**
 * 业务接口基类
 * 
 * @author yjf
 * @version 1.0
 *
 *          创建时间： 2018年2月7日 下午2:52:06
 */
public interface BaseService<T, ID extends Serializable> {

    /**
     * 按条件分页查询
     * 
     * @param params
     * @param pageable
     * @return
     */
    Page<T> load(Map<String, String> params, Pageable pageable);

    /**
     * 按条件分页查询
     * 
     * @param params
     * @param pageNo
     * @param pageSize
     * @param orderBy
     * @return
     */
    Page<T> load(Map<String, String> params, int pageNo, int pageSize, String orderBy);

    /**
     * 按条件查询，返回第一个值{@link Condition}
     * 
     * @param {@link
     *            Condition} to count instances for
     * @return
     */
    T findOne(Condition con);

    /**
     * 按条件查询，返回第一个值{@link Condition}
     * 
     * @param {@link
     *            Condition} to count instances for
     * @return
     */
    T findOne(String property, Object value);

    /**
     * 按条件查询，返回列表{@link Condition}
     * 
     * @param {@link
     *            Condition} to count instances for
     * @return
     */
    List<T> findAll(Condition con);

    /**
     * 按条件分页查询，返回列表{@link Condition}
     * 
     * @param {@link
     *            Condition} to count instances for
     * @param pageable
     * @return
     */
    Page<T> findAll(Condition con, Pageable pageable);

    /**
     * 按条件查询，返回列表{@link Condition}
     * 
     * @param @link
     *            Condition} to count instances for
     * @param sort
     * @return
     */
    List<T> findAll(Condition con, Sort sort);

    /**
     * 按条件查询，返回列表
     * 
     * @param params
     *            参数
     * @param orderBy
     *            排序字段(属性 DESC/ASC)
     * @return
     */
    List<T> findAll(Map<String, String> params, String orderBy);

    /**
     * 按条件查询，返回记录数{@link Condition}
     * 
     * @param con
     *            the {@link Condition} to count instances for
     * @return the number of instances
     */
    long count(Condition con);

    /**
     * 查询所有记录，排序
     * 
     * @param sort
     * @return all entities sorted by the given options
     */
    List<T> findAll(Sort sort);

    /**
     * 查询所有记录，分页排序
     * 
     * @param pageable
     * @return a page of entities
     */
    Page<T> findAll(Pageable pageable);

    /**
     * Saves a given entity. Use the returned instance for further operations as
     * the save operation might have changed the entity instance completely.
     * 
     * @param entity
     * @return the saved entity
     */
    <S extends T> S save(S entity);

    /**
     * Saves all given entities.
     * 
     * @param entities
     * @return the saved entities
     * @throws IllegalArgumentException
     *             in case the given entity is {@literal null}.
     */
    <S extends T> Iterable<S> save(Iterable<S> entities);

    /**
     * Retrieves an entity by its id.
     * 
     * @param id
     *            must not be {@literal null}.
     * @return the entity with the given id or {@literal null} if none found
     * @throws IllegalArgumentException
     *             if {@code id} is {@literal null}
     */
    T findOne(ID id);

    /**
     * Returns whether an entity with the given id exists.
     * 
     * @param id
     *            must not be {@literal null}.
     * @return true if an entity with the given id exists, {@literal false}
     *         otherwise
     * @throws IllegalArgumentException
     *             if {@code id} is {@literal null}
     */
    boolean exists(ID id);

    /**
     * Returns all instances of the type.
     * 
     * @return all entities
     */
    List<T> findAll();

    /**
     * Returns all instances of the type with the given IDs.
     * 
     * @param ids
     * @return
     */
    List<T> findAll(Iterable<ID> ids);

    /**
     * Returns the number of entities available.
     * 
     * @return the number of entities
     */
    long count();

    /**
     * Deletes the entity with the given id.
     * 
     * @param id
     *            must not be {@literal null}.
     * @throws IllegalArgumentException
     *             in case the given {@code id} is {@literal null}
     */
    void delete(ID id);

    /**
     * Deletes a given entity.
     * 
     * @param entity
     * @throws IllegalArgumentException
     *             in case the given entity is {@literal null}.
     */
    void delete(T entity);

    /**
     * Deletes the given entities.
     * 
     * @param entities
     * @throws IllegalArgumentException
     *             in case the given {@link Iterable} is {@literal null}.
     */
    void delete(Iterable<? extends T> entities);

    /**
     * Deletes all entities managed by the repository.
     */
    void deleteAll();

    Page<T> findAll(Specification<T> spec, Pageable pageable);
}
