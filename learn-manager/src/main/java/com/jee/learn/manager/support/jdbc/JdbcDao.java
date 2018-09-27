package com.jee.learn.manager.support.jdbc;

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * <p>
 * Title: JdbcDao
 * </p>
 * <p>
 * Description: 常用jdbc操作接口 ,参考{@link JdbcTemplate}
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013 ITDCL All right reserved.
 * </p>
 * <p>
 * Company: ITDCL
 * </p>
 * 在使用分页功能时，要注意数据库的类型。SQL SERVER与DB2暂不支持分页。
 * 
 * @author yjf
 * @version 1.0
 *
 *          修改记录: 下面填写修改的内容以及修改的日期<br/>
 *          1.2013-9-10 上午10:56:25 yjf new<br/>
 *          2.2018年9月10日 下午1:58:29 ccp 增加备注
 */
public interface JdbcDao {

    /**
     * 返回JdbcTemplate对象，或IJdbcDao列出的所有方法无法满足查询要求，可通过此对象获取更丰富的查询方法
     * 
     * @return
     */
    JdbcTemplate getJdbcTemplate();

    /**
     * 批量更新
     * 
     * @param sql
     * @return
     */
    int[] batchUpdate(String... sql);

    /**
     * 统计数量
     * 
     * @param sql
     * @return
     */
    long count(String sql);

    /**
     * 带预编译参数统计数量
     * 
     * @param sql
     * @param args
     * @return
     */
    long count(String sql, Object... args);

    /**
     * 检索记录，并映射成指定类型pojo对象
     * 
     * @param sql
     * @param clazz
     * @return
     */
    <T> List<T> query(String sql, Class<T> clazz);

    /**
     * 带预编译参数检索记录，并映射成指定类型的pojo对象
     * 
     * @param sql
     * @param clazz
     * @param args
     * @return
     */
    <T> List<T> query(String sql, Class<T> clazz, Object... args);

    /**
     * 检索记录，并映射成指定类型pojo对象-分页
     * 
     * @param sql
     * @param clazz
     * @param offset
     * @param limit
     * @return
     */
    <T> List<T> query(String sql, Class<T> clazz, int offset, int limit);

    /**
     * 带预编译参数检索记录，并映射成指定类型pojo对象-分页
     * 
     * @param sql
     * @param clazz
     * @param offset
     * @param limit
     * @param args
     * @return
     */
    <T> List<T> query(String sql, int offset, int limit, Class<T> clazz, Object... args);

    /**
     * 检索记录并映射成对象
     * 
     * @param sql
     * @param rowMapper
     * @return
     */
    <T> List<T> query(String sql, RowMapper<T> rowMapper);

    /**
     * 带预编译参数检索记录并映射成对象
     * 
     * @param sql
     * @param rowMapper
     * @param args
     * @return
     */
    <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args);

    /**
     * 检索记录并映射成对象-分页
     * 
     * @param sql
     * @param rowMapper
     * @param offset
     * @param limit
     * @return
     */
    <T> List<T> query(String sql, RowMapper<T> rowMapper, int offset, int limit);

    /**
     * 带预编译参数检索记录并映射成对象-分页
     * 
     * @param sql
     * @param rowMapper
     * @param offset
     * @param limit
     * @param args
     * @return
     */
    <T> List<T> query(String sql, int offset, int limit, RowMapper<T> rowMapper, Object... args);

    /**
     * 带预编译参数检索一条记录并映射成对象
     * 
     * @param sql
     * @param clazz
     * @param args
     * @return
     */
    <T> T queryOne(String sql, Class<T> clazz, Object... args);

    /**
     * 带预编译参数检索一列并映射成该列所对应的类型对象(基本数据类型)<br/>
     * 简单来说，就是只能获取结果集中的某一列
     * 
     * @param sql
     * @param clazz
     * @param args
     * @return
     */
    <T> List<T> queryObjList(String sql, Class<T> clazz, Object... args);

    /**
     * 带预编译参数检索一列的第一条记录并映射成该记录所对应的类型对象(基本数据类型)<br/>
     * 简单来说，就是只能获取结果集中的某一列的第一个字段
     * 
     * @param sql
     * @param clazz
     * @param args
     * @return
     */
    <T> T queryForObject(String sql, Class<T> clazz, Object... args);

    /**
     * 检索记录
     * 
     * @param sql
     * @return
     */
    List<Map<String, Object>> queryForList(String sql);

    /**
     * 带预编译参数检索记录
     * 
     * @param sql
     * @param args
     * @return
     */
    List<Map<String, Object>> queryForList(String sql, Object... args);

    /**
     * 检索记录-分页
     * 
     * @param sql
     * @param offset
     * @param limit
     * @return
     */
    List<Map<String, Object>> queryForPageList(String sql, int offset, int limit);

    /**
     * 带预编译参数检索记录-分页
     * 
     * @param sql
     * @param offset
     * @param limit
     * @param args
     * @return
     */
    List<Map<String, Object>> queryForPageList(String sql, int offset, int limit, Object... args);

    /**
     * 检索记录，返回唯一记录(结果集的第一条)
     * 
     * @param sql
     * @return
     */
    Map<String, Object> queryForMap(String sql);

    /**
     * 带预编译参数检索记录，返回唯一记录(结果集的第一条)
     * 
     * @param sql
     * @param args
     * @return
     */
    Map<String, Object> queryForMap(String sql, Object... args);

    /**
     * 增删改记录
     * 
     * @param sql
     * @return
     */
    int update(String sql);

    /**
     * 带预编译参数增删改记录
     * 
     * @param sql
     * @param args
     * @return
     */
    int update(String sql, Object... args);

    /**
     * 获取数据库相关信息
     * 
     * @return
     */
    DatabaseMetaData getDatabaseMetaData();

}
