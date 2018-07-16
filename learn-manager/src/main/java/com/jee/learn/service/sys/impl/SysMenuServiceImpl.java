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
import com.jee.learn.model.sys.SysMenu;
import com.jee.learn.model.sys.SysRoleMenu;
import com.jee.learn.persist.sys.SysMenuRepository;
import com.jee.learn.service.sys.SysMenuService;

@Service
@Transactional(readOnly = true)
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu, String> implements SysMenuService {

    @Autowired
    private IdGen idGen;
    @Autowired
    private SysMenuRepository sysMenuRepository;

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
    public void deleteSysRoleMenuBySysMenuId(String id) {
        sysMenuRepository.deleteSysRoleMenuBySysMenuId(id);
    }

    @Transactional(readOnly = false)
    @Override
    public void createSysRoleMenuBySysMenuId(String id, String sysRoleId, String sysMenuId) {
        if (StringUtils.isNoneBlank(id, sysRoleId, sysRoleId)) {
            sysMenuRepository.createSysRoleMenuBySysMenuId(id, sysRoleId, sysMenuId);
        }
    }

    @Transactional(readOnly = false)
    @Override
    public void createSysRoleMenuBySysMenuId(SysRoleMenu sysRoleMenu) {
        if (sysRoleMenu != null && sysRoleMenu.getSysRole() != null && sysRoleMenu.getSysMenu() != null) {
            if (StringUtils.isBlank(sysRoleMenu.getId())) {
                sysRoleMenu.setId(idGen.numid());
            }
            sysMenuRepository.createSysRoleMenuBySysMenuId(sysRoleMenu.getId(), sysRoleMenu.getSysRole().getId(),
                    sysRoleMenu.getSysMenu().getId());
        }
    }

    @Override
    public List<SysMenu> findByUser(String userId) {
        return sysMenuRepository.findByUser(userId);
    }

}