package com.jee.learn.interfaces.gen.service;

import java.util.List;

import com.jee.learn.interfaces.gen.dto.GenTableColumnDto;
import com.jee.learn.interfaces.gen.dto.GenTableDto;

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
    List<GenTableDto> selectDataTables();

    /**
     * 获取当前所连接数据库中的指定表信息
     * 
     * @param tableName
     * @return
     */
    List<GenTableDto> selectDataTables(String tableName);

    /**
     * 获取指定表的所有列
     * 
     * @param tableName
     * @return
     */
    List<GenTableColumnDto> selectTableColumn(String tableName);

    /**
     * 获取指定表的主键
     * 
     * @param tableName
     * @return
     */
    List<String> selecePrivateKey(String tableName);

}
