package com.jee.learn.interfaces.gen.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.gen.domain.GenTable;
import com.jee.learn.interfaces.gen.repository.GenTableRepository;
import com.jee.learn.interfaces.gen.service.GenTableService;
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
public class GenTableServiceImpl implements GenTableService {

    @Autowired
    private GenTableRepository genTableRepository;

    @Override
    public GenTable findOneById(String id) {
        return StringUtils.isBlank(id) ? null : genTableRepository.findOneById(id);
    }

    @Override
    @Transactional(readOnly = false)
    public void save(GenTable entity) {
        if (StringUtils.isBlank(entity.getId())) {
            entity.setCreateDate(ClockUtil.currentDate());
        }
        entity.setUpdateDate(ClockUtil.currentDate());
        genTableRepository.save(entity);
    }

}
