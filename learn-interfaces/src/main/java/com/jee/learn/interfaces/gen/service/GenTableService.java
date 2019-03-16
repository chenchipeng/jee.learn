package com.jee.learn.interfaces.gen.service;

import com.jee.learn.interfaces.gen.domain.GenTable;

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
     * 根据name查询一条记录
     * 
     * @param name
     * @return
     */
    GenTable findOneByName(String name);

    /**
     * 保存一条记录
     * 
     * @param entity
     */
    void save(GenTable entity);

}
