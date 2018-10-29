package com.jee.learn.manager.service.sys;

import java.util.List;

import com.jee.learn.manager.domain.sys.SysRole;
import com.jee.learn.manager.support.dao.service.EntityService;

public interface SysRoleService extends EntityService<SysRole, String> {

    /**
     * 根据用户id查找其有用的角色
     * 
     * @param userId
     * @return
     */
    List<SysRole> findListByUserId(String userId);

}
