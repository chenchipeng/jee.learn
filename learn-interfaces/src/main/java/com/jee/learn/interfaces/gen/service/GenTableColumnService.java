package com.jee.learn.interfaces.gen.service;

import com.jee.learn.interfaces.gen.domain.GenTableColumn;

/**
 * GenTableColumnService
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月16日 下午5:45:57 ccp 新建
 */
public interface GenTableColumnService {

    /**
     * 根据id查询一条记录
     * 
     * @param id
     * @return
     */
    GenTableColumn findOneById(String id);

    /**
     * 保存一条记录
     * 
     * @param entity
     */
    void save(GenTableColumn entity);

}
