package com.jee.learn.persist.sys;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jee.learn.common.persist.jpa.CustomJpaRepository;
import com.jee.learn.model.sys.SysUser;

@Repository
public interface SysUserRepository extends CustomJpaRepository<SysUser, String> {

    /** 根据用户Id删除其用户角色关联 */
    @Modifying
    @Query("DELETE FROM SysUserRole sur WHERE sur.sysUser.id = :sysUserId")
    void deleteSysUserRoleBySysUserId(@Param("sysUserId") String sysUserId);

    /* 使用HQL新增数据有点问题 */
    /** 根据用户Id新增其用户角色关联 */
    @Modifying
    @Query(value = "INSERT INTO sys_user_role (id, user_id, role_id) VALUES (?1, ?2, ?3) ", nativeQuery = true)
    void createSysUserRoleBySysUserId(String id, String sysUserId, String sysRoleId);

    /**
     * 查询缓存测试<br/>
     * 查询所有没被删除的用户
     */
    @QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value = "true") })
    @Query("FROM SysUser u WHERE u.delFlag = '0'")
    List<SysUser> findAllByNoDel();
    
    /**根据登录名查询用户 */
    SysUser findByLoginName(String loginName);

}