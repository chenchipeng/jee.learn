package com.jee.learn.jwt.common;

/**
 * Created by ace on 2017/9/10.<br/>
 * 参考: https://gitee.com/minull/ace-security/tree/master
 */
public interface IJWTInfo {

    /**
     * 获取用户名
     * 
     * @return
     */
    String getUniqueName();

    /**
     * 获取用户ID
     * 
     * @return
     */
    String getId();

    /**
     * 获取名称
     * 
     * @return
     */
    String getName();
}
