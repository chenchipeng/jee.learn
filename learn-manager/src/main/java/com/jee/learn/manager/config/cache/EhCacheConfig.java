package com.jee.learn.manager.config.cache;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jee.learn.manager.support.cache.CacheConstants;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;

/**
 * EhCache配置,可以替代ehcache.xml<br/>
 * 参考:https://my.oschina.net/sdlvzg/blog/1594834
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月15日 下午7:54:38 1002360 新建
 */
@Configuration
public class EhCacheConfig implements CachingConfigurer {

    private static final String DEFAULT_CONFIG_NAME = "defaultCacheConfig";

    @Value("${spring.ehcache.disk-store-path}")
    private String diskStorePath;

    @Bean(destroyMethod = "shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {

        // 缓存路径
        DiskStoreConfiguration diskStore = new DiskStoreConfiguration();
        diskStore.setPath(diskStorePath);
        // 当堆内存或者非堆内存里面的元素已经满了的时候，将其中的元素临时的存放在磁盘上，一旦重启就会消失
        PersistenceConfiguration persistence = new PersistenceConfiguration();
        persistence.strategy(Strategy.LOCALTEMPSWAP);

        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.setName(DEFAULT_CONFIG_NAME);
        config.setDefaultCacheConfiguration(defCache(persistence));
        
        
        config.diskStore(diskStore);

        /* 可以创建多个cacheConfiguration，都添加到Config中 */
        // 默认配置
        // 系统活动会话缓存
        config.addCache(shiroCache(persistence));

        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    /** 默认配置 */
    private CacheConfiguration defCache(PersistenceConfiguration persistence) {
        CacheConfiguration defCache = new CacheConfiguration();
        defCache.setName(CacheConstants.EHCACHE_DEFAULT);
        defCache.setTimeToIdleSeconds(1800);
        defCache.setMaxEntriesLocalHeap(100);
        defCache.setMaxEntriesLocalDisk(100000);
        defCache.persistence(persistence);
        return defCache;
    }

    /** 系统活动会话缓存 */
    private CacheConfiguration shiroCache(PersistenceConfiguration persistence) {
        CacheConfiguration shiroCache = new CacheConfiguration();
        shiroCache.setName("shiroCache");
        shiroCache.setTimeToIdleSeconds(0);
        shiroCache.setTimeToIdleSeconds(0);
        shiroCache.setDiskExpiryThreadIntervalSeconds(180L);
        shiroCache.setMaxEntriesLocalHeap(100);
        shiroCache.setMaxEntriesLocalDisk(100000);
        shiroCache.persistence(persistence);
        return shiroCache;
    }

    /**
     * 指定spring boot 使用哪种缓存<br/>
     * 如有使用其他缓存作为boot的默认缓存时请勿注入此bean
     */
    // @Bean
    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }

    /**
     * 不使用ehcache作为boot的默认缓存时无需注入此bean
     */
    // @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }

}
