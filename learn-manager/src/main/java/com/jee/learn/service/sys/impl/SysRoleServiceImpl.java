package com.jee.learn.service.sys.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.common.persist.Condition;
import com.jee.learn.common.persist.Condition.Operator;
import com.jee.learn.common.service.impl.BaseServiceImpl;
import com.jee.learn.common.util.IdGen;
import com.jee.learn.model.sys.SysRole;
import com.jee.learn.model.sys.SysRoleMenu;
import com.jee.learn.model.sys.SysRoleOffice;
import com.jee.learn.model.sys.SysUserRole;
import com.jee.learn.persist.sys.SysRoleRepository;
import com.jee.learn.service.sys.SysRoleService;

@Service
@Transactional(readOnly = true)
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole, String> implements SysRoleService {

    @Autowired
    private IdGen idGen;
    @Autowired
    private SysRoleRepository sysRoleRepository;

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
    public void deleteSysUserRoleBySysRoleId(String id) {
        sysRoleRepository.deleteSysUserRoleBySysRoleId(id);
    }

    @Transactional(readOnly = false)
    @Override
    public void createSysUserRoleBySysRoleId(String id, String sysUserId, String sysRoleId) {
        if (StringUtils.isNoneBlank(id, sysUserId, sysRoleId)) {
            sysRoleRepository.createSysUserRoleBySysRoleId(id, sysUserId, sysRoleId);
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void createSysUserRoleBySysRoleId(SysUserRole sysUserRole) {
        if (sysUserRole != null && sysUserRole.getSysUser() != null && sysUserRole.getSysRole() != null) {
            if (StringUtils.isBlank(sysUserRole.getId())) {
                sysUserRole.setId(idGen.numid());
            }
            sysRoleRepository.createSysUserRoleBySysRoleId(sysUserRole.getId(), sysUserRole.getSysUser().getId(),
                    sysUserRole.getSysRole().getId());
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void deleteSysRoleOfficeBySysRoleId(String id) {
        sysRoleRepository.deleteSysRoleOfficeBySysRoleId(id);
    }

    @Transactional(readOnly = false)
    @Override
    public void createSysRoleOfficeBySysRoleId(String id, String sysRoleId, String sysOfficeId) {
        if (StringUtils.isNoneBlank(id, sysRoleId, sysOfficeId)) {
            sysRoleRepository.createSysUserRoleBySysRoleId(id, sysRoleId, sysOfficeId);
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void createSysRoleOfficeBySysRoleId(SysRoleOffice sysRoleOffice) {
        if (sysRoleOffice != null && sysRoleOffice.getSysRole() != null && sysRoleOffice.getSysOffice() != null) {
            if (StringUtils.isBlank(sysRoleOffice.getId())) {
                sysRoleOffice.setId(idGen.numid());
            }
            sysRoleRepository.createSysRoleOfficeBySysRoleId(sysRoleOffice.getId(), sysRoleOffice.getSysRole().getId(),
                    sysRoleOffice.getSysOffice().getId());
        }
    }

    @Override
    public void deleteSysRoleMenuBySysRoleId(String id) {
        sysRoleRepository.deleteSysRoleMenuBySysRoleId(id);
    }

    @Override
    public void createSysRoleMenuBySysRoleId(String id, String sysRoleId, String sysMenuId) {
        if (StringUtils.isNoneBlank(id, sysRoleId, sysMenuId)) {
            sysRoleRepository.createSysUserRoleBySysRoleId(id, sysRoleId, sysMenuId);
        }
    }

    @Override
    public void createSysRoleMenuBySysRoleId(SysRoleMenu sysRoleMenu) {
        if (sysRoleMenu != null && sysRoleMenu.getSysRole() != null && sysRoleMenu.getSysMenu() != null) {
            if (StringUtils.isBlank(sysRoleMenu.getId())) {
                sysRoleMenu.setId(idGen.numid());
            }
            sysRoleRepository.createSysRoleMenuBySysRoleId(sysRoleMenu.getId(), sysRoleMenu.getSysRole().getId(),
                    sysRoleMenu.getSysMenu().getId());
        }
    }

    @Override
    public Page<SysRole> findPage(SysRole sysRole, int pageNo, int pageSize, String orderBy) {
        PageRequest pageable = new PageRequest(pageNo > 0 ? pageNo - 1 : 0, pageSize, parseSort(orderBy));

        Condition con = new Condition();
        if (sysRole != null) {
            if (StringUtils.isNotBlank(sysRole.getName())) {
                con.add("name", Operator.LIKE, sysRole.getName());
            }
        }
        Page<SysRole> page = super.findAll(con, pageable);

        return page;
    }

}