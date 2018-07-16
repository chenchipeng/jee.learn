package com.jee.learn.test.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.LearnManagerApplication;
import com.jee.learn.common.mapper.JsonMapper;
import com.jee.learn.model.sys.SysArea;
import com.jee.learn.model.sys.SysOffice;
import com.jee.learn.model.sys.SysRole;
import com.jee.learn.model.sys.SysUser;
import com.jee.learn.model.sys.SysUserRole;
import com.jee.learn.service.sys.SysAreaService;
import com.jee.learn.service.sys.SysOfficeService;
import com.jee.learn.service.sys.SysUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnManagerApplication.class)
public class SysUserServiceTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysOfficeService sysOfficeService;
    @Autowired
    private SysAreaService sysAreaService;

    @Test
    public void hello() {
        logger.info("hello {}");
    }

    @Test
    @Transactional
    public void findOneTest() {

        try {
            SysUser u = sysUserService.findOne("1");
            SysOffice o = sysOfficeService.findOne(u.getOffice().getId());
            SysArea a = sysAreaService.findOne(o.getSysArea().getId());
            logger.info("{}", JsonMapper.toJsonString(a));

            logger.info("{}", JsonMapper.toJsonString(u));
        } catch (Exception e) {
            logger.info("测试数据不完整", e);
        }

    }

    @Test
    @Transactional
    @Rollback(true) // 决定测试例子是否提交事务
    public void deleteSysUserRoleBySysUserIdTest() {

        try {
            sysUserService.deleteSysUserRoleBySysUserId("100");
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
        sysUserService.createSysUserRoleBySysUserId(sysUserRole);
    }

    @Test
    @Transactional
    @Rollback(true)
    public void findByLoginNameTest() {
        SysUser u = sysUserService.findByLoginName("thinkgem");
        logger.info("{}", JsonMapper.toJsonString(u));
    }

}
