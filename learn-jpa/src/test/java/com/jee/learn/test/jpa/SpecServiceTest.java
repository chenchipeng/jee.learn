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
import com.jee.learn.jpa.support.spec.Filter;
import com.jee.learn.jpa.support.spec.QueryParams;
import com.jee.learn.jpa.support.spec.service.SpecSercice;
import com.jee.learn.jpa.util.mapper.JsonMapper;

/**
 * SpecSercice 测试类
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月10日 上午10:38:11 1002360 新建
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnJpaApplication.class)
public class SpecServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(SpecServiceTest.class);

    @Autowired
    private SpecSercice<ApiUser, String> specSercice;

    @Test
    public void findOneTest() {
        ApiUser u = specSercice.findOne("1");
        logger.debug("{}", JsonMapper.toJson(u));
    }

    @Test
    public void findOneEqPropertyTest() {
        ApiUser u = specSercice.findOne("id", "1");
        logger.debug("{}", JsonMapper.toJson(u));
    }

    @Test
    public void findOneByPropertyTest() {
        List<ApiUser> list = specSercice.findList("delFlag", "0");
        logger.debug("{}", JsonMapper.toJson(list));
    }

    @Test
    public void findListTest() {
        List<ApiUser> list = specSercice.findList(new ApiUser());
        list.forEach(item -> {
            logger.debug("{}", item.getLoginName());
        });

        QueryParams<ApiUser> spec = new QueryParams<>();
        spec.and(Filter.eq("delFlag", "10"));
        list = specSercice.findList(spec);
        list.forEach(item -> {
            logger.debug("{}", item.getLoginName());
        });
    }

    @Test
    public void logicDeleteTest() {
        ApiUser u = new ApiUser();
        u.setId("2");
        specSercice.logicDelete(u);
    }

    @Test
    public void primaryKeyValidationTest() {
        Object obj = "string";
        logger.debug("{}", obj instanceof String);

        obj = 1;
        logger.debug("{}", obj instanceof String);
        logger.debug("{}", obj instanceof Integer);
    }

}
