package com.jee.learn.test.jpa;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.jpa.LearnJpaApplication;
import com.jee.learn.jpa.domain.ApiUser;
import com.jee.learn.jpa.service.api.ApiUserService;
import com.jee.learn.jpa.support.dao.EntityDao;
import com.jee.learn.jpa.support.dao.Page;
import com.jee.learn.jpa.support.dao.service.EntityService;
import com.jee.learn.jpa.util.mapper.JsonMapper;

/**
 * {@link EntityService} 测试类
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月11日 上午10:00:12 ccp 新建
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnJpaApplication.class)
public class EntityDaoTest {

    private static final Logger logger = LoggerFactory.getLogger(EntityDaoTest.class);

    @Autowired
    private ApiUserService apiUserService;

    private void printList(List<ApiUser> list) {
        list.forEach(item -> logger.debug("{}", item.getLoginName()));
    }

    @Test
    public void getEntityDaoTest() {
        EntityDao entityDao = apiUserService.getEntityDao();
        assertNotNull(entityDao);
        logger.debug("{}", entityDao.getClass().getName());

    }

    @Test
    public void findOneTest() {
        ApiUser u = apiUserService.findOne("1");
        logger.debug("{}", u.getLoginName());

        u.setLoginName("alice");
        u = apiUserService.findOne(u);
        assertNotNull(u);
        logger.debug("{}", u.getLoginName());

        u = apiUserService.findOne("delFlag", "0");
        logger.debug("{}", u.getLoginName());
    }

    @Test
    public void findListTest() {
        ApiUser u = new ApiUser();
        u.setDelFlag("0");
        List<ApiUser> list = apiUserService.findList(u);
        printList(list);

        list = apiUserService.findList(u, "loginName DESC");
        printList(list);

        list = apiUserService.findList("delFlag", "0");
        printList(list);

        list = apiUserService.findList("delFlag", "0", "loginName DESC");
        printList(list);
    }

    @Test
    public void findPageTest() {
        ApiUser u = new ApiUser();
        u.setDelFlag("0");
        Page<ApiUser> page = apiUserService.findPage(u, 0, 3);
        logger.debug("{}", JsonMapper.toJson(page));
        printList(page.getRows());

        page = apiUserService.findPage(u, page);
        logger.debug("{}", JsonMapper.toJson(page));
        printList(page.getRows());

        page = apiUserService.findPage(u, 0, 3, "loginName DESC");
        logger.debug("{}", JsonMapper.toJson(page));
        printList(page.getRows());

        page = apiUserService.findPage(u, page, "loginName DESC");
        logger.debug("{}", JsonMapper.toJson(page));
        printList(page.getRows());
    }
    
    @Test
    public void saveOrUpdateTest() {
        ApiUser u = new ApiUser();
        u.setId("0");
        apiUserService.saveOrUpdate(u);
        
        u = new ApiUser();
        u.setId("0");
        apiUserService.saveOrUpdate(u);
    }

}
