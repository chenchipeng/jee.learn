package com.jee.learn.service.sys;

import org.springframework.data.domain.Page;

import com.jee.learn.common.service.BaseService;
import com.jee.learn.model.sys.SysRole;
import com.jee.learn.model.sys.SysRoleMenu;
import com.jee.learn.model.sys.SysRoleOffice;
import com.jee.learn.model.sys.SysUserRole;

public interface SysRoleService extends BaseService<SysRole, String> {

    /** 根据角色Id删除其用户角色关联 */
    void deleteSysUserRoleBySysRoleId(String id);

    /** 根据角色Id新增其用户角色关联 */
    void createSysUserRoleBySysRoleId(String id, String sysUserId, String sysRoleId);

    /** 根据角色Id新增其用户角色关联 */
    void createSysUserRoleBySysRoleId(SysUserRole sysUserRole);

    /** 根据角色Id删除其角色部门关联 */
    void deleteSysRoleOfficeBySysRoleId(String id);

    /** 根据角色Id新增其角色部门关联 */
    void createSysRoleOfficeBySysRoleId(String id, String sysRoleId, String sysOfficeId);

    /** 根据角色Id新增其角色部门关联 */
    void createSysRoleOfficeBySysRoleId(SysRoleOffice sysRoleOffice);

    /** 根据角色Id删除其角色菜单关联 */
    void deleteSysRoleMenuBySysRoleId(String id);

    /** 根据角色Id新增其角色菜单关联 */
    void createSysRoleMenuBySysRoleId(String id, String sysRoleId, String sysMenuId);

    /** 根据角色Id新增其角色菜单关联 */
    void createSysRoleMenuBySysRoleId(SysRoleMenu sysRoleMenu);

    /** 分页查询 */
    Page<SysRole> findPage(SysRole entity, int pageNo, int pageSize, String orderBy);

}
