package com.jee.learn.interfaces.gen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jee.learn.interfaces.gen.domain.GenTable;

/**
 * GenTableRepository
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月6日 上午11:27:06 ccp 新建
 */
public interface GenTableRepository extends JpaRepository<GenTable, String> {

    /**
     * 根据id查询一条记录
     * 
     * @param id
     * @return
     */
    GenTable findOneById(String id);

}
