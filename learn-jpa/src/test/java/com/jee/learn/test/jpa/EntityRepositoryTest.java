package com.jee.learn.test.jpa;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.jpa.LearnJpaApplication;
import com.jee.learn.jpa.domain.ApiUser;
import com.jee.learn.jpa.repository.ApiUserRepository;
import com.jee.learn.jpa.repository.spec.Filter;
import com.jee.learn.jpa.repository.spec.QueryParams;
import com.jee.learn.jpa.util.JsonMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnJpaApplication.class)
public class EntityRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRepositoryTest.class);

    @Autowired
    private ApiUserRepository apiUserRepository;

    @Test
    public void test1() {
        ApiUser u = apiUserRepository.findOneById("1");
        LOGGER.debug("{}", u.getLoginName());
    }

    @Test
    public void test2() {

        QueryParams<ApiUser> queryParams = new QueryParams<>();
        // 使用Specification条件查询,使用JPQL字段查询
        queryParams.and(Filter.eq("id", "1"));
        Optional<ApiUser> u = apiUserRepository.findOne(queryParams);
        LOGGER.debug("{}", JsonMapper.toJson(u.get()));
    }

    @Test
    public void test3() {

        QueryParams<ApiUser> queryParams = new QueryParams<>();
        queryParams.and(Filter.eq("delFlag", "0"));

        List<ApiUser> u = apiUserRepository.findAll(queryParams);
        LOGGER.debug("{}", JsonMapper.toJson(u));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test4() {
        ApiUser u = apiUserRepository.findOneById("1");
        u.setRemarks(String.valueOf(System.currentTimeMillis()));
        apiUserRepository.save(u);
        LOGGER.debug("{}", u.getLoginName());
    }

    /** 分页排序 */
    @Test
    public void test5() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Page<ApiUser> page = apiUserRepository.findByDelFlag("0", PageRequest.of(1, 1, sort));
        LOGGER.debug("{}", page.getTotalElements());
    }
    
    /** 分页排序 */
    @Test
    public void test6() {
        try {
            QueryParams<ApiUser> queryParams = new QueryParams<>();
            queryParams.and(Filter.eq("delFlag", "0"));
            Sort sort = new Sort(Sort.Direction.DESC, "id");
            Page<ApiUser> page = apiUserRepository.findAll(queryParams, PageRequest.of(1, 1, sort));
            LOGGER.debug("{}", page.getTotalElements());
        } catch (Exception e) {
            LOGGER.info("", e);
        }

    }

}
