package com.jee.learn.manager.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.manager.config.datasource.dynamic.TargetDataSource;
import com.jee.learn.manager.domain.sys.SysRole;
import com.jee.learn.manager.repository.sys.SysRoleRepository;
import com.jee.learn.manager.security.UserUtil;
import com.jee.learn.manager.service.sys.SysRoleService;
import com.jee.learn.manager.support.cache.CacheConstants;
import com.jee.learn.manager.support.cache.EhcacheService;
import com.jee.learn.manager.support.dao.Condition;
import com.jee.learn.manager.support.dao.Sort;
import com.jee.learn.manager.support.dao.service.EntityServiceImpl;
import com.jee.learn.manager.util.idgen.IdGenerate;

@Service
@Transactional(readOnly = true)
public class SysRoleServiceImpl extends EntityServiceImpl<SysRole, String> implements SysRoleService {

	@Autowired
	private UserUtil userUtil;
	@Autowired
	private EhcacheService ehcacheService;

	@Autowired
	private SysRoleRepository sysRoleRepository;

	@Override
	protected Condition parseQueryParams(SysRole entity) {
		return super.parseQueryParams(entity);
	}

	@Override
	protected Sort parseSort(String orderBy) {
		return super.parseSort(orderBy);
	}

	@TargetDataSource
	@Override
	public List<SysRole> findListByUserId(String userId) {
		return StringUtils.isBlank(userId) ? new ArrayList<>() : sysRoleRepository.findListByUserId(userId);
	}

	@Transactional(readOnly = false)
	@Override
	public void saveOrUpdate(SysRole entity) {
		if (entity == null) {
			return;
		}
		// 处理SysLog主键非自增的情况
		if (StringUtils.isBlank(entity.getId())) {
			entity.setId(IdGenerate.fastUUID());
			super.getEntityDao().save(entity);
		} else {
			super.getEntityDao().update(entity);
		}
		ehcacheService.remove(CacheConstants.EHCACHE_USER, CacheConstants.CACHE_KEY_USER_ROLE + userUtil.getUser().getId());
	}

}
