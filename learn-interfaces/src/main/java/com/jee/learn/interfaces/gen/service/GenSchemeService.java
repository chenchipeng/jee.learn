package com.jee.learn.interfaces.gen.service;

import com.jee.learn.interfaces.gen.domain.GenScheme;

/**
 * GenSchemeService
 * 
 * @author ccp, gen
 * @version 2019-03-18 09:49:35
 */
public interface GenSchemeService {

    /**
     * 根据id查询一条记录
     * 
     * @param id
     * @return
     */
    GenScheme findOneById(String id);

    /**
     * 根据genTableId查询一条记录
     * 
     * @param genTableId
     * @return
     */
    GenScheme findOneByGenTableId(String genTableId);

    /**
     * 保存一条记录
     * 
     * @param entity
     */
    void save(GenScheme entity);

}
