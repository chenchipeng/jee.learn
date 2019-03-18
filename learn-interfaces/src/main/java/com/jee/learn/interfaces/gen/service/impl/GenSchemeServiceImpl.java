package com.jee.learn.interfaces.gen.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.gen.GenConstants;
import com.jee.learn.interfaces.gen.domain.GenScheme;
import com.jee.learn.interfaces.gen.repository.GenSchemeRepository;
import com.jee.learn.interfaces.gen.service.GenSchemeService;
import com.jee.learn.interfaces.util.time.ClockUtil;

/**
 * GenSchemeService
 * 
 * @author ccp, gen
 * @version 2019-03-18 09:49:35
 */
@Service
@Transactional(readOnly = true)
public class GenSchemeServiceImpl implements GenSchemeService {

    @Autowired
    private GenSchemeRepository genSchemeRepository;

    @Override
    public GenScheme findOneById(String id) {
        return StringUtils.isBlank(id) ? null : genSchemeRepository.findOneById(id);
    }

    @Override
    public GenScheme findOneByGenTableId(String genTableId) {
        return StringUtils.isBlank(genTableId) ? null : genSchemeRepository.findOneByGenTableId(genTableId);
    }

    @Transactional(readOnly = false)
    @Override
    public void save(GenScheme entity) {
        entity.setCreateBy(
                StringUtils.isBlank(entity.getCreateBy()) ? GenConstants.SYSTEM_ADMIN_ID : entity.getCreateBy());
        entity.setUpdateBy(
                StringUtils.isBlank(entity.getUpdateBy()) ? GenConstants.SYSTEM_ADMIN_ID : entity.getUpdateBy());

        entity.setCreateDate(StringUtils.isBlank(entity.getId()) ? ClockUtil.currentDate() : entity.getCreateDate());
        entity.setUpdateDate(ClockUtil.currentDate());
        genSchemeRepository.save(entity);
    }

}
