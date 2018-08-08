package com.jee.learn.test.interfaces;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.LearnInterfaceApplication;
import com.jee.learn.interfaces.domain.ApiUser;
import com.jee.learn.interfaces.service.TestService;
import com.jee.learn.interfaces.service.api.ApiUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnInterfaceApplication.class)
public class JpaTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApiUserService apiUserService;

    @Autowired
    private TestService testService;

    @Test
    public void hello() {
        logger.debug("hello");
    }

    @Test
    public void findOneTest() {
        ApiUser u = apiUserService.get("1");
        logger.debug("{}", u.getLoginName());
    }

    @Test
    @Transactional(readOnly = false)
    @Rollback(false)
    public void EntityDaoTest() {
        try {
            ApiUser u = testService.findOne("1");
            logger.debug("findOne = {}", u.getLoginName());

            logger.debug("save 之前 = {}", u.getRemarks());
            u.setRemarks(String.valueOf(System.currentTimeMillis()));
            testService.save(u);
            u = testService.findOne("1");
            logger.debug("save 之后 = {}", u.getRemarks());
        } catch (Exception e) {
            logger.debug("", e);
        }
    }

}
