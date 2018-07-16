package com.jee.learn.component.security;

import java.io.Serializable;

import com.jee.learn.model.sys.SysUser;

/**
 * 授权用户信息
 */
public class Principal implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // 编号
    private String loginName; // 登录名
    private String name; // 姓名
    private boolean mobileLogin; // 是否手机登录
    private boolean apiLogin;// 是否通过API实现前后端分离登录

    public Principal() {
        super();
    }

    public Principal(SysUser user, boolean mobileLogin, boolean apiLogin) {
        this.id = user.getId();
        this.loginName = user.getLoginName();
        this.name = user.getName();
        this.mobileLogin = mobileLogin;
        this.apiLogin = apiLogin;
    }

    public String getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getName() {
        return name;
    }

    public boolean isMobileLogin() {
        return mobileLogin;
    }

    public boolean isApiLogin() {
        return apiLogin;
    }

    @Override
    public String toString() {
        return id;
    }

}
