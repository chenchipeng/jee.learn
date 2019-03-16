package com.jee.learn.interfaces.gen.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jee.learn.interfaces.gen.domain.GenTableColumn;

/**
 * GenTableColumnRepository
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月16日 下午5:48:19 ccp 新建
 */
public interface GenTableColumnRepository extends JpaRepository<GenTableColumn, String> {

    /**
     * 根据id查询一条记录
     * 
     * @param id
     * @return
     */
    GenTableColumn findOneById(String id);

}
