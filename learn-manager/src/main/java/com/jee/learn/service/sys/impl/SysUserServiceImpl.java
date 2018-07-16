package com.jee.learn.service.sys.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.common.persist.Condition;
import com.jee.learn.common.persist.Condition.Operator;
import com.jee.learn.common.service.impl.BaseServiceImpl;
import com.jee.learn.common.util.IdGen;
import com.jee.learn.model.sys.SysUser;
import com.jee.learn.model.sys.SysUserRole;
import com.jee.learn.persist.sys.SysUserRepository;
import com.jee.learn.service.sys.SysUserService;

@Service
@Transactional(readOnly = true)
public class SysUserServiceImpl extends BaseServiceImpl<SysUser, String> implements SysUserService {

    @Autowired
    private IdGen idGen;
    @Autowired
    private SysUserRepository sysUserRepository;

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
    public void deleteSysUserRoleBySysUserId(String id) {
        sysUserRepository.deleteSysUserRoleBySysUserId(id);
    }

    @Transactional(readOnly = false)
    @Override
    public void createSysUserRoleBySysUserId(String id, String sysUserId, String sysRoleId) {
        if (StringUtils.isNoneBlank(id, sysRoleId, sysRoleId)) {
            sysUserRepository.createSysUserRoleBySysUserId(id, sysUserId, sysRoleId);
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void createSysUserRoleBySysUserId(SysUserRole sysUserRole) {
        if (sysUserRole != null && sysUserRole.getSysUser() != null && sysUserRole.getSysRole() != null) {
            if (StringUtils.isBlank(sysUserRole.getId())) {
                sysUserRole.setId(idGen.numid());
            }
            sysUserRepository.createSysUserRoleBySysUserId(sysUserRole.getId(), sysUserRole.getSysUser().getId(),
                    sysUserRole.getSysRole().getId());
        }
    }

    @Override
    public List<SysUser> findAllByNoDel() {
        return sysUserRepository.findAllByNoDel();
    }

    @Override
    public SysUser findByLoginName(String loginName) {
        if (StringUtils.isBlank(loginName)) {
            return null;
        }
        return sysUserRepository.findByLoginName(loginName);
    }

}