package com.jee.learn.manager.config.shiro;

public class ShiroContants {

    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_LENGTH = 4;
    public static final int SALT_SIZE = 8;// 必须为 SALT_LENGTH 的两倍

    public static final String MESSAGE_PREFIX = "msg:";

    public static final String INVALID_USERNAME_ERROR = "账号禁止登录.";
    public static final String USERNAME_PASSWORD_ERROR = "用户或密码错误, 请重试.";
    public static final String CAPTCH_ERROR = "验证码错误, 请重试.";
    public static final String MULTI_LOGIN_ERROR = "账号已在其它地方登录, 请重新登录.";
    public static final String SYSTEM_ERROR = "系统出现点问题, 请稍后再试!";

    //////// session dao 相关 ////////
    public static final String SESSION_PARAM_PREFIX = "session_";
    public static final String SESSION_UPDATE_PARAMETER = "updateSession";
    public static final String SESSION_REQUEST_ATTRIBUTE_PREFIX = "session_";
}
