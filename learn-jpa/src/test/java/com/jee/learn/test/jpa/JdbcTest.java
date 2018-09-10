package com.jee.learn.test.jpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.jpa.LearnJpaApplication;
import com.jee.learn.jpa.domain.ApiUser;
import com.jee.learn.jpa.support.jdbc.JdbcDao;
import com.jee.learn.jpa.support.jdbc.RowMapperCustom;
import com.jee.learn.jpa.support.jdbc.RowMapperSupport;

/**
 * JdbcTemplate 测试类
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月10日 下午5:40:09 ccp 新建
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnJpaApplication.class)
public class JdbcTest {

    private static final Logger logger = LoggerFactory.getLogger(JdbcTest.class);

    private static final String SQL_1 = "SELECT * FROM api_user";
    private static final String SQL_2 = "SELECT * FROM api_user WHERE id = ?";

    @Autowired
    private JdbcDao jdbcDao;

    private void printList(List<ApiUser> list) {
        list.forEach(item -> logger.debug("{}", item.getLoginName()));
    }

    private void printMap(List<Map<String, Object>> list) {
        list.forEach(map -> {
            map.keySet().forEach(key -> {
                if (key.contains("name")) {
                    logger.debug("{} -> {}", key, String.valueOf(map.get(key)));
                }
            });
        });
    }

    private void queryRowMapper(RowMapper<ApiUser> rowMapper) {
        List<ApiUser> list = new ArrayList<>();
        list = jdbcDao.query(SQL_1, rowMapper);
        printList(list);

        list = jdbcDao.query(SQL_2, rowMapper, "1");
        printList(list);

        list = jdbcDao.query(SQL_1, rowMapper, 0, 1);
        printList(list);

        list = jdbcDao.query(SQL_2, 1, 1, rowMapper, "1");
        printList(list);
    }

    @Test
    public void countTest() {
        long count = jdbcDao.count(SQL_1);
        logger.debug("{}", count);

        count = jdbcDao.count(SQL_2, "1");
        logger.debug("{}", count);
    }

    @Test
    public void queryTest() {
        List<ApiUser> list = jdbcDao.query(SQL_1, ApiUser.class);
        printList(list);

        list = jdbcDao.query(SQL_2, ApiUser.class, "1");
        printList(list);

        list = jdbcDao.query(SQL_1, ApiUser.class, 1, 1);
        printList(list);

        list = jdbcDao.query(SQL_2, 1, 2, ApiUser.class, "1");
        printList(list);
    }

    @Test
    public void queryRowMapperSupportTest() {

        RowMapper<ApiUser> rowMapper = new RowMapper<ApiUser>() {
            @Override
            public ApiUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                return (ApiUser) new RowMapperSupport(ApiUser.class).mapRow(rs, rowNum);
            }
        };

        queryRowMapper(rowMapper);
    }

    @Test
    public void queryRowMapperCustomTest() {
        queryRowMapper(new RowMapperCustom<ApiUser>() {
        });
    }

    @Test
    public void queryOneTest() {
        ApiUser u = jdbcDao.queryOne(SQL_2, ApiUser.class, "1");
        logger.debug("{}", u.getLoginName());
    }

    @Test
    public void queryObjListTest() {
        List<String> list = jdbcDao.queryObjList("SELECT login_name FROM api_user WHERE id = ?", String.class, "1");
        list.forEach(str -> logger.debug("{}", str));
    }

    @Test
    public void queryForObjectTest() {
        String str = jdbcDao.queryForObject("SELECT login_name FROM api_user WHERE id = ?", String.class, "1");
        logger.debug("{}", str);
    }

    @Test
    public void queryForListTest() {
        List<Map<String, Object>> list = jdbcDao.queryForList(SQL_1);
        printMap(list);

        list = jdbcDao.queryForList(SQL_2, "1");
        printMap(list);

    }

    @Test
    public void queryForPageListTest() {
        List<Map<String, Object>> list = jdbcDao.queryForPageList(SQL_1, 1, 1);
        printMap(list);

        list = jdbcDao.queryForPageList(SQL_2, 0, 1, "1");
        printMap(list);
    }

    @Test
    public void queryForMapTest() {
        List<Map<String, Object>> list = new ArrayList<>();
        list.add(jdbcDao.queryForMap(SQL_1));
        printMap(list);

        list.clear();
        list.add(jdbcDao.queryForMap(SQL_2, "2"));
        printMap(list);
    }

    @Test
    public void updateTest() {
        String sql = "UPDATE api_user SET remarks = '" + System.currentTimeMillis() + "' WHERE id = '2'";
        int effect = jdbcDao.update(sql);
        logger.debug("{}", effect);

        sql = "UPDATE api_user SET remarks = ? WHERE id = ?";
        effect = jdbcDao.update(sql, System.currentTimeMillis(), "1");
        logger.debug("{}", effect);
    }

}
