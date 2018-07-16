package com.jee.learn.common.persist.jdbc;

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * <p>Title: IJdbcDao</p>
 * <p>Description: 常用jdbc操作接口 ,参考{@link JdbcTemplate}</p>
 * <p>Copyright: Copyright (c) 2013 ITDCL  All right reserved.</p>
 * <p>Company: ITDCL</p>
 * @author yjf
 * @version 1.0
 *
 * 修改记录:
 * 下面填写修改的内容以及修改的日期
 * 1.2013-9-10 上午10:56:25  yjf    new
 */
public interface IJdbcDao {

	/**
	 * 批量更新
	 * 
	 * @param sql
	 * @return
	 */
	public int[] batchUpdate(String... sql);

	
	public int[] batchUpdate(String sql,List<Object[]> args);
	/**
	 * 统计数量
	 * 
	 * @param sql
	 * @return
	 */
	public long count(String sql);

	/**
	 * 带预编译参数统计数量
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public long count(String sql, Object... args);

	/**
	 * 返回JdbcTemplate对象，或IJdbcDao列出的所有方法无法满足查询要求，可通过此对象获取更丰富的查询方法
	 * 
	 * @return
	 */
	public JdbcTemplate getJdbcTemplate();

	/**
	 * 检索记录，并映射成指定类型pojo对象
	 * 
	 * @param sql
	 * @param clazz
	 * @return
	 */
	public <T> List<T> query(String sql, Class<T> clazz);
	
	public <T> List<T> queryObjList(String sql, Class<T> clazz, Object... args);
	/**
	 * 检索记录，并映射成指定类型pojo对象-分页
	 * 
	 * @param sql
	 * @param clazz
	 * @param offset
	 * @param limit
	 * @return
	 */
	public <T> List<T> query(String sql, Class<T> clazz, int offset, int limit);

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
	public <T> List<T> query(String sql, Class<T> clazz, int offset, int limit, Object... args);

	/**
	 * 带预编译参数检索记录，并映射成指定类型的pojo对象
	 * 
	 * @param sql
	 * @param clazz
	 * @param args
	 * @return
	 */
	public <T> List<T> query(String sql, Class<T> clazz, Object... args);

	/**
	 * 检索记录并映射成对象
	 * 
	 * @param sql
	 * @param rowMapper
	 * @return
	 */
	public <T> List<T> query(String sql, RowMapper<T> rowMapper);

	/**
	 * 检索记录并映射成对象-分页
	 * 
	 * @param sql
	 * @param rowMapper
	 * @param offset
	 * @param limit
	 * @return
	 */
	public <T> List<T> query(String sql, RowMapper<T> rowMapper, int offset, int limit);

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
	public <T> List<T> query(String sql, RowMapper<T> rowMapper, int offset, int limit, Object... args);

	/**
	 * 带预编译参数检索记录并映射成对象
	 * 
	 * @param sql
	 * @param rowMapper
	 * @param args
	 * @return
	 */
	public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args);

	
	public <T> T queryForObject(String sql,Class<T> clazz, Object... args);
	/**
	 * 检索记录
	 * 
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql);

	/**
	 * 检索记录-分页
	 * 
	 * @param sql
	 * @param offset
	 * @param limit
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, int offset, int limit);

	/**
	 * 带预编译参数检索记录-分页
	 * 
	 * @param sql
	 * @param offset
	 * @param limit
	 * @param args
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, int offset, int limit, Object... args);

	/**
	 * 带预编译参数检索记录
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public List<Map<String, Object>> queryForList(String sql, Object... args);

	/**
	 * 检索记录，返回唯一记录
	 * 
	 * @param sql
	 * @return
	 */
	public Map<String, Object> queryForMap(String sql);

	/**
	 * 带预编译参数检索记录，返回唯一记录
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public Map<String, Object> queryForMap(String sql, Object... args);

	/**
	 * 增删改记录
	 * 
	 * @param sql
	 * @return
	 */
	public int update(String sql);

	/**
	 * 带预编译参数增删改记录
	 * 
	 * @param sql
	 * @param args
	 * @return
	 */
	public int update(String sql, Object... args);
	
	/**
	 * 获取数据库相关信息
	 * @return
	 */
	public DatabaseMetaData getDatabaseMetaData();

}
