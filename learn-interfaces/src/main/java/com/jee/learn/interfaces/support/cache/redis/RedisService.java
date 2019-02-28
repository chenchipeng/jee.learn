package com.jee.learn.interfaces.support.cache.redis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * RedisTemplate 工具类<br/>
 * 参考:https://www.cnblogs.com/skyessay/p/6485187.html<br/>
 * 关于Autowired与Resource的使用请参考https://blog.csdn.net/weixin_40423597/article/details/80643990
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月22日 下午11:22:39 1002360 新建
 */
@Component
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private HashOperations<String, String, String> hashOperations;
    @Resource
    private HashOperations<String, String, Object> hashOperationsObj;
    @Resource
    private ValueOperations<String, Object> valueOperationsObj;

    @Autowired
    private RedisTemplate<String, String> strRedisTemplate;
    @Resource
    private ValueOperations<String, String> valueOperations;

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 添加
     * 
     * @param hashKey
     * @param key
     * @param values
     * @param expire 过期时间(单位:秒),传入 -1 时表示不设置过期时间
     */
    public void put(String hashKey, String key, String values, long expire) {
        try {
            hashOperations.put(hashKey, key, values);
            if (expire != -1) {
                redisTemplate.expire(hashKey, expire, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    /**
     * 添加
     * 
     * @param hashKey
     * @param key
     * @param values
     * @param expire 过期时间(单位:秒),传入 -1 时表示不设置过期时间
     */
    public void putObj(String hashKey, String key, Object values, long expire) {
        try {
            hashOperationsObj.put(hashKey, key, values);
            if (expire != -1) {
                redisTemplate.expire(hashKey, expire, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    /**
     * 添加
     * 
     * @param key
     * @param values
     * @param expire 过期时间(单位:秒),传入 -1 时表示不设置过期时间
     */
    public void setKeyValue(String key, Object values, long expire) {
        try {
            valueOperationsObj.set(key, values);
            if (expire != -1) {
                redisTemplate.expire(key, expire, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    /**
     * 删除
     * 
     * @param hashKey
     * @param key
     */
    public void remove(String hashKey, String key) {
        hashOperations.delete(hashKey, key);
    }

    /**
     * 删除
     * 
     * @param key
     */
    public void remove(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 查询
     * 
     * @param hashKey
     * @param key
     * @return
     */
    public String get(String hashKey, String key) {
        return hashOperations.get(hashKey, key);
    }

    /**
     * 查询
     * 
     * @param hashKey
     * @param key
     * @return
     */
    public Object getObj(String hashKey, String key) {
        return hashOperationsObj.get(hashKey, key);
    }

    /**
     * 查询
     * 
     * @param key
     * @return
     */
    public Object getKeyValue(String key) {
        return valueOperationsObj.get(key);
    }

    /**
     * 获取当前redis库下所有对象
     *
     * @return
     */
    public List<String> getAll(String hashKey) {
        return hashOperations.values(hashKey);
    }

    /**
     * 查询查询当前redis库下所有key
     *
     * @return
     */
    public Set<String> getKeys(String hashKey) {
        return hashOperations.keys(hashKey);
    }

    /**
     * 判断key是否存在redis中
     *
     * @param key 传入key的名称
     * @return
     */
    public boolean isKeyExists(String hashKey, String key) {
        return hashOperations.hasKey(hashKey, key);
    }

    /**
     * 查询当前key下缓存数量
     *
     * @return
     */
    public long count(String hashKey) {
        return hashOperations.size(hashKey);
    }

    /**
     * 清空redis
     */
    public void empty(String hashKey) {
        Set<String> set = hashOperations.keys(hashKey);
        set.stream().forEach(key -> hashOperations.delete(hashKey, key));
    }

    /**
     * 重设过期时间(秒)
     * 
     * @param hashKey
     * @param expire
     */
    public void flushExpire(String hashKey, long expire) {
        flushExpire(hashKey, expire, TimeUnit.SECONDS);
    }

    /**
     * 重设过期时间
     * 
     * @param hashKey
     * @param expire
     * @param timeUnit 单位
     */
    public void flushExpire(String hashKey, long expire, TimeUnit timeUnit) {
        if (expire != -1) {
            redisTemplate.expire(hashKey, expire, timeUnit);
        }
    }

    /**
     * 根据Key查询普通字符串value
     * 
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        return valueOperations.get(key);
    }

    /**
     * 添加<br/>
     * value为非json格式字符串
     * 
     * @param key
     * @param value
     * @param expire 过期时间(单位:秒),传入 -1 时表示不设置过期时间
     */
    public void putStringValue(String key, String value, long expire) {
        try {
            valueOperations.set(key, value);
            if (expire != -1) {
                strRedisTemplate.expire(key, expire, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    /**
     * 重设过期时间<br/>
     * 针对strRedisTemplate
     * 
     * @param hashKey
     * @param expire
     * @param timeUnit 单位
     */
    public void flushStrExpire(String key, long expire, TimeUnit timeUnit) {
        if (expire != -1) {
            strRedisTemplate.expire(key, expire, timeUnit);
        }
    }
}