package com.jee.learn.interfaces.gen.service;

import java.util.List;

public interface GeneratorService {

	/**
	 * 获取当前所连接数据库中的所有表
	 * 
	 * @return
	 */
	List<String> selectDataTables();

	/**
	 * 获取指定表的所有列
	 * 
	 * @param tableKey 表名+":"+注释
	 */
	void selectTableColumn(String tableKey);

}
