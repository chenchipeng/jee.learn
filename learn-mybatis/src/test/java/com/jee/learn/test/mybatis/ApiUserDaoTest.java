package com.jee.learn.test.mybatis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.mybatis.LearnMybatisApplication;
import com.jee.learn.mybatis.dao.ApiUserMapper;
import com.jee.learn.mybatis.entity.ApiUser;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnMybatisApplication.class)
public class ApiUserDaoTest {

    @Autowired
    private ApiUserMapper apiUserMapper;

    @Test
    public void getTest() {
        try {
            ApiUser u = apiUserMapper.get2("1");
            System.out.println(u.getLoginName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
