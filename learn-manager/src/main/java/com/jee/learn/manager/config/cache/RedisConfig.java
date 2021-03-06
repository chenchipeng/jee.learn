package com.jee.learn.manager.config.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis 配置<br/>
 * 参考:https://www.cnblogs.com/skyessay/p/6485187.html
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月21日 下午10:46:18 1002360 新建
 */
@Configuration
public class RedisConfig {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 设置数据存入 redis 的序列化方式
     *
     * @param redisTemplate
     * @param factory
     */
    private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(factory);
    }

    //////// redis json ////////

    /**
     * 实例化 RedisTemplate 对象<br/>
     * 数据以json格式保存
     *
     * @return
     */
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> josnRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        initDomainRedisTemplate(redisTemplate, redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 实例化 ValueOperations 对象,可以使用 Value 操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean("valueOps")
    public ValueOperations<String, Object> valueOps(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    /**
     * 实例化 HashOperations 对象,可以使用 Hash 类型操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean("hashOps")
    public HashOperations<String, String, Object> hashOps(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    /**
     * 实例化 ListOperations 对象,可以使用 List 操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean("listOps")
    public ListOperations<String, Object> listOps(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForList();
    }

    /**
     * 实例化 SetOperations 对象,可以使用 Set 操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean("setOps")
    public SetOperations<String, Object> setOps(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    /**
     * 实例化 ZSetOperations 对象,可以使用 ZSet 操作
     *
     * @param redisTemplate
     * @return
     */
    @Bean("zSetOps")
    public ZSetOperations<String, Object> zSetOps(RedisTemplate<String, Object> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    //////// redis string ////////

    /**
     * 实例化 RedisTemplate 对象<br/>
     * 数据以普通/常量字符串格式保存
     * 
     * @return
     */
    @Bean(name = "strRedisTemplate")
    public RedisTemplate<String, String> strRedisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    /**
     * 实例化 ValueOperations 对象,可以使用 String 操作
     * 
     * @param stringRedisTemplate
     * @return
     */
    @Bean("strValueOps")
    public ValueOperations<String, String> strValueOps(RedisTemplate<String, String> strRedisTemplate) {
        return strRedisTemplate.opsForValue();
    }

    ////// shiro-redis //////

    /**
     * 实例化 RedisTemplate 对象<br/>
     * 用于shiro session管理, key 为字符串, value 为JDK默认的序列化
     * 
     * @return
     */
    @Bean(name = "shiroRedisTemplate")
    public RedisTemplate<String, Object> shiroRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean("shiroHashOps")
    public HashOperations<String, Object, Object> shiroHashOps(RedisTemplate<String, Object> shiroRedisTemplate) {
        return shiroRedisTemplate.opsForHash();
    }

    @Bean("shiroValueOps")
    public ValueOperations<String, Object> shiroValueOps(RedisTemplate<String, Object> shiroRedisTemplate) {
        return shiroRedisTemplate.opsForValue();
    }

}