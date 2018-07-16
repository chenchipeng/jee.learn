package com.jee.learn.common.persist.jdbc.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import com.jee.learn.common.persist.jdbc.IJdbcDao;

/**
 * 
 * <p>
 * Title: JdbcDaoImpl
 * </p>
 * <p>
 * Description: 接口{@link IJdbcDao}实现类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013 ITDCL All right reserved.
 * </p>
 * <p>
 * Company: ITDCL
 * </p>
 * 
 * @author yjf
 * @version 1.0
 *
 *          修改记录: 下面填写修改的内容以及修改的日期 1.2013-9-10 上午11:48:24 yjf new
 */
@Repository
public class JdbcDaoImpl implements IJdbcDao {

    private static Logger logger = LoggerFactory.getLogger(JdbcDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int[] batchUpdate(String... sql) {
        return jdbcTemplate.batchUpdate(sql);
    }

    @Override
    public int[] batchUpdate(String sql, List<Object[]> args) {
        return jdbcTemplate.batchUpdate(sql, args);
    }

    @Override
    public long count(String sql) {
        Long count = queryForObject(sql, Long.class);
        return count == null ? 0 : count;
    }

    @Override
    public long count(String sql, Object... args) {
        Long count = queryForObject(sql, Long.class, args);
        return count == null ? 0 : count;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public <T> List<T> query(String sql, Class<T> clazz) {
        return query(sql, createRowMapper(clazz));
    }

    @Override
    public <T> List<T> queryObjList(String sql, Class<T> clazz, Object... args) {
        if (args == null || args.length < 1) {
            return jdbcTemplate.queryForList(sql, clazz);
        } else {
            return jdbcTemplate.queryForList(sql, clazz, args);
        }
    }

    private <T> RowMapper<T> createRowMapper(final Class<T> clazz) {
        return new RowMapper<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                return (T) new PojoRowMapperSurport(clazz).mapRow(rs, rowNum);
            }
        };
    }

    @Override
    public <T> List<T> query(String sql, Class<T> clazz, int offset, int limit) {
        return query(generateLimitSql(sql, offset, limit), clazz);
    }

    @Override
    public <T> List<T> query(String sql, Class<T> clazz, int offset, int limit, Object... args) {
        return query(generateLimitSql(sql, offset, limit), createRowMapper(clazz), args);
    }

    @Override
    public <T> List<T> query(String sql, Class<T> clazz, Object... args) {
        return query(sql, createRowMapper(clazz), args);
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, int offset, int limit) {
        return query(generateLimitSql(sql, offset, limit), rowMapper);
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, int offset, int limit, Object... args) {
        return query(generateLimitSql(sql, offset, limit), rowMapper, args);
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
        return jdbcTemplate.query(sql, rowMapper, args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T queryForObject(String sql, Class<T> clazz, Object... args) {
        List<Map<String, Object>> list = queryForList(sql, args);
        if (list != null && !list.isEmpty()) {
            if (!list.get(0).keySet().isEmpty()) {
                return (T) list.get(0).get(list.get(0).keySet().toArray()[0]);
            }
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql) {
        logger.debug(sql);
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, int offset, int limit) {
        return queryForList(generateLimitSql(sql, offset, limit));
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, int offset, int limit, Object... args) {
        return queryForList(generateLimitSql(sql, offset, limit), args);
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Object... args) {
        logger.debug("sql=【" + sql + "】" + "args【" + Arrays.toString(args) + "】");
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public Map<String, Object> queryForMap(String sql) {
        // return queryForMap(sql,null);
        return queryForMap(sql, (Object[]) null);
    }

    @Override
    public Map<String, Object> queryForMap(String sql, Object... args) {
        List<Map<String, Object>> list = queryForList(sql, args);
        Map<String, Object> map = null;
        if (list != null && list.size() > 0) {
            map = list.get(0);
        }
        return map;
    }

    @Override
    public int update(String sql) {
        logger.debug("sql=【" + sql + "】");
        return jdbcTemplate.update(sql);
    }

    @Override
    public int update(String sql, Object... args) {
        logger.debug("sql=【" + sql + "】" + "args【" + Arrays.toString(args) + "】");
        return jdbcTemplate.update(sql, args);
    }

    @Override
    public DatabaseMetaData getDatabaseMetaData() {
        Connection connection = DataSourceUtils.getConnection(jdbcTemplate.getDataSource());
        try {
            return connection.getMetaData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DataSourceUtils.releaseConnection(connection, jdbcTemplate.getDataSource());
        }
    }

    /*
     * 拼接分页sql
     */
    private String generateLimitSql(String sql, int offset, int limit) {
        String dbType = null;
        try {
            dbType = getDatabaseMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        StringBuilder builder = new StringBuilder();
        if (dbType.equalsIgnoreCase("MySQL")) {// MYSQL分页
            builder.append("SELECT Abcxd.* FROM( ");
            builder.append(sql);
            builder.append(" )Abcxd limit ").append(offset).append(" , ").append(limit);

        } else if (dbType.equalsIgnoreCase("Oracle")) { // ORACLE分页
            builder.append("SELECT * FROM  (SELECT A.*,ROWNUM RN FROM (");
            builder.append(sql).append(") A WHERE ROWNUM <=");
            builder.append(offset + limit).append(") WHERE RN >").append(offset);

            // } else if (dbType.equalsIgnoreCase("sqlServer")) {//
            // SQLSERVICE数据库
            //
            // } else if (dbType.equalsIgnoreCase("DB2")) {// DB2数据库

        }
        logger.debug("QUERY 分页sql : " + builder.toString());

        return builder.toString();
    }

}
