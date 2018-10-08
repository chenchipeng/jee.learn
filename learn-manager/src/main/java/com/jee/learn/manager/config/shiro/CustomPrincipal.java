package com.jee.learn.manager.config.shiro;

import java.io.Serializable;

public class CustomPrincipal implements Serializable{

    private static final long serialVersionUID = 1L;
    
    private String id; // 编号
    private String loginName; // 登录名
    private String name; // 姓名
    private boolean mobileLogin; // 是否手机登录
    

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

    @Override
    public String toString() {
        return id;
    }

}
