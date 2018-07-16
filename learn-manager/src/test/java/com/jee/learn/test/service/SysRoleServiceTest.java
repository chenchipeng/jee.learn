package com.jee.learn.test.service;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.LearnManagerApplication;
import com.jee.learn.common.mapper.JsonMapper;
import com.jee.learn.common.util.Constant;
import com.jee.learn.model.sys.SysMenu;
import com.jee.learn.model.sys.SysOffice;
import com.jee.learn.model.sys.SysRole;
import com.jee.learn.model.sys.SysRoleMenu;
import com.jee.learn.model.sys.SysRoleOffice;
import com.jee.learn.model.sys.SysUser;
import com.jee.learn.model.sys.SysUserRole;
import com.jee.learn.service.sys.SysRoleService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnManagerApplication.class)
public class SysRoleServiceTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysRoleService sysRoleService;

    @Test
    public void hello() {
        logger.info("hello");
    }

    @Test
    @Transactional
    public void findOneTest() {
        SysRole r = sysRoleService.findOne("4");
        logger.info("{}", JsonMapper.toJsonString(r.getSysUserRoles()));
    }

    @Test
    @Transactional
    @Rollback(true) // 决定测试例子是否提交事务
    public void deleteSysUserRoleBySysUserIdTest() {
        try {
            sysRoleService.deleteSysUserRoleBySysRoleId("4");
        } catch (Exception e) {
            logger.info("", e);
        }
    }

    @Test
    @Transactional
    @Rollback(true)
    public void createSysUserRoleBySysUserIdTest() {
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setSysUser(new SysUser("666"));
        sysUserRole.setSysRole(new SysRole("999"));
        sysRoleService.createSysUserRoleBySysRoleId(sysUserRole);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void deleteSysRoleOfficeBySysRoleIdTest() {
        sysRoleService.deleteSysRoleOfficeBySysRoleId("1");
    }

    @Test
    @Transactional
    @Rollback(true)
    public void createSysRoleOfficeBySysRoleIdTest() {
        SysRoleOffice sysRoleOffice = new SysRoleOffice();
        sysRoleOffice.setSysOffice(new SysOffice("1"));
        sysRoleOffice.setSysRole(new SysRole("1"));
        sysRoleService.createSysRoleOfficeBySysRoleId(sysRoleOffice);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void deleteSysRoleMenuBySysRoleIdTest() {
        sysRoleService.deleteSysRoleMenuBySysRoleId("100");
    }

    @Test
    @Transactional
    @Rollback(true)
    public void createSysRoleMenuBySysRoleIdTest() {
        SysRoleMenu sysRoleMenu = new SysRoleMenu();
        sysRoleMenu.setSysMenu(new SysMenu("100"));
        sysRoleMenu.setSysRole(new SysRole("100"));
        sysRoleService.createSysRoleMenuBySysRoleId(sysRoleMenu);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findPageTest() {
        Page<SysRole> page = sysRoleService.findPage(null, 1, Constant.DEFAULT_PAGE_SIZE, StringUtils.EMPTY);
        logger.info("{}", JsonMapper.toJsonString(page.getContent()));
    }

}
