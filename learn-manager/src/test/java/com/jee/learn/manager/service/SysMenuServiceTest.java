package com.jee.learn.manager.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.manager.LearnManagerApplication;
import com.jee.learn.manager.dto.sys.MenuDto;
import com.jee.learn.manager.service.sys.SysMenuService;
import com.jee.learn.manager.util.mapper.JsonMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnManagerApplication.class)
public class SysMenuServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SysMenuServiceTest.class);

    @Autowired
    private SysMenuService sysMenuService;

    @Test
    public void getCurrentUserMenuTest() {
        MenuDto dto = sysMenuService.getCurrentUserMenuDtoTree();
        log.debug("{}", JsonMapper.toJson(dto));
    }

}
