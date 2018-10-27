package com.jee.learn.manager.config.shiro.security;

import java.io.Serializable;

/**
 * 权鉴主体
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月16日 上午9:47:40 ccp 新建
 */
public class CustomPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // 编号
    private String loginName; // 登录名
    private String name; // 姓名
    private String oldLoginIP;// 上次登录IP
    private String oldloginDate;// 上次登录时间

    public CustomPrincipal(String id, String loginName, String name) {
        super();
        this.id = id;
        this.loginName = loginName;
        this.name = name;
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

    public String getOldLoginIP() {
        return oldLoginIP;
    }

    public void setOldLoginIP(String oldLoginIP) {
        this.oldLoginIP = oldLoginIP;
    }

    public String getOldloginDate() {
        return oldloginDate;
    }

    public void setOldloginDate(String oldloginDate) {
        this.oldloginDate = oldloginDate;
    }

    @Override
    public String toString() {
        return "CustomPrincipal [id=" + id + ", loginName=" + loginName + ", name=" + name + ", oldLoginIP="
                + oldLoginIP + ", oldloginDate=" + oldloginDate + "]";
    }

}
