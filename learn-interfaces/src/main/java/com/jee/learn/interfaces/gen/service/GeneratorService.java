package com.jee.learn.interfaces.gen.service;

import java.util.List;
import java.util.Map;

/**
 * 数据库源数据service
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月6日 上午11:29:40 ccp 新建
 */
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

    /**
     * 表名备注解析器
     * 
     * @param tableKey
     * @return
     */
    Map<String, String> analizeTableKey(String tableKey);

}
