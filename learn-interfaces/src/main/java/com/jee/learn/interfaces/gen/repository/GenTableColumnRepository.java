package com.jee.learn.interfaces.gen.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    /**
     * 根据genTableId查询
     * 
     * @param genTableId
     * @return
     */
    List<GenTableColumn> findByGenTableId(String genTableId);

    /**
     * 查找主键
     * 
     * @param genTableId
     * @return
     */
    @Query("SELECT a FROM GenTableColumn a WHERE a.delFlag = 0 AND a.isPk = 1")
    List<GenTableColumn> findPrimaryKey(String genTableId);

}
