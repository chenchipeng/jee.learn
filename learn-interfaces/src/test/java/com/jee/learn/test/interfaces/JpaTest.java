package com.jee.learn.test.interfaces;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.interfaces.LearnInterfaceApplication;
import com.jee.learn.interfaces.domain.ApiUser;
import com.jee.learn.interfaces.repository.ApiUserRepository;
import com.jee.learn.interfaces.service.api.ApiUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnInterfaceApplication.class)
public class JpaTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApiUserService apiUserService;
    @Autowired
    private ApiUserRepository apiUserRepository;

    @Test
    public void hello() {
        logger.debug("hello");
    }

    @Test
    public void findOneTest() {
        ApiUser u1 = apiUserService.get("1");
        logger.debug("service 拦截 {}", u1.getLoginName());
    }
    
    @Test
    public void repositoryFindOneTest() {
        ApiUser u1 = apiUserRepository.findOneById("1");
        logger.debug("repository 拦截 {}", u1.getLoginName());
    }

    @Test
    public void updateRemarksTest() {
        ApiUser u1 = apiUserService.updateRemarks("1");
        logger.debug("{} - {}", u1.getLoginName(), u1.getRemarks());
    }

}
