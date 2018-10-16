package com.jee.learn.manager.config.shiro.mv;

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

    @Override
    public String toString() {
        return id;
    }

}
