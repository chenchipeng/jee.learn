/**
 * Copyright &copy; 2015-2020 <a href="http://www.chinaskin.net/">chnskin</a> All rights reserved.
 */
package com.jee.learn.component.util;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Cache工具类
 * 
 * @author chnskin
 * @version 2013-5-29
 */
@Component
public class CacheUtils {

    private static final String SYS_CACHE = "sysCache";

    private CacheManager cacheManager;

    @Autowired
    private EhCacheManager shiroCacheManager;
    
    public CacheManager getCacheManager() {
        if (cacheManager == null) {
            this.cacheManager = shiroCacheManager.getCacheManager();
        }
        return cacheManager;
    }

    /**
     * 获取SYS_CACHE缓存
     * 
     * @param key
     * @return
     */
    public Object get(String key) {
        return get(SYS_CACHE, key);
    }

    /**
     * 写入SYS_CACHE缓存
     * 
     * @param key
     * @return
     */
    public void put(String key, Object value) {
        put(SYS_CACHE, key, value);
    }

    /**
     * 从SYS_CACHE缓存中移除
     * 
     * @param key
     * @return
     */
    public void remove(String key) {
        remove(SYS_CACHE, key);
    }

    /**
     * 获取缓存
     * 
     * @param cacheName
     * @param key
     * @return
     */
    public Object get(String cacheName, String key) {
        Element element = getCache(cacheName).get(key);
        return element == null ? null : element.getObjectValue();
    }

    /**
     * 写入缓存
     * 
     * @param cacheName
     * @param key
     * @param value
     */
    public void put(String cacheName, String key, Object value) {
        Element element = new Element(key, value);
        getCache(cacheName).put(element);
    }

    /**
     * 从缓存中移除
     * 
     * @param cacheName
     * @param key
     */
    public void remove(String cacheName, String key) {
        getCache(cacheName).remove(key);
    }

    /**
     * 获得一个Cache，没有则创建一个。
     * 
     * @param cacheName
     * @return
     */
    private Cache getCache(String cacheName) {
        Cache cache = getCacheManager().getCache(cacheName);
        if (cache == null) {
            cacheManager.addCache(cacheName);
            cache = cacheManager.getCache(cacheName);
            cache.getCacheConfiguration().setEternal(true);
        }
        return cache;
    }

}
