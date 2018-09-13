package com.jee.learn.test.jpa;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.jpa.LearnJpaApplication;
import com.jee.learn.jpa.domain.ApiUser;
import com.jee.learn.jpa.support.spec.Filter;
import com.jee.learn.jpa.support.spec.QueryParams;
import com.jee.learn.jpa.support.spec.service.SpecSercice;

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

    private void printList(List<ApiUser> list) {
        list.forEach(item -> {
            logger.debug("{}", item.getLoginName());
        });
    }

    @Test
    public void findOneTest() {
        // 根据id查询
        ApiUser u = specSercice.findOne("1");
        logger.debug("{}", u.getLoginName());

        // 构造QueryParams查询
        QueryParams<ApiUser> queryParams = new QueryParams<>();
        queryParams.and(Filter.eq("delFlag", "0"));
        u = specSercice.findOne(queryParams);
        logger.debug("{}", u.getLoginName());

        // 通过父类方法analyzeQueryParams()构造QueryParams查询
        specSercice.findOne(u);
        logger.debug("{}", u.getLoginName());

        // 指定属性值查询一条记录
        u = specSercice.findOne("loginName", "alice");
        logger.debug("{}", u.getLoginName());

    }

    @Test
    public void findListTest() {
        // 构造QueryParams查询
        QueryParams<ApiUser> queryParams = new QueryParams<>();
        queryParams.and(Filter.eq("delFlag", "0"));
        List<ApiUser> list = specSercice.findList(queryParams);
        printList(list);

        // 构造QueryParams查询, 并带排序
        Sort sort = new Sort(Direction.DESC, "id");
        list = specSercice.findList(queryParams, sort);
        printList(list);

        // 通过父类方法analyzeQueryParams()构造QueryParams查询
        ApiUser u = new ApiUser();
        specSercice.findList(u);
        printList(list);

        // 通过父类方法analyzeQueryParams()构造QueryParams查询, 并排序
        specSercice.findList(u, sort);
        printList(list);

        // 指定属性值查询
        list = specSercice.findList("delFlag", "0");
        printList(list);

        // 指定属性值查询, 并排序
        list = specSercice.findList("delFlag", "0", sort);
        printList(list);

    }

    @Test
    public void findPageTest() {

        ApiUser u = new ApiUser();
        Sort sort = new Sort(Direction.DESC, "id");
        QueryParams<ApiUser> queryParams = new QueryParams<>();
        queryParams.and(Filter.eq("delFlag", "0"));
        Page<ApiUser> page = null;

        // 构造QueryParams查询
        page = specSercice.findPage(queryParams, 1, 1);
        printList(page.getContent());

        // 构造QueryParams查询, 并排序
        page = specSercice.findPage(queryParams, 0, 2, sort);
        printList(page.getContent());

        // 通过父类方法analyzeQueryParams()构造QueryParams查询
        page = specSercice.findPage(u, PageRequest.of(0, 1, sort));
        printList(page.getContent());

        // 通过父类方法analyzeQueryParams()构造QueryParams查询
        page = specSercice.findPage(u, 1, 1);
        printList(page.getContent());

        // 通过父类方法analyzeQueryParams()构造QueryParams查询
        page = specSercice.findPage(u, 1, 1, sort);
        printList(page.getContent());

    }

    @Test
    @Rollback(false)
    @Transactional(readOnly = false)
    public void saveTest() {
        ApiUser u = specSercice.findOne("1");
        logger.debug("{} - {}", u.getLoginName(), u.getRemarks());

        u.setRemarks(String.valueOf(System.currentTimeMillis()));
        specSercice.save(u);
        logger.debug("{} - {}", u.getLoginName(), u.getRemarks());
    }

    @Test
    @Rollback(false)
    @Transactional(readOnly = false)
    public void saveAndFlushTest() {
        ApiUser u = specSercice.findOne("1");
        logger.debug("{} - {}", u.getLoginName(), u.getRemarks());

        // 体验不了saveAndFlush的效果
        u.setRemarks(String.valueOf(System.currentTimeMillis()));
        specSercice.saveAndFlush(u);
        logger.debug("{} - {}", u.getLoginName(), u.getRemarks());

        ApiUser u1 = specSercice.findOne("1");
        logger.debug("{} - {}", u1.getLoginName(), u1.getRemarks());
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
