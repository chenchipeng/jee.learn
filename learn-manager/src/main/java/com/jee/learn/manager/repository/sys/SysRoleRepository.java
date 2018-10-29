package com.jee.learn.manager.repository.sys;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jee.learn.manager.config.datasource.dynamic.TargetDataSource;
import com.jee.learn.manager.domain.sys.SysRole;
import com.jee.learn.manager.support.spec.repository.SpecRepository;

public interface SysRoleRepository extends SpecRepository<SysRole, String> {

    @TargetDataSource
    @Query(value = "SELECT a.* FROM sys_role a LEFT JOIN sys_user_role b ON a.id = b.role_id WHERE b.user_id = :userId", nativeQuery = true)
    List<SysRole> findListByUserId(@Param("userId") String userId);
}
