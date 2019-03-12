package com.jee.learn.interfaces.gen.service;

import java.util.List;

import com.jee.learn.interfaces.gen.domain.GenTable;
import com.jee.learn.interfaces.gen.dto.GenTableDto;

/**
 * GenTableService
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月6日 上午11:30:34 ccp 新建
 */
public interface GenTableService {

    /**
     * 根据id查询一条记录
     * 
     * @param id
     * @return
     */
    GenTable findOneById(String id);

    /**
     * 保存一条记录
     * 
     * @param entity
     */
    void save(GenTable entity);

    //////// 获取数据库元数据 ///////

    /**
     * 获取数据表列表
     * 
     * @return
     */
    List<GenTableDto> selectDataTables();

}
