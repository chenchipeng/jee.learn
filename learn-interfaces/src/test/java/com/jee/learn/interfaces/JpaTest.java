package com.jee.learn.interfaces;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.domain.ApiUser;
import com.jee.learn.interfaces.repository.api.ApiUserRepository;
import com.jee.learn.interfaces.service.api.ApiUserService;
import com.jee.learn.interfaces.support.jpa.dao.EntityDao;
import com.jee.learn.interfaces.support.jpa.dao.Page;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnInterfaceApplication.class)
public class JpaTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApiUserService apiUserService;
    @Autowired
    private ApiUserRepository apiUserRepository;
    @Autowired
    private EntityDao entityDao;

    @Value("${spring.jpa.aop:*}")
    private String[] aopKeys;

    @Test
    public void hello() {
        logger.debug("hello");
        for (String key : aopKeys) {
            logger.debug("repository 拦截 {}*", key);
        }
    }

    @Test
    public void apiUserServiceGetTest() {
        ApiUser u = apiUserService.get("1");
        logger.debug("service 拦截 {}", u.getLoginName());
    }

    @Test
    public void apiUserServicefindOneTest() {
        ApiUser u = apiUserService.findOne("1");
        logger.debug("base service 拦截 {}", u.getLoginName());
    }

    @Test
    public void apiUserRepositoryFindOneByIdTest() {
        // 自定义repository方法
        ApiUser u = apiUserRepository.findOneById("1");
        logger.debug("repository 拦截 {}", u.getLoginName());
    }

    /**
     * 在这里竟然无法进行切入？因为有 {@link Transactional},所以dao层不受切换控制，因此该测试无法切换，在 Controller中测试时可以切换
     */
    @Test
    @Transactional(readOnly = false)
    public void apiUserRepositoryGetOneTest() {
        // repository自带方法，需要@Transactional注解支持，否则报错：no session
        try {
            ApiUser u = apiUserRepository.getOne("1");
            logger.debug("repository 拦截 {}", u.getLoginName());
        } catch (Exception e) {
            logger.info("", e);
        }
    }

    @Test
    public void entityDaofindOneTest() {
        ApiUser u = entityDao.findOne(ApiUser.class, "1");
        logger.debug("entityDao 拦截 {}", u.getLoginName());
    }

    @Test
    @Transactional(readOnly = true)
    @Rollback(false)
    public void apiUsersaveOrUpdateTest() {
        ApiUser u = apiUserService.get("1");
        u.setRemarks(String.valueOf(System.currentTimeMillis()));
        apiUserService.saveOrUpdate(u);
        logger.debug("service 拦截 {} - {}", u.getLoginName(), u.getRemarks());
    }

    @Test
    public void findPageTest() {
        ApiUser u = new ApiUser();
        u.setIsEnable("1");

        Page<ApiUser> page = apiUserService.findPage(u, 2, 3, "id");
        System.out.println(page.toString());

//        page = new Page<>();
//        page.setPageNum(3);
//        page.setPageSize(3);
//        page = apiUserService.findPage(u, page, "id");
//        logger.info("{}", page.toString());

        for (ApiUser item : page.getContent()) {
            System.out.println(item.getId());
        }
    }

}