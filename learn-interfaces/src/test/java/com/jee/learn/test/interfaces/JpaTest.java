package com.jee.learn.test.interfaces;

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

import com.jee.learn.interfaces.LearnInterfaceApplication;
import com.jee.learn.interfaces.domain.ApiUser;
import com.jee.learn.interfaces.repository.EntityDao;
import com.jee.learn.interfaces.repository.api.ApiUserRepository;
import com.jee.learn.interfaces.service.api.ApiUserService;

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
        ApiUser u = apiUserRepository.findOneById("1");
        logger.debug("repository 拦截 {}", u.getLoginName());
    }

    @Test
    public void entityDaofindOneTest() {
        ApiUser u = entityDao.findOne(ApiUser.class, "1");
        logger.debug("entityDao 拦截 {}", u.getLoginName());
    }

    @Test
    @Transactional(readOnly=true)
    @Rollback(false)
    public void apiUsersaveOrUpdateTest() {
        ApiUser u = apiUserService.get("1");
        u.setRemarks(String.valueOf(System.currentTimeMillis()));
        apiUserService.saveOrUpdate(u);
        logger.debug("service 拦截 {} - {}", u.getLoginName(), u.getRemarks());
    }

}
