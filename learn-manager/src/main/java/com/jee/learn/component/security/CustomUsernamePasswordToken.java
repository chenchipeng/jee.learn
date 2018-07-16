/**
 * Copyright &copy; 2015-2020 <a href="http://www.chinaskin.net/">chnskin</a> All rights reserved.
 */
package com.jee.learn.component.security;

import org.apache.commons.lang3.StringUtils;

import com.jee.learn.component.security.realm.RealmType;

/**
 * 用户和密码（包含验证码）令牌类
 * 
 * @author chnskin
 * @version 2013-5-19
 */
public class CustomUsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

    private static final long serialVersionUID = 1L;

    private String captcha;// 验证码
    private boolean mobileLogin;// 是否手机登录
    private String realmType;// realm登录类型,区分用户表
    private boolean apiLogin;// 是否API实现前后端分离登录

    public CustomUsernamePasswordToken() {
        super();
    }
    
    public CustomUsernamePasswordToken(String username, char[] password, boolean rememberMe, String host,
            String captcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.mobileLogin = false;
        this.realmType = RealmType.MANAGER.toString();
        this.apiLogin = false;
    }

    public CustomUsernamePasswordToken(String username, char[] password, boolean rememberMe, String host,
            String captcha, boolean mobileLogin, String realmType, boolean apiLogin) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
        this.mobileLogin = mobileLogin;
        this.realmType = RealmType.MANAGER.toString();
        if (StringUtils.isNotBlank(realmType)) {
            this.realmType = realmType;
        }
        this.apiLogin = apiLogin;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public boolean isMobileLogin() {
        return mobileLogin;
    }

    public String getRealmType() {
        return realmType;
    }

    public boolean isApiLogin() {
        return apiLogin;
    }

}