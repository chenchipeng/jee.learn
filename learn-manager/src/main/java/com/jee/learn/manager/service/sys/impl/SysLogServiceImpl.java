package com.jee.learn.manager.service.sys.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.manager.domain.sys.SysLog;
import com.jee.learn.manager.service.sys.SysLogService;
import com.jee.learn.manager.support.dao.Condition;
import com.jee.learn.manager.support.dao.Sort;
import com.jee.learn.manager.support.dao.service.EntityServiceImpl;

@Service
@Transactional(readOnly = true)
public class SysLogServiceImpl extends EntityServiceImpl<SysLog, String> implements SysLogService {

    @Override
    protected Condition parseQueryParams(SysLog entity) {
        return super.parseQueryParams(entity);
    }

    @Override
    protected Sort parseSort(String orderBy) {
        return super.parseSort(orderBy);
    }

}
