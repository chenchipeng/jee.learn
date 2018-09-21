package com.jee.learn.mybatis.service;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jee.learn.mybatis.domain.BaseEntity;
import com.jee.learn.mybatis.domain.api.ApiUser;
import com.jee.learn.mybatis.repository.BaseMapper;
import com.jee.learn.mybatis.support.PageDto;

@Transactional(readOnly=true)
public class BaseService<T extends BaseEntity, ID extends Serializable, M extends BaseMapper<T, ID>> {

    @Autowired
    protected M mapper;

    protected T get(ID id) {
        return mapper.get(id);
    }

    protected T findOne(T entity) {
        List<T> list = findList(entity);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        if (list.size() != 1) {
            throw new RuntimeException("the result more than one!");
        }
        return list.get(0);
    }

    protected List<T> findList(T entity) {
        return mapper.findList(entity);
    }

    protected PageDto<T> findPage(PageDto<T> pageDto, T entity) {
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
    
    @Transactional(readOnly=false)
    protected T insert(T entity) {
        mapper.insert(entity);
        return entity;
    }
    
    
    @Transactional(readOnly=false)
    protected void update(T entity) {
        mapper.update(entity);
    }
    
    @Transactional(readOnly=false)
    protected void delete(ID id) {
        mapper.delete(id);
    }
    
    protected void save(T entity,String primaryKey) {
        
    }

}
