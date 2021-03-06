package com.jee.learn.interfaces.support.jpa.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.jee.learn.interfaces.util.reflect.ReflectUtils;

/**
 * 
 * <p>
 * Title: EntityDaoImpl
 * </p>
 * <p>
 * Description: 通用实体对象数据库操作实现类，通过调用JPA实现,{@link ConditionHandler}
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013 ITDCL All right reserved.
 * </p>
 * <p>
 * Company: ITDCL
 * </p>
 * 
 * @author yjf
 * @version 1.0<br/>
 *          修改记录: 下面填写修改的内容以及修改的日期<br/>
 *          1.2013-9-6 下午5:27:48 yjf new
 *          2.2019年3月1日 下午5:40:07 ccp 增加批量更新
 */

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityDaoImpl implements EntityDao {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public <T> List<T> find(Class<T> entityClass, Condition condition) {
        return find(entityClass, condition, null);
    }

    @Override
    public <T> List<T> find(Class<T> entityClass, Condition condition, Sort sort) {
        TypedQuery<T> query = createQuery(entityClass, condition, sort);

        return query.getResultList();
    }

    @Override
    public <T> List<T> find(Class<T> entityClass, Condition condition, int offset, int limit) {
        return find(entityClass, condition, null, offset, limit);
    }

    @Override
    public <T> List<T> find(Class<T> entityClass, Condition condition, Sort sort, int offset, int limit) {
        TypedQuery<T> query = createQuery(entityClass, condition, sort);
        query.setFirstResult(offset);
        query.setMaxResults(limit);

        return query.getResultList();
    }

    @Override
    public <T> List<T> findAll(Class<T> entityClass) {
        return find(entityClass, null);
    }

    @Override
    public <T> T findOne(Class<T> entityClass, Condition condition) {
        List<T> result = find(entityClass, condition);
        if (result != null && !result.isEmpty()) {
            if (result.size() == 1) {
                return result.get(0);
            }
            throw new NonUniqueResultException("query did not return a unique result: " + result.size());
        }
        return null;
    }

    @Override
    public <T> T findOne(Class<T> entityClass, String property, Object value) {
        return this.findOne(entityClass, new Condition(property, Condition.Operator.EQ, value));
    }

    @Override
    public <T> T findOne(Class<T> entityClass, Serializable id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    public <T> T findOneForUpdate(Class<T> entityClass, Serializable id) {
        return entityManager.find(entityClass, id, LockModeType.PESSIMISTIC_WRITE);
    }

    @Override
    public <T> void save(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public <T> void save(Iterable<T> entities) {
        for (T entity : entities) {
            save(entity);
        }
    }

    @Override
    public <T> T update(T entity) {
        return entityManager.merge(entity);
    }
    
    @Override
    public <T> Iterable<T> update(Iterable<T> entities) {
        for (T entity : entities) {
            entity = update(entity);
        }
        return entities;
    }

    @Override
    public <T> void logicDelete(Class<T> entityClass, Serializable id) {
        T obj = findOne(entityClass, id);
        if (obj != null) {
            logicDelete(obj);
        }
    }

    @Override
    public <T> void logicDelete(T entity) {
        String id = (String) ReflectUtils.invokeGetter(entity, PRIMARY_KEY_NAME);
        Object obj = findOne(entity.getClass(), id);
        ReflectUtils.invokeSetter(obj, DEL_FLAG_NAME, Y);
        update(obj);
    }

    @Override
    public <T> void logicDelete(Iterable<? extends T> entities) {
        for (T entity : entities) {
            logicDelete(entity);
        }
    }

    @Override
    public <T> void logicDeleteAll(Class<T> entityClass) {
        logicDelete(findAll(entityClass));
    }

    @Override
    public <T> void physicalDelete(Class<T> entityClass, Serializable id) {
        T obj = findOne(entityClass, id);
        if (obj != null) {
            physicalDelete(obj);
        }
    }

    @Override
    public <T> void physicalDelete(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public <T> void physicalDelete(Iterable<? extends T> entities) {
        for (T entity : entities) {
            physicalDelete(entity);
        }
    }

    @Override
    public <T> void physicalDeleteAll(Class<T> entityClass) {
        physicalDelete(findAll(entityClass));
    }

    @Override
    public <T> long count(Class<T> entityClass) {
        return count(entityClass, null);
    }

    @Override
    public <T> long count(Class<T> entityClass, Condition condition) {
        TypedQuery<T> query = createQuery(entityClass, condition, null, new ConditionHandler<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public CriteriaQuery<T> handle(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder cb, Condition con,
                    Sort sort) {
                if (con != null) {
                    query = query.where(parseCondition(root, cb, con));
                }
                return query.select((Selection<? extends T>) cb.count(root));
            }
        });

        return (Long) query.getSingleResult();
    }

    @Override
    public <T> Number sum(Class<T> entityClass, String fieldName, Condition condition) {
        TypedQuery<T> query = createQuery(entityClass, condition, null, new ConditionHandler<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public CriteriaQuery<T> handle(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder cb, Condition con,
                    Sort sort) {
                if (con != null) {
                    query = query.where(parseCondition(root, cb, con));
                }
                return query.select((Selection<? extends T>) cb.sum((Expression<Number>) getPath(root, fieldName)));
            }
        });

        return (Number) query.getSingleResult();
    }

    @Override
    public void flush() {
        entityManager.flush();
    }

    /**
     * 通过entityManager创建查询对象
     * 
     * @param entityClass
     * @param con
     * @param sort
     * @param handler {@link ConditionHandler<T>} 的一个子类，可由new ConditionHandler<T>(){}匿名创建
     * @return
     */
    private <T> TypedQuery<T> createQuery(Class<T> entityClass, Condition con, Sort sort, ConditionHandler<T> handler) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);

        return entityManager.createQuery(handler.handle(root, query, cb, con, sort));
    }

    private <T> TypedQuery<T> createQuery(Class<T> entityClass, Condition con, Sort sort) {

        /*
         * ConditionHandler<T>是抽象类，这里并没有将其实例化。一般情况下abstract类是不能够实例化的， 但可以通过abstract父类的引用来指向子类的实例实现实例化，如AbstractClazz ac = new
         * NormalClazz()，其中AbstractClazz是NormalClazz的父类。
         * 
         * 这里new关键字实例化的是一个继承了ConditionHandler<T>的匿名对象
         */

        TypedQuery<T> query = createQuery(entityClass, con, sort, new ConditionHandler<T>() {
            @Override
            public CriteriaQuery<T> handle(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder cb, Condition con,
                    Sort sort) {
                if (con != null) {
                    query = query.where(parseCondition(root, cb, con));
                }
                if (sort != null) {
                    query = query.orderBy(parseSort(root, cb, sort));
                }
                if (con != null && con.getGroupby() != null && !con.getGroupby().isEmpty()) {
                    query = query.groupBy(root.get(con.getGroupby()));
                }
                return query;
            }
        });
        return query;
    }

}
