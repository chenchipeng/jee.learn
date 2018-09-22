package com.jee.learn.mybatis.service;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jee.learn.mybatis.domain.api.ApiUser;
import com.jee.learn.mybatis.repository.BaseMapper;
import com.jee.learn.mybatis.support.PageDto;
import com.jee.learn.mybatis.support.PrimaryKey;

@Transactional(readOnly = true)
public abstract class BaseService<T, ID extends Serializable, M extends BaseMapper<T, ID>> {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected M mapper;

    /**
     * 根据主键获取一条记录
     * 
     * @param id
     * @return
     */
    public T get(ID id) {
        return mapper.get(id);
    }

    /**
     * 查询一条记录
     * 
     * @param entity
     * @return
     */
    public T findOne(T entity) {
        List<T> list = findList(entity);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (list.size() != 1) {
            throw new RuntimeException("the result more than one!");
        }
        return list.get(0);
    }

    /**
     * 列表查询
     * 
     * @param entity
     * @return
     */
    public List<T> findList(T entity) {
        return mapper.findList(entity);
    }

    /**
     * 分页查询
     * 
     * @param pageDto
     * @param entity
     * @return
     */
    public PageDto<T> findPage(PageDto<T> pageDto, T entity) {
        if (pageDto == null) {
            return null;
        }
        try {
            Page<ApiUser> page = PageHelper.startPage(pageDto.getPageNum(), pageDto.getPageSize())
                    .setOrderBy(pageDto.getOrderBy());
            List<T> list = mapper.findList(entity);
            pageDto.setTotal(page.getTotal());
            pageDto.setContent(list);
        } finally {
            // 手动(强制)清理 ThreadLocal 存储的分页参数
            PageHelper.clearPage();
        }
        return pageDto;
    }

    /**
     * 分页查询
     * 
     * @param pageNum
     * @param pageSize
     * @param entity
     * @return
     */
    public PageDto<T> findPage(int pageNum, int pageSize, String orderBy, T entity) {
        PageDto<T> pageDto = new PageDto<>();
        pageDto.setPageNum(pageNum);
        pageDto.setPageSize(pageSize);
        pageDto.setOrderBy(orderBy);
        return findPage(pageDto, entity);
    }

    /**
     * 新增
     * 
     * @param entity
     * @return
     */
    @Transactional(readOnly = false)
    public T insert(T entity) {
        mapper.insert(entity);
        return entity;
    }

    /**
     * 更新, 注意: 主键外的字段都会被更新
     * 
     * @param entity
     */
    @Transactional(readOnly = false)
    public void update(T entity) {
        mapper.update(entity);
    }

    /**
     * 删除
     * 
     * @param id
     */
    @Transactional(readOnly = false)
    public void delete(ID id) {
        mapper.delete(id);
    }

    /**
     * 保存或更新<br/>
     * 实体类的主键属性必须被{@link PrimaryKey}注解标记才能使用该方法
     * 
     * @param entity
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    @Transactional(readOnly = false)
    @SuppressWarnings("unchecked")
    public T save(T entity) throws IllegalArgumentException, IllegalAccessException {

        Object pkValue = null;
        boolean isAutoIncrease = false;
        boolean tag = true;

        Class<T> clazz = getEntityType();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(PrimaryKey.class)) {
                field.setAccessible(true);
                pkValue = field.get(entity);
                isAutoIncrease = field.getAnnotation(PrimaryKey.class).increase();
                tag = false;
                break;
            }
        }
        if (tag) {
            throw new RuntimeException("can not scan a primary key, plase add a annotation tag");
        }

        // 如果是主键自增的情况
        if (isAutoIncrease) {
            if (pkValue == null) {
                return insert(entity);
            }
            update(entity);
            return entity;

        }
        // 如果主键不是自增
        if (pkValue == null) {
            throw new RuntimeException("primary key must not be null");
        }
        if (isExist((ID) pkValue)) {
            update(entity);
            return entity;
        }
        return insert(entity);

    }

    /**
     * 统计记录条数
     * 
     * @param entity
     * @return
     */
    public long count(T entity) {
        return mapper.count(entity);
    }

    /**
     * 判断记录是否存在
     * 
     * @param id
     * @return
     */
    public boolean isExist(ID id) {
        return mapper.isExist(id) != 0;
    }

    @SuppressWarnings("unchecked")
    private Class<T> getEntityType() {
        return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

}
