package com.jee.learn.interfaces.support.cache.ehcache;

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

import com.jee.learn.interfaces.support.cache.CacheConstants;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration.Strategy;

/**
 * EhCache配置,可以替代ehcache.xml<br/>
 * 参考:https://my.oschina.net/sdlvzg/blog/1594834
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月15日 下午7:54:38 1002360 新建
 */
@Configuration
public class EhCacheConfig implements CachingConfigurer {

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
        config.diskStore(diskStore);
        
        // 可以创建多个cacheConfiguration，都添加到Config中
        config.addCache(initDefCache(persistence));

        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    //////// 缓存块定义 ////////

    /**
     * 默认配置
     * 
     * @param persistence
     * @return
     */
    private CacheConfiguration initDefCache(PersistenceConfiguration persistence) {
        CacheConfiguration defCache = new CacheConfiguration();
        defCache.setName(CacheConstants.EHCACHE_DEFAULT);
        defCache.setTimeToIdleSeconds(1800);// 最后一次访问后失效的间隔
        defCache.setMaxEntriesLocalHeap(100);// 内存最大缓存个数
        defCache.setMaxEntriesLocalDisk(100000);// 磁盘最大缓存个数
        defCache.persistence(persistence);

        return defCache;
    }

    //////// boot 缓存选型配置 ////////

    /**
     * 指定spring boot 使用哪种缓存<br/>
     * 如有使用其他缓存作为boot的默认缓存时请勿注入此bean
     */
    // @Bean
    @Override
    public CacheManager cacheManager() {
        // return null;
        return new EhCacheCacheManager(ehCacheManager());
    }

    /**
     * 不使用ehcache作为boot的默认缓存时无需注入此bean
     */
    // @Bean
    @Override
    public KeyGenerator keyGenerator() {
        // return null;
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
