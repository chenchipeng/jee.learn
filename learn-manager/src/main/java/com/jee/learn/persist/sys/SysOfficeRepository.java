package com.jee.learn.persist.sys;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jee.learn.common.persist.jpa.CustomJpaRepository;
import com.jee.learn.model.sys.SysOffice;

@Repository
public interface SysOfficeRepository extends CustomJpaRepository<SysOffice, String> {

    /** 根据部门Id删除其角色部门关联 */
    @Modifying
    @Query("DELETE FROM SysRoleOffice sur WHERE sur.sysOffice.id = :id")
    void deleteSysRoleOfficeBySysOfficeId(@Param("id") String id);

    /** 根据用户Id新增其用户角色关联 */
    @Modifying
    @Query(value = "INSERT INTO sys_role_office (id, role_id, office_id) VALUES (?1, ?2, ?3) ", nativeQuery = true)
    void createSysRoleOfficeBySysOfficeId(String id, String sysRoleId, String sysOfficeId);

}