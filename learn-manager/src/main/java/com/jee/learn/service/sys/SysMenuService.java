package com.jee.learn.service.sys;

import java.util.List;

import com.jee.learn.common.service.BaseService;
import com.jee.learn.model.sys.SysMenu;
import com.jee.learn.model.sys.SysRoleMenu;

public interface SysMenuService extends BaseService<SysMenu, String> {

    /** 根据菜单Id删除其角色菜单关联 */
    void deleteSysRoleMenuBySysMenuId(String id);

    /** 根据菜单Id新增其角色菜单关联 */
    void createSysRoleMenuBySysMenuId(String id, String sysMenuId, String sysRoleId);

    /** 根据菜单Id新增其角色菜单关联 */
    void createSysRoleMenuBySysMenuId(SysRoleMenu sysRoleMenu);

    /** 查询用户授权的菜单 */
    List<SysMenu> findByUser(String userId);
}
