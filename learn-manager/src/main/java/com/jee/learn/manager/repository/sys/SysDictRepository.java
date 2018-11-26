package com.jee.learn.manager.repository.sys;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jee.learn.manager.config.datasource.dynamic.TargetDataSource;
import com.jee.learn.manager.domain.sys.SysDict;
import com.jee.learn.manager.support.spec.repository.SpecRepository;

public interface SysDictRepository extends SpecRepository<SysDict, String> {

	/**
	 * 根据类型和标签查询一条记录<br/>
	 * 当结果不唯一时抛出异常
	 * 
	 * @param type
	 * @param label
	 * @return
	 */
	@TargetDataSource
	@Query(value = "SELECT * FROM sys_dict WHERE type =:type AND label =:label AND del_flag = '0'", nativeQuery = true)
	SysDict getDictWithLabel(@Param("type") String type, @Param("label") String label);

	/**
	 * 根据类型和值查询一条记录<br/>
	 * 当结果不唯一时抛出异常
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	@TargetDataSource
	@Query(value = "SELECT * FROM sys_dict WHERE type =:type AND `value` =:value AND del_flag = '0'", nativeQuery = true)
	SysDict getDictWithValue(@Param("type") String type, @Param("value") String value);

	/**
	 * 根据类型查找一类字典
	 * 
	 * @param type
	 * @return
	 */
	@TargetDataSource
	@Query(value = "SELECT * FROM sys_dict WHERE type =:type AND del_flag = '0'", nativeQuery = true)
	List<SysDict> getDictList(@Param("type") String type);

}
