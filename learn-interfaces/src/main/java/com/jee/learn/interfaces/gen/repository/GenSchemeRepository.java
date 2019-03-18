package com.jee.learn.interfaces.gen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jee.learn.interfaces.gen.domain.GenScheme;

/**
 * GenSchemeRepository
 * 
 * @author ccp, gen
 * @version 2019-03-18 09:49:35
 */
public interface GenSchemeRepository extends JpaRepository<GenScheme, String> {

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

}
