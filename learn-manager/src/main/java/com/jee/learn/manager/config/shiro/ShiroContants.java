package com.jee.learn.manager.config.shiro;

public class ShiroContants {

    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_INTERATIONS = 1024;
    public static final int SALT_LENGTH = 4;
    public static final int SALT_SIZE = 8;// 必须为 SALT_LENGTH 的两倍

    public static final String DEFAULT_CAPTCHA_PARAM = "captcha";
    public static final String DEFAULT_MESSAGE_PARAM = "message";
    public static final String MESSAGE_PREFIX = "msg:";

    public static final String INVALID_USERNAME_ERROR = "账号禁止登录.";
    public static final String USERNAME_PASSWORD_ERROR = "用户或密码错误, 请重试.";
    public static final String SYSTEM_ERROR = "系统出现点问题，请稍后再试!";

}
