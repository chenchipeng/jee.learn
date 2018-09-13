package com.jee.learn.jpa.support.dao.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.jpa.support.dao.Condition;
import com.jee.learn.jpa.support.dao.Condition.Operator;
import com.jee.learn.jpa.support.dao.EntityDao;
import com.jee.learn.jpa.support.dao.Page;
import com.jee.learn.jpa.support.dao.Sort;
import com.jee.learn.jpa.support.dao.Sort.Direction;
import com.jee.learn.jpa.util.Constants;
import com.jee.learn.jpa.util.reflect.ReflectUtils;

/**
 * {@link EntityDao} 通用sevice接口实现<br/>
 * 无法直接使用，必须通过具体的子类继承才行
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月12日 下午3:38:08 ccp 新建
 */
@Transactional(readOnly = true)
public abstract class EntityServiceImpl<T, ID extends Serializable> implements EntityService<T, ID> {

    @Autowired
    private EntityDao entityDao;

    @Override
    public EntityDao getEntityDao() {
        return this.entityDao;
    }

    @Override
    public T findOne(ID id) {
        return entityDao.findOne(getEntityType(), id);
    }

    @Override
    public T findOne(T entity) {
        return entityDao.findOne(getEntityType(), parseQueryParams(entity));
    }

    @Override
    public T findOne(String property, Object value) {
        return entityDao.findOne(getEntityType(), property, value);
    }

    @Override
    public List<T> findList(T entity) {
        return findList(entity, StringUtils.EMPTY);
    }

    @Override
    public List<T> findList(T entity, String orderBy) {
        return entityDao.find(getEntityType(), parseQueryParams(entity), parseSort(orderBy));
    }

    @Override
    public List<T> findList(String property, Object value) {
        return findList(property, value, StringUtils.EMPTY);
    }

    @Override
    public List<T> findList(String property, Object value, String orderBy) {
        Condition con = new Condition();
        con.add(property, Operator.EQ, value);
        return entityDao.find(getEntityType(), con, parseSort(orderBy));
    }

    @Override
    public Page<T> findPage(T entity, Page<T> page) {
        return findPage(entity, page.getPageSize() * page.getPageNum(), page.getPageSize(), StringUtils.EMPTY);
    }

    @Override
    public Page<T> findPage(T entity, Page<T> page, String orderBy) {
        return findPage(entity, page.getPageSize() * page.getPageNum(), page.getPageSize(), orderBy);
    }

    @Override
    public Page<T> findPage(T entity, int offset, int limit) {
        return findPage(entity, offset, limit, StringUtils.EMPTY);
    }

    @Override
    public Page<T> findPage(T entity, int offset, int limit, String orderBy) {

        Long count = entityDao.count(getEntityType());
        List<T> list = entityDao.find(getEntityType(), parseQueryParams(entity), parseSort(orderBy), offset, limit);

        Page<T> page = new Page<>();
        page.setRows(list);
        page.setTotal(count.intValue());
        page.setPageSize(limit);
        page.setOffset(offset);
        if (limit != 0) {
            page.setPageNum(offset / limit + 1);
        }
        return page;
    }

    @Override
    @Transactional(readOnly = false)
    public void saveOrUpdate(T entity) {

        Object primaryKey = ReflectUtils.invokeGetter(entity, Constants.PRIMARY_KEY_NAME);
        if (primaryKey == null || (primaryKey instanceof String && StringUtils.isBlank(String.valueOf(primaryKey)))) {
            // 新增
            entityDao.save(entity);
        } else {
            // 更新
            entityDao.update(entity);
        }

    }

    @Override
    @Transactional(readOnly = false)
    public void saveOrUpdate(Iterable<T> entities) {
        entities.forEach(entity -> saveOrUpdate(entity));
    }

    @Override
    @Transactional(readOnly = false)
    public void logicDelete(T entity) {
        entityDao.logicDelete(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public void logicDelete(Iterable<T> entities) {
        entities.forEach(entity -> logicDelete(entity));
    }

    @Override
    @Transactional(readOnly = false)
    public void physicalDelete(T entity) {
        entityDao.physicalDelete(entity);
    }

    @Override
    @Transactional(readOnly = false)
    public void physicalDelete(Iterable<T> entities) {
        entities.forEach(entity -> physicalDelete(entity));
    }

    /**
     * 勾子方法，由子类去解释查询参数，并转换成Condition对象
     * 
     * @param params
     * @return
     */
    protected Condition parseQueryParams(T entity) {
        return new Condition();
    }

    /**
     * 勾子方法，由子类去解释排序参数，并转换成Sort对象<br/>
     * 默认主键倒序
     * 
     * @param orderBy example: "properite1,properite2,... ASC"
     * @return
     */
    protected Sort parseSort(String orderBy) {
        Sort sort = new Sort(Direction.DESC, Constants.PRIMARY_KEY_NAME);

        if (StringUtils.isNotBlank(orderBy)) {
            // 用空格分隔
            String[] arrs = orderBy.split(" ");
            if (arrs.length == 2) {
                if (arrs[1].toUpperCase().equals("DESC")) {
                    sort = new Sort(Direction.DESC, arrs[0]);
                    return sort;
                }
                if (arrs[1].toUpperCase().equals("ASC")) {
                    sort = new Sort(Direction.ASC, arrs[0]);
                    return sort;
                }
            }
        }
        return sort;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getEntityType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
