package com.jee.learn.manager.service.sys.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.manager.domain.sys.SysUser;
import com.jee.learn.manager.service.sys.SysUserService;
import com.jee.learn.manager.support.dao.Condition;
import com.jee.learn.manager.support.dao.Sort;
import com.jee.learn.manager.support.dao.service.EntityServiceImpl;

@Service
@Transactional(readOnly = true)
public class SysUserServiceImpl extends EntityServiceImpl<SysUser, String> implements SysUserService {

    @Override
    protected Condition parseQueryParams(SysUser entity) {
        return super.parseQueryParams(entity);
    }

    @Override
    protected Sort parseSort(String orderBy) {
        return super.parseSort(orderBy);
    }

}
