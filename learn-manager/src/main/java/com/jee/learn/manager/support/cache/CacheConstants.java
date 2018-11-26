package com.jee.learn.manager.support.cache;

/**
 * cache信息常量池
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月29日 上午9:02:22 1002360 新建
 */
public class CacheConstants {

    private CacheConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final long NEVER_EXPIRE = -1L;
    public static final String CACHE_KEY_SEPARATOR = ":";
    public static final String CACHE_KEY_APIUSER = "apiuser";
    public static final String CACHE_KEY_TOKEN = "token";
    public static final String CACHE_KEY_DICT = "dict";
    public static final String CACHE_KEY_USER_MENU = "user:menu:";
    public static final String CACHE_KEY_USER_ROLE = "user:role:";

    /** ehcache 默认存储空间 */
    public static final String EHCACHE_DEFAULT = "defCache";
    /** ehcache shiro 系统活动会话缓存存储空间 */
    public static final String EHCACHE_SHIRO = "shiroCache";
    /** ehcache user 用户数据缓存存储空间 */
    public static final String EHCACHE_USER = "userCache";

    /** "null"字符串 */
    public static final String NULL_STRING = "null";
    
    
}
