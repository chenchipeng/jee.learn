package com.jee.learn.manager.support.jdbc;

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
import org.springframework.stereotype.Component;

/**
 * 
 * <p>
 * Title: JdbcDaoImpl
 * </p>
 * <p>
 * Description: 接口{@link JdbcDao}实现类
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
 *          修改记录: 下面填写修改的内容以及修改的日期<br/>
 *          1.2013-9-10 上午11:48:24 yjf new
 */
@Component
public class JdbcDaoImpl implements JdbcDao {

    private final static Logger logger = LoggerFactory.getLogger(JdbcDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    @Override
    public int[] batchUpdate(String... sql) {
        return jdbcTemplate.batchUpdate(sql);
    }

    @Override
    public long count(String sql) {
        Long count = queryForObject("SELECT COUNT(*) FROM ( " + sql + ") exm", Long.class);
        return count == null ? 0 : count;
    }

    @Override
    public long count(String sql, Object... args) {
        Long count = queryForObject("SELECT COUNT(*) FROM ( " + sql + ") exm", Long.class, args);
        return count == null ? 0 : count;
    }

    @Override
    public <T> List<T> query(String sql, Class<T> clazz) {
        return query(sql, createRowMapper(clazz));
    }

    @Override
    public <T> List<T> query(String sql, Class<T> clazz, Object... args) {
        return query(sql, createRowMapper(clazz), args);
    }

    @Override
    public <T> List<T> query(String sql, Class<T> clazz, int offset, int limit) {
        return query(generateLimitSql(sql, offset, limit), clazz);
    }

    @Override
    public <T> List<T> query(String sql, int offset, int limit, Class<T> clazz, Object... args) {
        return query(generateLimitSql(sql, offset, limit), createRowMapper(clazz), args);
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        logger.debug("JdbcTemplate:\n{}", sql);
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
        logger.debug("JdbcTemplate:\n{}\nargs: {}", sql, Arrays.toString(args));
        return jdbcTemplate.query(sql, rowMapper, args);
    }

    @Override
    public <T> List<T> query(String sql, RowMapper<T> rowMapper, int offset, int limit) {
        return query(generateLimitSql(sql, offset, limit), rowMapper);
    }

    @Override
    public <T> List<T> query(String sql, int offset, int limit, RowMapper<T> rowMapper, Object... args) {
        return query(generateLimitSql(sql, offset, limit), rowMapper, args);
    }

    @Override
    public <T> T queryOne(String sql, Class<T> clazz, Object... args) {
        List<T> list = query(sql, clazz, args);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public <T> List<T> queryObjList(String sql, Class<T> clazz, Object... args) {
        logger.debug("JdbcTemplate:\n{}\nargs: {}", sql, Arrays.toString(args));
        if (args == null || args.length < 1) {
            return jdbcTemplate.queryForList(sql, clazz);
        } else {
            return jdbcTemplate.queryForList(sql, clazz, args);
        }
    }

    @SuppressWarnings("unchecked")
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
        logger.debug("JdbcTemplate:\n{}", sql);
        return jdbcTemplate.queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> queryForList(String sql, Object... args) {
        logger.debug("JdbcTemplate:\n{}\nargs: {}", sql, Arrays.toString(args));
        return jdbcTemplate.queryForList(sql, args);
    }

    @Override
    public List<Map<String, Object>> queryForPageList(String sql, int offset, int limit) {
        return queryForList(generateLimitSql(sql, offset, limit));
    }

    @Override
    public List<Map<String, Object>> queryForPageList(String sql, int offset, int limit, Object... args) {
        return queryForList(generateLimitSql(sql, offset, limit), args);
    }

    @Override
    public Map<String, Object> queryForMap(String sql) {
        List<Map<String, Object>> list = queryForList(sql);
        Map<String, Object> map = null;
        if (list != null && list.size() > 0) {
            map = list.get(0);
        }
        return map;
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
        logger.debug("JdbcTemplate:\n{}", sql);
        return jdbcTemplate.update(sql);
    }

    @Override
    public int update(String sql, Object... args) {
        logger.debug("JdbcTemplate:\n{}\nargs: {}", sql, Arrays.toString(args));
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

    /**
     * 拼接分页sql<br/>
     * 该方法暂不支持 SqlServer 与 DB2
     * 
     * @param sql
     * @param offset
     * @param limit
     * @return
     */
    private String generateLimitSql(String sql, int offset, int limit) {
        String dbType = null;
        try {
            dbType = getDatabaseMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        StringBuilder builder = new StringBuilder();
        if (dbType.equalsIgnoreCase("MySQL")) {
            // MYSQL分页
            builder.append("SELECT exm.* FROM( ");
            builder.append(sql);
            builder.append(" )exm LIMIT ").append(offset).append(" , ").append(limit);

        } else if (dbType.equalsIgnoreCase("Oracle")) {
            // ORACLE分页
            builder.append("SELECT * FROM  (SELECT A.*,ROWNUM RN FROM (");
            builder.append(sql).append(") A WHERE ROWNUM <=");
            builder.append(offset + limit).append(") WHERE RN >").append(offset);

        } else if (dbType.equalsIgnoreCase("sqlServer")) {
            // SQLSERVICE数据库

        } else if (dbType.equalsIgnoreCase("DB2")) {
            // DB2数据库
        }

        // logger.debug("JdbcTemplate 分页sql:{}\n", builder.toString());
        return builder.toString();
    }

    /**
     * 创建数据解析器
     * 
     * @param clazz
     * @return
     */
    private <T> RowMapper<T> createRowMapper(Class<T> clazz) {
        return new RowMapper<T>() {
            @SuppressWarnings("unchecked")
            @Override
            public T mapRow(ResultSet rs, int rowNum) throws SQLException {
                return (T) new RowMapperSupport(clazz).mapRow(rs, rowNum);
            }
        };
    }

    /**
     * 创建数据解析器<br/>
     * {@link RowMapperSupport} 与 {@link RowMapperCustom}
     * 类似。前者需要显式指定构造方法的参数，后者则通过泛型指定，功能上基本一致
     * 
     * @return
     */
    @SuppressWarnings("unused")
    private <T> RowMapper<T> createRowMapper() {
        return new RowMapperCustom<T>() {
        };
    }

}
