package com.jee.learn.manager.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.manager.config.datasource.dynamic.TargetDataSource;
import com.jee.learn.manager.domain.sys.SysMenu;
import com.jee.learn.manager.dto.ResponseDto;
import com.jee.learn.manager.dto.sys.MenuDto;
import com.jee.learn.manager.dto.util.TreeUtil;
import com.jee.learn.manager.repository.sys.SysMenuRepository;
import com.jee.learn.manager.security.UserUtil;
import com.jee.learn.manager.service.sys.SysMenuService;
import com.jee.learn.manager.support.cache.CacheConstants;
import com.jee.learn.manager.support.cache.EhcacheService;
import com.jee.learn.manager.support.dao.Condition;
import com.jee.learn.manager.support.dao.Condition.Operator;
import com.jee.learn.manager.support.dao.Sort;
import com.jee.learn.manager.support.dao.service.EntityServiceImpl;
import com.jee.learn.manager.util.WebConstants;

@Service
@Transactional(readOnly = true)
public class SysMenuServiceImpl extends EntityServiceImpl<SysMenu, String> implements SysMenuService {

    @Autowired
    private UserUtil userUtil;
    @Autowired
    private EhcacheService ehcacheService;

    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Override
    protected Condition parseQueryParams(SysMenu entity) {
        Condition con = super.parseQueryParams(entity);
        if (entity == null) {
            return con;
        }

        if (USER_PRIVATE_MENU_TAG.equals(entity.getDelFlag())) {
            con.add("token", Operator.NEEX, entity.getDelFlag());
        }

        return con;
    }

    @Override
    protected Sort parseSort(String orderBy) {
        return super.parseSort(orderBy);
    }

    @TargetDataSource
    @Override
    public List<SysMenu> findListByUserId(String userId) {
        return StringUtils.isBlank(userId) ? new ArrayList<>() : sysMenuRepository.findListByUserId(userId);
    }

    @Transactional(readOnly = false)
    @Override
    public void saveOrUpdate(SysMenu entity) {
        super.saveOrUpdate(entity);
        ehcacheService.remove(CacheConstants.EHCACHE_USER,
                CacheConstants.CACHE_KEY_USER_MENU + userUtil.getUser().getId());
    }

    @TargetDataSource
    @Override
    public ResponseDto<MenuDto> getCurrentUserMenu() {
        ResponseDto<MenuDto> result = new ResponseDto<>();
        result.setC(WebConstants.SUCCESS_CODE);

        List<SysMenu> menuList = userUtil.getMenuList();
        if (CollectionUtils.isEmpty(menuList)) {
            return result;
        }

        List<MenuDto> dtos = new ArrayList<>(menuList.size());
        MenuDto dto = null;
        for (SysMenu menu : menuList) {
            dto = new MenuDto();
            dto.setId(menu.getId());
            dto.setHref(menu.getHref());
            dto.setIcon(menu.getIcon());
            dto.setName(menu.getName());
            dto.setParentId(menu.getParentId());
            dto.setTarget(menu.getTarget());
            dtos.add(dto);
        }

        List<MenuDto> dtoList = TreeUtil.listToTree(dtos);
        result.setD(new MenuDto(dtoList));
        return result;
    }

}
