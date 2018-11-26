package com.jee.learn.manager.service.sys.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.manager.config.datasource.dynamic.TargetDataSource;
import com.jee.learn.manager.domain.sys.SysDict;
import com.jee.learn.manager.repository.sys.SysDictRepository;
import com.jee.learn.manager.service.sys.SysDictService;
import com.jee.learn.manager.support.cache.CacheConstants;
import com.jee.learn.manager.support.cache.EhcacheService;
import com.jee.learn.manager.support.dao.Condition;
import com.jee.learn.manager.support.dao.Sort;
import com.jee.learn.manager.support.dao.service.EntityServiceImpl;
import com.jee.learn.manager.util.idgen.IdGenerate;

@Service
@Transactional(readOnly = true)
public class SysDictServiceImpl extends EntityServiceImpl<SysDict, String> implements SysDictService {

	@Autowired
	private SysDictRepository sysDictRepository;

	@Autowired
	private EhcacheService ehcacheService;

	@Override
	protected Condition parseQueryParams(SysDict entity) {
		return super.parseQueryParams(entity);
	}

	@Override
	protected Sort parseSort(String orderBy) {
		return super.parseSort(orderBy);
	}

	@Override
	public void saveOrUpdate(SysDict entity) {
		if (entity == null) {
			return;
		}
		// 处理SysDict主键非自增的情况
		if (StringUtils.isBlank(entity.getId())) {
			entity.setId(IdGenerate.fastUUID());
			super.getEntityDao().save(entity);
		} else {
			super.getEntityDao().update(entity);
		}
		ehcacheService.remove(CacheConstants.EHCACHE_DEFAULT, CacheConstants.CACHE_KEY_DICT);
	}

	@Override
	@TargetDataSource
	public SysDict getDictWithLabel(String type, String label) {
		if (StringUtils.isAnyBlank(type, label)) {
			return null;
		}
		return sysDictRepository.getDictWithLabel(type, label);
	}

	@Override
	@TargetDataSource
	public SysDict getDictWithValue(String type, String value) {
		if (StringUtils.isAnyBlank(type, value)) {
			return null;
		}
		return sysDictRepository.getDictWithValue(type, value);
	}

	@Override
	@TargetDataSource
	public List<SysDict> getDictList(String type) {
		if (StringUtils.isBlank(type)) {
			return new ArrayList<>(1);
		}
		return sysDictRepository.getDictList(type);
	}

}
