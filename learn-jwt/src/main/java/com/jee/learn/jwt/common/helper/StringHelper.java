package com.jee.learn.jwt.common.helper;

/**
 * Created by ace on 2017/9/10.<br/>
 * 参考: https://gitee.com/minull/ace-security/tree/master
 */
public class StringHelper {
    
    public static String getObjectValue(Object obj) {
        return obj == null ? "" : obj.toString();
    }
    
}
