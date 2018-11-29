package com.jee.learn.manager.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.manager.config.datasource.dynamic.TargetDataSource;
import com.jee.learn.manager.domain.sys.SysMenu;
import com.jee.learn.manager.dto.sys.MenuDto;
import com.jee.learn.manager.dto.util.TreeUtil;
import com.jee.learn.manager.repository.sys.SysMenuRepository;
import com.jee.learn.manager.security.DictUtil;
import com.jee.learn.manager.security.UserUtil;
import com.jee.learn.manager.service.sys.SysMenuService;
import com.jee.learn.manager.support.cache.CacheConstants;
import com.jee.learn.manager.support.cache.EhcacheService;
import com.jee.learn.manager.support.dao.Condition;
import com.jee.learn.manager.support.dao.Sort;
import com.jee.learn.manager.support.dao.service.EntityServiceImpl;
import com.jee.learn.manager.util.idgen.IdGenerate;
import com.jee.learn.manager.util.mapper.BeanMapper;

@Service
@Transactional(readOnly = true)
public class SysMenuServiceImpl extends EntityServiceImpl<SysMenu, String> implements SysMenuService {

    @Autowired
    private SysMenuRepository sysMenuRepository;

    @Autowired
    private UserUtil userUtil;
    @Autowired
    private DictUtil dictUtil;
    @Autowired
    private EhcacheService ehcacheService;

    @Override
    protected Condition parseQueryParams(SysMenu entity) {
        return super.parseQueryParams(entity);
    }

    @Override
    protected Sort parseSort(String orderBy) {
        return super.parseSort(orderBy);
    }

    @Transactional(readOnly = false)
    @Override
    public void saveOrUpdate(SysMenu entity) {
        if (entity == null) {
            return;
        }
        // 由于使用了bootstrap table, 主键必须由数字组成. 有别于主键的自增长, 这里重写新增与更新的选择
        if (StringUtils.isBlank(entity.getId())) {
            entity.setId(IdGenerate.numid());
            super.getEntityDao().save(entity);
        } else {
            super.getEntityDao().update(entity);
        }
        ehcacheService.remove(CacheConstants.EHCACHE_USER,
                CacheConstants.CACHE_KEY_USER_MENU + userUtil.getUser().getId());
    }

    @TargetDataSource
    @Override
    public List<SysMenu> findListByUserId(String userId) {
        return StringUtils.isBlank(userId) ? new ArrayList<>() : sysMenuRepository.findListByUserId(userId);
    }

    @TargetDataSource
    @Override
    public List<MenuDto> getCurrentUserMenuDtoList() {
        List<SysMenu> list = userUtil.getMenuList();
        List<MenuDto> dtos = new ArrayList<>(list.size());
        for (SysMenu sysMenu : list) {
            MenuDto dto = BeanMapper.map(sysMenu, MenuDto.class);
            dto.setIsShowDict(dictUtil.getLabel("yes_no", sysMenu.getIsShow()));
            dtos.add(dto);
        }
        return dtos;
    }

    @TargetDataSource
    @Override
    public MenuDto getCurrentUserMenuDtoTree() {
        List<MenuDto> dtos = getCurrentUserMenuDtoList();
        dtos = TreeUtil.listToTree(dtos);
        return new MenuDto(dtos);
    }

    @TargetDataSource
    @Override
    public MenuDto findOne(String id) {
        SysMenu entity = super.findOne(id);
        if (entity == null) {
            return null;
        }
        MenuDto dto = BeanMapper.map(entity, MenuDto.class);
        dto.setIsShowDict(dictUtil.getLabel("yes_no", entity.getIsShow()));
        return dto;
    }

}
