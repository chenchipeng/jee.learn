package com.jee.learn.test.manager.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.manager.LearnManagerApplication;
import com.jee.learn.manager.domain.sys.SysUser;
import com.jee.learn.manager.service.sys.SysUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnManagerApplication.class)
public class SysUserServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SysUserServiceTest.class);

    @Autowired
    private SysUserService sysUserService;

    @Test
    public void findOneTest() {
        SysUser user = sysUserService.findOne("1");
        log.debug("{}", user.getLoginName());
    }

}
