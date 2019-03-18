package com.jee.learn.interfaces.gen.service;

import java.util.List;

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
     * 根据genTableId查询
     * 
     * @param genTableId
     * @return
     */
    List<GenTableColumn> findByGenTableId(String genTableId);

    /**
     * 保存一条记录
     * 
     * @param entity
     */
    void save(GenTableColumn entity);
    
    /**
     * 查找主键
     * 
     * @param genTableId
     * @return
     */
    List<GenTableColumn> findPrimaryKey(String genTableId);

}
