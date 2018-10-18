package com.jee.learn.test.manager.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.manager.LearnManagerApplication;
import com.jee.learn.manager.support.cache.CacheConstants;
import com.jee.learn.manager.support.cache.EhcacheService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnManagerApplication.class)
public class EhcacheServiceTest {
    
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EhcacheServiceTest.class);
    
    @Autowired
    private EhcacheService ehcacheService;
    
    @Test
    public void readWriteHashTest() {
        String cacheName =CacheConstants.EHCACHE_DEFAULT;
        String key = "key";
        String hashKey1 = "hashKey1";
        String hashKey2 = "hashKey2";
        
        try {
            ehcacheService.put(cacheName, key, hashKey1, 1);
            LOGGER.debug("第一次写读 {}",ehcacheService.get(cacheName, key, hashKey1));
            
            ehcacheService.put(cacheName, key, hashKey2, 2);
            LOGGER.debug("第二次写读 {}",ehcacheService.get(cacheName, key, hashKey2));
            
            LOGGER.debug("读出第一次的值 {}",ehcacheService.get(cacheName, key, hashKey1));
            LOGGER.debug("读出第二次的值 {}",ehcacheService.get(cacheName, key, hashKey2));
        } catch (Exception e) {
            LOGGER.debug("",e);
        }
        
    }


}
