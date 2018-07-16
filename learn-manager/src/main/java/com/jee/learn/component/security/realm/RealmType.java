package com.jee.learn.component.security.realm;

/**
 * 登录Realm类型<br/>
 * 参考:https://blog.csdn.net/xiangwanpeng/article/details/54802509
 * 
 * @author admin
 * @version 1.0 <br/>
 *          修改记录: <br/>
 *          1.2018年5月9日 下午10:10:09 admin 新建
 */
public enum RealmType {
    MANAGER("System"), CUSTOM("Custom");

    private String type;

    private RealmType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
