package com.jee.learn.test.mybatis;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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

    @Test
    public void findPage() {
        Page<ApiUser> page = PageHelper.startPage(1, 10);
        List<ApiUser> list = apiUserDao.findList(new ApiUser());
        list.forEach(item -> {
            System.out.println(item.getLoginName());
        });
        System.out.println("记录总数" + page.getTotal());
    }
}
