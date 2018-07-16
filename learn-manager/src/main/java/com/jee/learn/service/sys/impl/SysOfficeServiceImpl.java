package com.jee.learn.service.sys.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.common.persist.Condition;
import com.jee.learn.common.persist.Condition.Operator;
import com.jee.learn.common.service.impl.BaseServiceImpl;
import com.jee.learn.common.util.IdGen;
import com.jee.learn.model.sys.SysOffice;
import com.jee.learn.model.sys.SysRoleOffice;
import com.jee.learn.persist.sys.SysOfficeRepository;
import com.jee.learn.service.sys.SysOfficeService;

@Service
@Transactional(readOnly = true)
public class SysOfficeServiceImpl extends BaseServiceImpl<SysOffice, String> implements SysOfficeService {

    @Autowired
    private IdGen idGen;
    @Autowired
    private SysOfficeRepository sysOfficeRepository;

    @Override
    protected Condition parseQueryParams(Map<String, String> params) {
        // 该条件默认用在findAll上
        Condition con = new Condition();
        if (StringUtils.isNotBlank(params.get("name"))) {
            con.add("name", Operator.LIKE, params.get("name"));
        }
        return con;
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteSysRoleOfficeBySysOfficeId(String id) {
        sysOfficeRepository.deleteSysRoleOfficeBySysOfficeId(id);
    }

    @Transactional(readOnly = false)
    @Override
    public void createSysRoleOfficeBySysOfficeId(String id, String sysRoleId, String sysOfficeId) {
        if (StringUtils.isNoneBlank(id, sysRoleId, sysRoleId)) {
            sysOfficeRepository.createSysRoleOfficeBySysOfficeId(id, sysRoleId, sysOfficeId);
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void createSysRoleOfficeBySysOfficeId(SysRoleOffice sysRoleOffice) {
        if (sysRoleOffice != null && sysRoleOffice.getSysRole() != null && sysRoleOffice.getSysOffice() != null) {
            if (StringUtils.isBlank(sysRoleOffice.getId())) {
                sysRoleOffice.setId(idGen.numid());
            }
            sysOfficeRepository.createSysRoleOfficeBySysOfficeId(sysRoleOffice.getId(),
                    sysRoleOffice.getSysRole().getId(), sysRoleOffice.getSysOffice().getId());
        }
    }

}