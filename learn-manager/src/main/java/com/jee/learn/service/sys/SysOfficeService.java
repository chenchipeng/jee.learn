package com.jee.learn.service.sys;

import com.jee.learn.common.service.BaseService;
import com.jee.learn.model.sys.SysOffice;
import com.jee.learn.model.sys.SysRoleOffice;

public interface SysOfficeService extends BaseService<SysOffice, String> {

    /** 根据部门Id删除其角色部门关联 */
    void deleteSysRoleOfficeBySysOfficeId(String id);

    /** 根据用户Id新增其用户角色关联 */
    void createSysRoleOfficeBySysOfficeId(String id, String sysRoleId, String sysOfficeId);

    /** 根据用户Id新增其用户角色关联 */
    void createSysRoleOfficeBySysOfficeId(SysRoleOffice sysRoleOffice);

}
