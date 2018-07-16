package com.jee.learn.service.sys;

import java.util.List;

import com.jee.learn.common.service.BaseService;
import com.jee.learn.model.sys.SysUser;
import com.jee.learn.model.sys.SysUserRole;

public interface SysUserService extends BaseService<SysUser, String> {

    /** 根据用户Id删除其用户角色关联 */
    void deleteSysUserRoleBySysUserId(String id);

    /** 根据用户Id新增其用户角色关联 */
    void createSysUserRoleBySysUserId(String id, String sysUserId, String sysRoleId);

    /** 根据用户Id新增其用户角色关联 */
    void createSysUserRoleBySysUserId(SysUserRole sysUserRole);

    /**
     * 查询缓存测试<br/>
     * 查询所有没被删除的用户
     */
    List<SysUser> findAllByNoDel();

    /** 根据登录名查询用户 */
    SysUser findByLoginName(String loginName);
}
