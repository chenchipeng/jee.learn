package com.jee.learn.test.jpa;

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
import com.jee.learn.jpa.support.spec.Filter;
import com.jee.learn.jpa.support.spec.QueryParams;
import com.jee.learn.jpa.util.mapper.JsonMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnJpaApplication.class)
public class ApiUserServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(ApiUserServiceTest.class);

    @Autowired
    private ApiUserService apiUserService;

    @Test
    public void findOneTest() {
        ApiUser u = apiUserService.findOne("1");
        logger.debug("{}", JsonMapper.toJson(u));
    }

    @Test
    public void findListTest() {
        ApiUser u = new ApiUser();
        u.setDelFlag("10");

        List<ApiUser> list = apiUserService.findList(u);
        list.forEach(item -> {
            logger.debug("{}", item.getLoginName());
        });

        QueryParams<ApiUser> spec = new QueryParams<>();
        spec.and(Filter.eq("delFlag", "0"));
        list = apiUserService.findList(spec);
        list.forEach(item -> {
            logger.debug("{}", item.getLoginName());
        });
    }

}
