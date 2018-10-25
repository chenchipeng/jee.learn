package com.jee.learn.manager.repository.sys;

import com.jee.learn.manager.config.datasource.dynamic.TargetDataSource;
import com.jee.learn.manager.domain.sys.SysUser;
import com.jee.learn.manager.support.spec.repository.SpecRepository;

public interface SysUserRepository extends SpecRepository<SysUser, String> {

    /**
     * 根据用户登录名查询
     * 
     * @param loginName
     * @return
     */
    @TargetDataSource
    SysUser findOneByLoginName(String loginName);

}
