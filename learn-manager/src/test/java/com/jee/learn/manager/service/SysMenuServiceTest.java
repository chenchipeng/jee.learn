package com.jee.learn.manager.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.manager.LearnManagerApplication;
import com.jee.learn.manager.domain.sys.SysMenu;
import com.jee.learn.manager.dto.sys.MenuDto;
import com.jee.learn.manager.service.sys.SysMenuService;
import com.jee.learn.manager.util.base.excrption.RestException;
import com.jee.learn.manager.util.mapper.JsonMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnManagerApplication.class)
public class SysMenuServiceTest {

    private static final Logger log = LoggerFactory.getLogger(SysMenuServiceTest.class);

    @Autowired
    private SysMenuService sysMenuService;

    @Test
    public void getCurrentUserMenuTest() {
        List<SysMenu> menuList = sysMenuService.findList(new SysMenu());
        MenuDto dto = null;
        try {
            dto = sysMenuService.listToTree(menuList, SysMenuService.TREE_LIST_MENU);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RestException(e);
        }
        log.debug("{}", JsonMapper.toJson(dto));
    }

}
