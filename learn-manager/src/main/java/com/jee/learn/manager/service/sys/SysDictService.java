package com.jee.learn.manager.service.sys;

import java.util.List;

import com.jee.learn.manager.domain.sys.SysDict;
import com.jee.learn.manager.support.dao.service.EntityService;

public interface SysDictService extends EntityService<SysDict, String> {

	/**
	 * 根据类型和标签查询一条记录<br/>
	 * 当结果不唯一时抛出异常
	 * 
	 * @param type
	 * @param label
	 * @return
	 */
	SysDict getDictWithLabel(String type, String label);

	/**
	 * 根据类型和值查询一条记录<br/>
	 * 当结果不唯一时抛出异常
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	SysDict getDictWithValue(String type, String value);

	/**
	 * 根据类型查找一类字典
	 * 
	 * @param type
	 * @return
	 */
	List<SysDict> getDictList(String type);

}
