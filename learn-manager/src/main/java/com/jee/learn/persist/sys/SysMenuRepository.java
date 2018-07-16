package com.jee.learn.persist.sys;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jee.learn.common.persist.jpa.CustomJpaRepository;
import com.jee.learn.model.sys.SysMenu;

@Repository
public interface SysMenuRepository extends CustomJpaRepository<SysMenu, String> {

    /** 根据菜单Id删除其角色菜单关联 */
    @Modifying
    @Query("DELETE FROM SysRoleMenu srm WHERE srm.sysMenu.id = :id")
    void deleteSysRoleMenuBySysMenuId(@Param("id") String id);

    /** 根据菜单Id新增其角色菜单关联 */
    @Modifying
    @Query(value = "INSERT INTO sys_role_menu (id, role_id, menu_id) VALUES (?1, ?2, ?3) ", nativeQuery = true)
    void createSysRoleMenuBySysMenuId(String id, String sysRoleId, String sysMenuId);

    /** 查询用户授权的菜单 */
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Query(value = "SELECT DISTINCT m.* FROM sys_menu m LEFT JOIN sys_role_menu rm ON m.id=rm.menu_id LEFT JOIN sys_user_role ur ON ur.role_id = rm.role_id WHERE ur.user_id = ?1 AND m.del_flag = '0' ", nativeQuery = true)
    List<SysMenu> findByUser(String userId);

}