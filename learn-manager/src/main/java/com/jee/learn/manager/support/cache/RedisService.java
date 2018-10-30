package com.jee.learn.manager.support.cache;

import java.util.List;
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
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月22日 下午11:22:39 1002360 新建
 */
@Component
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Resource(name = "hashOps")
    private HashOperations<String, String, Object> hashOps;
    @Resource(name = "valueOps")
    private ValueOperations<String, Object> valueOps;

    @Autowired
    private RedisTemplate<String, String> strRedisTemplate;
    @Resource(name = "strValueOps")
    private ValueOperations<String, String> strValueOps;

    @Autowired
    private RedisTemplate<String, Object> shiroRedisTemplate;
    @Resource(name = "shiroHashOps")
    private HashOperations<String, Object, Object> shiroHashOps;
    @Resource(name = "shiroValueOps")
    private ValueOperations<String, Object> shiroValueOps;

    private Logger logger = LoggerFactory.getLogger(getClass());

    //////// redis json ////////

    /**
     * 添加
     * 
     * @param hashKey
     * @param key
     * @param values
     * @param expire
     * @param unit 过期时间,传入 -1 时表示不设置过期时间
     */
    public void putHash(String hashKey, String key, Object values, long expire, TimeUnit unit) {
        try {
            hashOps.put(hashKey, key, values);
            if (expire != -1) {
                redisTemplate.expire(hashKey, expire, unit);
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
     * @param expire
     * @param unit 过期时间,传入 -1 时表示不设置过期时间
     */
    public void putValue(String key, Object values, long expire, TimeUnit unit) {
        try {
            valueOps.set(key, values);
            if (expire != -1) {
                redisTemplate.expire(key, expire, unit);
            }
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    /**
     * 删除
     * 
     * @param key
     * @param hashKey
     */
    public void removeHash(String key, String hashKey) {
        try {
            hashOps.delete(key, hashKey);
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    /**
     * 删除
     * 
     * @param key
     */
    public void removeValue(String key) {
        try {
            flushExpire(key, 1L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    /**
     * 查询
     * 
     * @param key
     * @param hashKey
     * @return
     */
    public Object getHash(String key, String hashKey) {
        return hashOps.get(key, hashKey);
    }

    /**
     * 查询
     * 
     * @param key
     * @return
     */
    public Object getValue(String key) {
        return valueOps.get(key);
    }

    /**
     * 根据key模糊查询 like%
     * 
     * @param key
     * @return hash
     */
    public List<Object> getAllHash(String key) {
        return hashOps.values(key);
    }

    /**
     * 重设过期时间
     * 
     * @param key
     * @param expire
     * @param unit
     */
    public void flushExpire(String key, long expire, TimeUnit unit) {
        try {
            if (expire < -1L) {
                return;
            }
            redisTemplate.expire(key, expire, unit);

        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    /**
     * 根据key清除缓存
     * 
     * @param key
     */
    public void delete(String key) {
        flushExpire(key, 1L, TimeUnit.MILLISECONDS);
    }

    //////// redis string ////////

    /**
     * 添加<br/>
     * value为非json格式字符串
     * 
     * @param key
     * @param value
     * @param expire 过期时间(单位:秒),传入 -1 时表示不设置过期时间
     */
    public void putStrValue(String key, String value, long expire, TimeUnit unit) {
        try {
            strValueOps.set(key, value);
            if (expire != -1) {
                strRedisTemplate.expire(key, expire, unit);
            }
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    /**
     * 删除
     * 
     * @param key
     */
    public void removeStrValue(String key) {
        try {
            flushStrExpire(key, 1L, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    /**
     * 查询
     * 
     * @param key
     * @return
     */
    public String getStrValue(String key) {
        return strValueOps.get(key);
    }

    /**
     * 重设过期时间
     * 
     * @param key
     * @param expire
     * @param unit
     */
    public void flushStrExpire(String key, long expire, TimeUnit unit) {
        try {
            if (expire < -1L) {
                return;
            }
            strRedisTemplate.expire(key, expire, unit);
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    ////// shiro-redis //////

    /**
     * 新增
     * 
     * @param key
     * @param hashKey
     * @param value 序列化对象/byte[]
     * @param expire 有效期: 毫秒
     */
    public void putShiroHash(String key, String hashKey, Object value, long expire, TimeUnit unit) {
        try {
            shiroHashOps.put(key, hashKey, value);
            if (expire != -1) {
                redisTemplate.expire(key, expire, unit);
            }
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    /**
     * 新增
     * 
     * @param key
     * @param value 序列化对象/byte[]
     * @param expire 有效期: 毫秒
     */
    public void putShiroValue(String key, Object value, long expire, TimeUnit unit) {
        try {
            shiroValueOps.set(key, value);
            if (expire != -1) {
                redisTemplate.expire(key, expire, unit);
            }
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    /**
     * 查询
     * 
     * @param key
     * @param hashKey
     * @return 序列化对象/byte[]
     */
    public Object getShiroHash(String key, Object hashKey) {
        return shiroHashOps.get(key, hashKey);
    }

    /**
     * 查询
     * 
     * @param key
     * @return 序列化对象/byte[]
     */
    public Object getShiroValue(String key) {
        return shiroValueOps.get(key);
    }

    /**
     * 刷新shiroRedisTemplate有效期
     * 
     * @param key
     * @param expire
     */
    public void flushShiroExpire(String key, long expire, TimeUnit unit) {
        try {
            if (expire < -1L) {
                return;
            }
            shiroRedisTemplate.expire(key, expire, unit);
        } catch (Exception e) {
            logger.warn("", e);
        }
    }

    public RedisTemplate<String, Object> getShiroRedisTemplate() {
        return shiroRedisTemplate;
    }
}