package com.jee.learn.manager.repository.sys;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jee.learn.manager.config.datasource.dynamic.TargetDataSource;
import com.jee.learn.manager.domain.sys.SysMenu;
import com.jee.learn.manager.support.spec.repository.SpecRepository;

public interface SysMenuRepository extends SpecRepository<SysMenu, String> {

    /**
     * 根据用户id查找其有用的菜单
     * 
     * @param userId
     * @return
     */
    @TargetDataSource
    @Query(value = "SELECT a.* FROM sys_menu a LEFT JOIN sys_role_menu b ON a.id = b.menu_id LEFT JOIN sys_user_role c ON b.role_id = c.role_id WHERE c.user_id = :userId ORDER BY a.sort", nativeQuery = true)
    List<SysMenu> findListByUserId(@Param("userId") String userId);

}
