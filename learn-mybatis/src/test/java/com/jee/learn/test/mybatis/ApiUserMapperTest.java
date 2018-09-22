package com.jee.learn.test.mybatis;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.jee.learn.mybatis.LearnMybatisApplication;
import com.jee.learn.mybatis.domain.api.ApiUser;
import com.jee.learn.mybatis.repository.mapper.ApiUserMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnMybatisApplication.class)
public class ApiUserMapperTest {

    @Autowired
    private ApiUserMapper apiUserMapper;

    @Test
    public void getTest() {
        try {
            ApiUser u = apiUserMapper.get("1");
            System.out.println(u.getLoginName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findPageTest() {
        Page<ApiUser> page = PageHelper.startPage(2, 10).setOrderBy("loginName ASC");
        List<ApiUser> list = apiUserMapper.findList(new ApiUser());
        list.forEach(item -> {
            System.out.println(item.getLoginName());
        });
        System.out.println("记录总数" + page.getTotal());
    }

    @Test
    @Rollback(true)
    @Transactional(readOnly = false)
    public void insertTest() {
        try {
            ApiUser entity = new ApiUser();
            entity.setId("1");
            entity.setLoginName("test");
            apiUserMapper.insert(entity);
            System.out.println(entity.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional(readOnly = false)
    public void updateTest() {
        try {
            ApiUser entity = apiUserMapper.get("1");
            System.out.println(entity.getRemarks());
            
            entity.setRemarks(String.valueOf(System.currentTimeMillis()));
            apiUserMapper.update(entity);
            
            System.out.println(entity.getRemarks());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    @Rollback(true)
    @Transactional(readOnly = false)
    public void deleteTest() {
        try {
            apiUserMapper.delete("1");
            ApiUser entity = apiUserMapper.get("1");
            System.out.println(entity==null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void countTest() {
        try {
            ApiUser entity = new ApiUser();
            entity.setIsEnable("1");
            System.out.println(apiUserMapper.count(entity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
