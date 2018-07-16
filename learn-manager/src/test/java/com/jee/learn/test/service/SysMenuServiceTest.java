package com.jee.learn.test.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.LearnManagerApplication;
import com.jee.learn.common.mapper.JsonMapper;
import com.jee.learn.dto.MenuDto;
import com.jee.learn.model.sys.SysMenu;
import com.jee.learn.service.sys.SysMenuService;
import com.jee.learn.util.TreeUtils;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnManagerApplication.class)
public class SysMenuServiceTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysMenuService sysMenuService;

    @Test
    public void findByUserTest() {
        // 第一次是顺便做查询缓存测试
        sysMenuService.findByUser("1");
        List<SysMenu> list = sysMenuService.findByUser("1");
        logger.info("{}", list.size());
    }

    @Test
    @Transactional
    public void LeftMenuTest() {
        List<SysMenu> orignMenuList = sysMenuService.findByUser("1");
        
        SysMenu m1 = orignMenuList.get(0);
        logger.info("m1.getChildren = {}",JsonMapper.toJsonString(m1.getChildren()));
        
        List<MenuDto> mdl = TreeUtils.buildNaviMenu(orignMenuList, 2);
        logger.info("mdl = {}", JsonMapper.toJsonString(mdl));
        
        SysMenu m2 = sysMenuService.findOne("1");
        logger.info("m2 = {}",JsonMapper.toJsonString(m2));
        logger.info("m2.getChildren.size = {}",m2.getChildren().size());
        logger.info("m2.getChildren = {}", JsonMapper.toJsonString(m2.getChildren()));
    }

}
