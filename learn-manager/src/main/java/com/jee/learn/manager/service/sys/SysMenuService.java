package com.jee.learn.manager.service.sys;

import java.util.List;

import com.jee.learn.manager.domain.sys.SysMenu;
import com.jee.learn.manager.support.dao.service.EntityService;

public interface SysMenuService extends EntityService<SysMenu, String> {

    /**
     * 根据用户id查找其有用的菜单
     * 
     * @param userId
     * @return
     */
    List<SysMenu> findListByUserId(String userId);

}
