package com.chnskin.persist.dao.impl;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Selection;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.chnskin.common.utils.Reflections;
import com.chnskin.persist.dao.Condition;
import com.chnskin.persist.dao.IEntityDao;
import com.chnskin.persist.dao.Sort;
import com.chnskin.utils.Constant;

/**
 * 
 * <p>Title: EntityDaoImpl</p>
 * <p>Description: 通用实体对象数据库操作实现类，通过调用JPA实现,{@link ConditionHandler}</p>
 * <p>Copyright: Copyright (c) 2013 ITDCL  All right reserved.</p>
 * <p>Company: ITDCL</p>
 * @author yjf
 * @version 1.0
 *
 * 修改记录:
 * 下面填写修改的内容以及修改的日期
 * 1.2013-9-6 下午5:27:48  yjf    new
 */

@Repository
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EntityDaoImpl implements IEntityDao {

    @PersistenceContext
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
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
    public <T> void update(T entity) {
        entity = entityManager.merge(entity);
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
    public <T> List<T> findAll(Class<T> entityClass) {
        return find(entityClass, null);
    }

    @Override
    public <T> void delete(Class<T> entityClass, Serializable id) {
        T obj = findOne(entityClass, id);
        if (obj != null) {
            delete(obj);
        }
    }

    @Override
    public <T> void delete(T entity) {
        String id = (String) Reflections.invokeGetter(entity, Constant.ID_NAME);
        Object obj = findOne(entity.getClass(), id);
        Reflections.invokeSetter(obj, Constant.DEL_FLEG_NAME, Constant.YES_NO_1);
        update(obj);
    }

    @Override
    public <T> void delete(Iterable<? extends T> entities) {
        for (T entity : entities) {
            delete(entity);
        }
    }

    @Override
    public <T> void deleteAll(Class<T> entityClass) {
        delete(findAll(entityClass));
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
            public CriteriaQuery<T> handle(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder cb, Condition con, Sort sort) {
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
            public CriteriaQuery<T> handle(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder cb, Condition con, Sort sort) {
                if (con != null) {
                    query = query.where(parseCondition(root, cb, con));
                }
                return query.select((Selection<? extends T>) cb.sum((Expression<Number>) getPath(root, fieldName)));
            }
        });

        return (Number) query.getSingleResult();
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
    public <T> T findOne(Class<T> entityClass, Condition condition) {
        List<T> result = find(entityClass, condition);

        return result != null && !result.isEmpty() ? result.get(0) : null;
    }

    @Override
    public <T> T findOne(Class<T> entityClass, String property, Object value) {
        return this.findOne(entityClass, new Condition(property, Condition.Operator.EQ, value));
    }

    private <T> TypedQuery<T> createQuery(Class<T> entityClass, Condition con, Sort sort, ConditionHandler<T> handler) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);

        return entityManager.createQuery(handler.handle(root, query, cb, con, sort));
    }

    private <T> TypedQuery<T> createQuery(Class<T> entityClass, Condition con, Sort sort) {
        TypedQuery<T> query = createQuery(entityClass, con, sort, new ConditionHandler<T>() {
            @Override
            public CriteriaQuery<T> handle(Root<T> root, CriteriaQuery<T> query, CriteriaBuilder cb, Condition con, Sort sort) {
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

    @Override
    public void flush() {
        entityManager.flush();
    }

}
