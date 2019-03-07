package com.jee.learn.interfaces.support.cache;

/**
 * cache信息常量池
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月29日 上午9:02:22 1002360 新建
 */
public class CacheConstants {

    private CacheConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String CACHE_KEY_SEPARATOR = ":";
    public static final String CACHE_KEY_TOKEN = "tkoen";

    //////// ehcache缓存相关 ////////

    /** 默认存储块 */
    public static final String EHCACHE_DEFAULT = "def";
    /** "null"字符串 */
    public static final String NULL_STRING = "null";

    //////// redis缓存相关 ////////

    public static final Long SIXTY = 60L;

}
