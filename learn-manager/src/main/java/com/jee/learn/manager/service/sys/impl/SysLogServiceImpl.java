package com.jee.learn.manager.service.sys.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.manager.domain.sys.SysLog;
import com.jee.learn.manager.service.sys.SysLogService;
import com.jee.learn.manager.support.dao.Condition;
import com.jee.learn.manager.support.dao.Sort;
import com.jee.learn.manager.support.dao.service.EntityServiceImpl;
import com.jee.learn.manager.util.idgen.IdGenerate;

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

    @Override
    public void saveOrUpdate(SysLog entity) {
        if (entity == null) {
            return;
        }
        // 处理SysLog主键非自增的情况
        if (StringUtils.isBlank(entity.getId())) {
            entity.setId(IdGenerate.fastUUID());
            super.getEntityDao().save(entity);
        } else {
            super.getEntityDao().update(entity);
        }
    }

}
