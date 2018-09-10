package com.jee.learn.test.jpa;

import java.util.List;
import java.util.Map;
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
import com.jee.learn.jpa.repository.BaseRepository;
import com.jee.learn.jpa.repository.api.ApiUserRepository;
import com.jee.learn.jpa.repository.dao.Condition;
import com.jee.learn.jpa.repository.dao.Condition.Operator;
import com.jee.learn.jpa.repository.dao.EntityDao;
import com.jee.learn.jpa.support.jdbc.JdbcDao;
import com.jee.learn.jpa.support.spec.Filter;
import com.jee.learn.jpa.support.spec.QueryParams;
import com.jee.learn.jpa.util.mapper.JsonMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnJpaApplication.class)
public class EntityRepositoryTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityRepositoryTest.class);

    @Autowired
    private BaseRepository<ApiUser, String> baseRepository;
    @Autowired
    private ApiUserRepository apiUserRepository;
    @Autowired
    private EntityDao entityDao;
    @Autowired
    private JdbcDao jdbcDao;

    @Test
    public void findOneByIdTest() {
        ApiUser u = apiUserRepository.findOneById("1");
        LOGGER.debug("{}", u.getLoginName());
    }

    @Test
    public void specFindOneTest() {

        QueryParams<ApiUser> queryParams = new QueryParams<>();
        // 使用Specification条件查询,使用JPQL字段查询
        queryParams.and(Filter.eq("id", "1"));
        Optional<ApiUser> u = apiUserRepository.findOne(queryParams);
        LOGGER.debug("{}", JsonMapper.toJson(u.get()));
    }

    @Test
    public void specFindAllTest() {

        QueryParams<ApiUser> queryParams = new QueryParams<>();
        queryParams.and(Filter.eq("delFlag", "0"));

        List<ApiUser> u = apiUserRepository.findAll(queryParams);
        LOGGER.debug("{}", JsonMapper.toJson(u));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void saveTest() {
        ApiUser u = apiUserRepository.findOneById("1");
        u.setRemarks(String.valueOf(System.currentTimeMillis()));
        apiUserRepository.save(u);
        LOGGER.debug("{}", u.getLoginName());
    }

    /** 分页排序 */
    @Test
    public void pageTest() {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Page<ApiUser> page = apiUserRepository.findByDelFlag("0", PageRequest.of(1, 1, sort));
        LOGGER.debug("{}", page.getTotalElements());
    }

    /** 分页排序 */
    @Test
    public void specPageTest() {
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

    @Test
    public void findOneByIdNullTest() {
        ApiUser u = apiUserRepository.findOneById("3");
        LOGGER.debug("{}", JsonMapper.toJson(u));
    }

    @Test
    public void entityDaoFindTest() {
        ApiUser u = entityDao.findOne(ApiUser.class, "1");
        LOGGER.debug("{}", JsonMapper.toJson(u));

        Condition condition = new Condition();
        condition.add("id", Operator.EQ, "3");
        u = entityDao.findOne(ApiUser.class, condition);
        LOGGER.debug("{}", JsonMapper.toJson(u));
        List<ApiUser> list = entityDao.find(ApiUser.class, condition);
        LOGGER.debug("{}", JsonMapper.toJson(list));
    }

    @Test
    public void baseRepositoryTest() {
        ApiUser u = baseRepository.findById("12").orElseGet(()->{return null;});
        LOGGER.debug("{}", u == null);
        LOGGER.debug("{}", JsonMapper.toJson(u));
    }

    @Test
    public void queryTest() {
        List<ApiUser> list = jdbcDao.query("select * from api_user where del_flag = ?", ApiUser.class, "0");
        LOGGER.debug("{}", JsonMapper.toJson(list));
    }

    @Test
    public void queryForListTest() {
        List<Map<String, Object>> list = jdbcDao.queryForList("select * from api_user where del_flag = '0' ");
        LOGGER.debug("{}", JsonMapper.toJson(list));
    }

    @Test
    public void queryObjListTest() {
        try {
            // 没有使用RowMapper将结果集映射成对象，因此只能查询具体的某一列，而且传入的clazz要与所查询的字段的类型对应得上
            List<String> list = jdbcDao.queryObjList("select id from api_user where del_flag = ?", String.class, "0");
            LOGGER.debug("{}", JsonMapper.toJson(list));
        } catch (Exception e) {
            LOGGER.info("", e);
        }

    }

    @Test
    public void queryOneTest() {
        // 查询一个对象，能成功进行映射
        ApiUser u = jdbcDao.queryOne("select * from api_user where id = ?", ApiUser.class, "1");
        LOGGER.debug("{}", JsonMapper.toJson(u));
    }

    @Test
    public void queryForObjectTest() {
        try {
            // 没有使用RowMapper将结果集映射成对象，因此只能查询具体的某一列的第一条，而且传入的clazz要与所查询的字段的类型对应得上
            String u = jdbcDao.queryForObject("select login_name from api_user where id = ?", String.class, "1");
            LOGGER.debug("{}", JsonMapper.toJson(u));
        } catch (Exception e) {
            LOGGER.info("", e);
        }
    }

    @Test
    public void findByIdTest() {
        Optional<ApiUser> u = apiUserRepository.findById("1");
        LOGGER.debug("{}", JsonMapper.toJson(u.isPresent()));
    }

}
