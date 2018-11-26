package com.jee.learn.manager.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jee.learn.manager.domain.sys.SysDict;
import com.jee.learn.manager.service.sys.SysDictService;
import com.jee.learn.manager.support.cache.CacheConstants;
import com.jee.learn.manager.support.cache.EhcacheService;

/**
 * 系统字典查找工具
 * 
 * @author admin
 *
 */
@Component
public class DictUtil {

	@Autowired
	private EhcacheService ehcacheService;
	@Autowired
	private SysDictService sysDictService;

	/** 根据类型和值查找对应的标签 */
	public String getLabel(String type, String value) {
		if (StringUtils.isAnyBlank(type, value)) {
			return null;
		}
		String label = StringUtils.EMPTY;
		List<SysDict> list = getDictList(type);
		for (SysDict sysDict : list) {
			if (value.equals(sysDict.getValue())) {
				label = sysDict.getLabel();
				break;
			}
		}
		if (StringUtils.isBlank(label)) {
			SysDict dict = sysDictService.getDictWithValue(type, value);
			if (dict != null) {
				label = dict.getLabel();
			}
		}
		return label;
	}

	/** 根据类型和标签查找对应的值 */
	public String getValue(String type, String label) {
		if (StringUtils.isAnyBlank(type, label)) {
			return null;
		}
		String value = StringUtils.EMPTY;
		List<SysDict> list = getDictList(type);
		for (SysDict sysDict : list) {
			if (label.equals(sysDict.getLabel())) {
				value = sysDict.getValue();
				break;
			}
		}
		if (StringUtils.isBlank(value)) {
			SysDict dict = sysDictService.getDictWithLabel(type, label);
			if (dict != null) {
				value = dict.getValue();
			}
		}
		return value;
	}

	/** 根据类型查找字典 */
	@SuppressWarnings("unchecked")
	public List<SysDict> getDictList(String type) {
		if (StringUtils.isBlank(type)) {
			return new ArrayList<>(1);
		}
		List<SysDict> list = null;
		list = (List<SysDict>) ehcacheService.get(CacheConstants.EHCACHE_DEFAULT, CacheConstants.CACHE_KEY_DICT);
		if (CollectionUtils.isEmpty(list)) {
			list = sysDictService.getDictList(type);
			ehcacheService.put(CacheConstants.EHCACHE_DEFAULT, CacheConstants.CACHE_KEY_DICT, list);
		}
		return list;
	}

}
