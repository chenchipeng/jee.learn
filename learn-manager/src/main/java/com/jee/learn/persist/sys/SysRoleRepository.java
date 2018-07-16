package com.jee.learn.persist.sys;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jee.learn.common.persist.jpa.CustomJpaRepository;
import com.jee.learn.model.sys.SysRole;

@Repository
public interface SysRoleRepository extends CustomJpaRepository<SysRole, String> {

    /** 根据角色Id删除其用户角色关联 */
    @Modifying
    @Query("DELETE FROM SysUserRole sur WHERE sur.sysRole.id = :sysRoleId")
    void deleteSysUserRoleBySysRoleId(@Param("sysRoleId") String sysRoleId);

    /** 根据角色Id新增其用户角色关联 */
    @Modifying
    @Query(value = "INSERT INTO sys_user_role (id, user_id, role_id) VALUES (?1, ?2, ?3) ", nativeQuery = true)
    void createSysUserRoleBySysRoleId(String id, String sysUserId, String sysRoleId);

    /** 根据角色Id删除其角色部门关联 */
    @Modifying
    @Query("DELETE FROM SysRoleOffice sro WHERE sro.sysRole.id = :sysRoleId")
    void deleteSysRoleOfficeBySysRoleId(@Param("sysRoleId") String sysRoleId);

    /** 根据角色Id新增其角色部门关联 */
    @Modifying
    @Query(value = "INSERT INTO sys_role_office (id, role_id, office_id) VALUES (?1, ?2, ?3) ", nativeQuery = true)
    void createSysRoleOfficeBySysRoleId(String id, String sysRoleId, String sysOfficeId);

    /** 根据角色Id删除其角色菜单关联 */
    @Modifying
    @Query("DELETE FROM SysRoleMenu sro WHERE sro.sysRole.id = :sysRoleId")
    void deleteSysRoleMenuBySysRoleId(@Param("sysRoleId") String sysRoleId);

    /** 根据角色Id新增其角色菜单关联 */
    @Modifying
    @Query(value = "INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (?1, ?2, ?3) ", nativeQuery = true)
    void createSysRoleMenuBySysRoleId(String id, String sysRoleId, String sysMenuId);

}