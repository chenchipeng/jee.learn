package com.jee.learn.interfaces.gen.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.gen.GenConstants;
import com.jee.learn.interfaces.gen.domain.GenTableColumn;
import com.jee.learn.interfaces.gen.repository.GenTableColumnRepository;
import com.jee.learn.interfaces.gen.service.GenTableColumnService;
import com.jee.learn.interfaces.util.time.ClockUtil;

/**
 * GenTableService
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月6日 上午11:30:17 ccp 新建
 */
@Service
@Transactional(readOnly = true)
public class GenTableColumnServiceImpl implements GenTableColumnService {

    @Autowired
    private GenTableColumnRepository genTableColumnRepository;

    @Override
    public GenTableColumn findOneById(String id) {
        return StringUtils.isBlank(id) ? null : genTableColumnRepository.findOneById(id);
    }

    @Override
    public List<GenTableColumn> findByGenTableId(String genTableId) {
        if (StringUtils.isBlank(genTableId)) {
            return new ArrayList<GenTableColumn>(1);
        }
        return genTableColumnRepository.findByGenTableId(genTableId);
    }
    
    @Override
    public GenTableColumn findOneByGenTableIdAndName(String genTableId,String name) {
        if (StringUtils.isAnyBlank(genTableId,name)) {
            return null;
        }
        return genTableColumnRepository.findOneByGenTableIdAndName(genTableId,name);
    }

    @Transactional(readOnly = false)
    @Override
    public void save(GenTableColumn entity) {
        entity.setCreateBy(
                StringUtils.isBlank(entity.getCreateBy()) ? GenConstants.SYSTEM_ADMIN_ID : entity.getCreateBy());
        entity.setUpdateBy(
                StringUtils.isBlank(entity.getUpdateBy()) ? GenConstants.SYSTEM_ADMIN_ID : entity.getUpdateBy());

        entity.setCreateDate(StringUtils.isBlank(entity.getId()) ? ClockUtil.currentDate() : entity.getCreateDate());
        entity.setUpdateDate(ClockUtil.currentDate());
        genTableColumnRepository.save(entity);
    }

    @Override
    public List<GenTableColumn> findPrimaryKey(String genTableId) {
        if (StringUtils.isBlank(genTableId)) {
            return new ArrayList<GenTableColumn>(1);
        }
        return genTableColumnRepository.findPrimaryKey(genTableId);
    }

}
