package com.jee.learn.interfaces.support.cache.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.interfaces.LearnInterfaceApplication;
import com.jee.learn.interfaces.support.cache.redis.RedisService;

/**
 * RedisServiceTest
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年2月28日 下午3:38:51 ccp 新建
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnInterfaceApplication.class)
public class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void putTest() {
        redisService.putStringValue("test", "test", -1);
    }

}
