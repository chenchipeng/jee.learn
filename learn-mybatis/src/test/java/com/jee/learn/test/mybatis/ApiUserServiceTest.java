package com.jee.learn.test.mybatis;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.mybatis.LearnMybatisApplication;
import com.jee.learn.mybatis.domain.api.ApiUser;
import com.jee.learn.mybatis.service.ApiUserService;
import com.jee.learn.mybatis.support.PageDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnMybatisApplication.class)
public class ApiUserServiceTest {

    @Autowired
    private ApiUserService apiUserService;

    @Test
    public void getTest() {
        ApiUser entity = apiUserService.get("1");
        System.out.println(entity.getLoginName());
    }

    @Test
    public void findOneTest() {
        ApiUser entity = new ApiUser();
        entity.setId("1");
        entity = apiUserService.findOne(entity);
        System.out.println(entity.getLoginName());
    }

    @Test
    public void findListTest() {
        ApiUser entity = new ApiUser();
        entity.setIsEnable("1");
        List<ApiUser> list = apiUserService.findList(entity);
        System.out.println(list.size());
    }

    @Test
    public void findPageTest() {
        PageDto<ApiUser> dto = apiUserService.findPage(1, 5, null, null);
        System.out.println(dto.getTotal());
    }

    @Test
    @Rollback(true)
    @Transactional(readOnly = false)
    public void insertTest() {
        try {
            ApiUser entity = new ApiUser();
            entity.setId("0");
            entity.setLoginName("test");
            apiUserService.insert(entity);
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
            ApiUser entity = apiUserService.get("1");
            System.out.println(entity.getRemarks());

            entity.setRemarks(String.valueOf(System.currentTimeMillis()));
            apiUserService.update(entity);

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
            apiUserService.delete("1");
            ApiUser entity = apiUserService.get("1");
            System.out.println(entity == null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Rollback(true)
    @Transactional(readOnly = false)
    public void saveTest() {
        try {
            // 更新
            ApiUser entity = apiUserService.get("1");
            System.out.println(entity.getRemarks());

            entity.setRemarks(String.valueOf(System.currentTimeMillis()));
            entity = apiUserService.save(entity);

            System.out.println(entity.getRemarks());

            // 新增
            entity = new ApiUser();
            entity.setId("0");
            entity.setLoginName("test");
            entity = apiUserService.save(entity);
            System.out.println(entity.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
