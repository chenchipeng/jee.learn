package com.jee.learn.test.mybatis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.mybatis.LearnMybatisApplication;
import com.jee.learn.mybatis.domain.ApiUser;
import com.jee.learn.mybatis.repository.mapper.ApiUserDao;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnMybatisApplication.class)
public class ApiUserDaoTest {

    @Autowired
    private ApiUserDao apiUserDao;

    @Test
    public void getTest() {
        try {
            ApiUser u = apiUserDao.get("1");
            System.out.println(u.getLoginName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
