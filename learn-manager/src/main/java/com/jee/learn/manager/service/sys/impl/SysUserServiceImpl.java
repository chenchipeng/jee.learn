package com.jee.learn.manager.service.sys.impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.manager.domain.sys.SysUser;
import com.jee.learn.manager.security.ShiroUtil;
import com.jee.learn.manager.service.sys.SysUserService;
import com.jee.learn.manager.support.dao.Condition;
import com.jee.learn.manager.support.dao.Sort;
import com.jee.learn.manager.support.dao.service.EntityServiceImpl;
import com.jee.learn.manager.util.time.ClockUtil;

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

    @Override
    @Transactional(readOnly = false)
    public void updateUserLoginInfo(String id) {
        SysUser user = findOne(id);
        if (user != null) {
            // 更新本次登录信息
            user.setLoginIp(ShiroUtil.getSession().getHost());
            user.setLoginDate(ClockUtil.currentDate());
            user.setUpdateDate(user.getLoginDate());
            super.saveOrUpdate(user);
        }
    }

}
