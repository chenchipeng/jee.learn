package com.jee.learn.manager.support.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * ehCacheManager 工具类
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月15日 下午8:44:54 1002360 新建
 */
@Service
public class EhcacheService {

    @Autowired
    private CacheManager ehCacheManager;

    public void put(String cacheName, String key, Object value) {
        Cache cache = ehCacheManager.getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }

    public Object get(String cacheName, String key) {
        Cache cache = ehCacheManager.getCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
    }

    public void remove(String cacheName, String key) {
        Cache cache = ehCacheManager.getCache(cacheName);
        cache.remove(key);
    }
    
}
