package com.jee.learn.manager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自定义系统属性配置
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月9日 上午11:32:17 ccp 新建
 */
@Component
@ConfigurationProperties(prefix = "system")
public class SystemConfig {

    private String name = "后台管理系统";
    private String authcPath = "/u";// 用户
    private String guestPath = "/g";// 游客

    private Long sessionTimeout = 1800000L;// sessoin默认30分钟有效期
    private Long sessionTimeoutClean = 180000L;// 默认关闭浏览器后3分钟清除session

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthcPath() {
        return authcPath;
    }

    public void setAuthcPath(String userPath) {
        this.authcPath = userPath;
    }

    public String getGuestPath() {
        return guestPath;
    }

    public void setGuestPath(String guestPath) {
        this.guestPath = guestPath;
    }

    public Long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Long getSessionTimeoutClean() {
        return sessionTimeoutClean;
    }

    public void setSessionTimeoutClean(Long sessionTimeoutClean) {
        this.sessionTimeoutClean = sessionTimeoutClean;
    }

}
