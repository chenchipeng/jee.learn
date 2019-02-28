package com.jee.learn.interfaces.support.cache.ehcache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jee.learn.interfaces.support.cache.CacheConstants;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * ehCacheManager 工具类
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月15日 下午8:44:54 1002360 新建
 */
@Component
public class EhcacheService {

    @Autowired
    private CacheManager ehCacheManager;

    /**
     * 写入缓存
     * 
     * @param cacheName 存储块名称, 形如: {@link CacheConstants#EHCACHE_DEFAULT}
     * @param key
     * @param value
     */
    public void put(String cacheName, String key, Object value) {
        Cache cache = ehCacheManager.getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }

    /**
     * 获取缓存
     * 
     * @param cacheName 存储块名称, 形如: {@link CacheConstants#EHCACHE_DEFAULT}
     * @param key
     * @return
     */
    public Object get(String cacheName, String key) {
        Cache cache = ehCacheManager.getCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
    }

    /**
     * 删除缓存
     * 
     * @param cacheName 存储块名称, 形如: {@link CacheConstants#EHCACHE_DEFAULT}
     * @param key
     */
    public void remove(String cacheName, String key) {
        Cache cache = ehCacheManager.getCache(cacheName);
        cache.remove(key);
    }

}
