package com.jee.learn.manager.support.cache;

import java.util.HashMap;
import java.util.Map;

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
    
    //////// K V ////////

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
    
    //////// K HASH ////////

    @SuppressWarnings("unchecked")
    public void put(String cacheName, String key, String hashKey, Object value) {
        Cache cache = ehCacheManager.getCache(cacheName);
        Element element = cache.get(key);

        Map<String, Object> map = null;
        if (element != null) {
            map = (Map<String, Object>) element.getObjectValue();
        }
        if (map == null) {
            map = new HashMap<>(1);
        }

        map.put(hashKey, value);
        element = new Element(key, map);
        cache.put(element);
    }

    @SuppressWarnings("unchecked")
    public Object get(String cacheName, String key, String hashKey) {
        Cache cache = ehCacheManager.getCache(cacheName);
        Element element = cache.get(key);
        if (element == null) {
            return null;
        }
        Map<String, Object> map = (Map<String, Object>) element.getObjectValue();
        return map.get(hashKey);
    }

    @SuppressWarnings("unchecked")
    public void delete(String cacheName, String key, String hashKey) {
        Cache cache = ehCacheManager.getCache(cacheName);
        Element element = cache.get(key);
        if (element == null) {
            return;
        }
        Map<String, Object> map = (Map<String, Object>) element.getObjectValue();
        map.remove(hashKey);

        element = new Element(key, map);
        cache.put(element);
    }

    // 得到内存的真实大小calculateInMemorySize()

}
