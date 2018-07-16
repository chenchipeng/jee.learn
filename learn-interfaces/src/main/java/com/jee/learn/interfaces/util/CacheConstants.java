package com.jee.learn.interfaces.util;

/**
 * cache信息常量池
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月29日 上午9:02:22 1002360 新建
 */
public interface CacheConstants {

    String CACHE_KEY_SEPARATOR = ":";
    String CACHE_KEY_APIUSER="apiuser";
    String CACHE_KEY_TOKEN="tkoen";

    /* ehcache缓存相关 */

    String EHCACHE_DEFAULT = "def";

    /** "null"字符串 */
    String NULL_STRING = "null";
}
