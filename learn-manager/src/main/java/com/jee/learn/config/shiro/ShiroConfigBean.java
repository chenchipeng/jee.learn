package com.jee.learn.config.shiro;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * shiro配置属性映射
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年1月31日 下午3:44:20 1002360 新建
 */
@ConfigurationProperties(prefix = "shiro")
public class ShiroConfigBean {

    private String adminPath = "/a";
    private String frontPath = "/f";
    private String staticPath = "/static";
    private String sessionId = "sys";

    private boolean redisSupport = false;
    private String redisKeyPrefix = "sys";

    private boolean casSupport = false;
    private String casServerURL = "127.0.0.1:8443";
    private String casProjectURL = "sys";

    private long sessionTimeout = 1800000L;
    private long validationInterval = 1800000L;

    public String getAdminPath() {
        return adminPath;
    }

    public void setAdminPath(String adminPath) {
        this.adminPath = adminPath;
    }

    public String getFrontPath() {
        return frontPath;
    }

    public void setFrontPath(String frontPath) {
        this.frontPath = frontPath;
    }

    public String getStaticPath() {
        return staticPath;
    }

    public void setStaticPath(String staticPath) {
        this.staticPath = staticPath;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isRedisSupport() {
        return redisSupport;
    }

    public void setRedisSupport(boolean redisSupport) {
        this.redisSupport = redisSupport;
    }

    public String getRedisKeyPrefix() {
        return redisKeyPrefix;
    }

    public void setRedisKeyPrefix(String redisKeyPrefix) {
        this.redisKeyPrefix = redisKeyPrefix;
    }

    public boolean isCasSupport() {
        return casSupport;
    }

    public void setCasSupport(boolean casSupport) {
        this.casSupport = casSupport;
    }

    public String getCasServerURL() {
        return casServerURL;
    }

    public void setCasServerURL(String casServerURL) {
        this.casServerURL = casServerURL;
    }

    public String getCasProjectURL() {
        return casProjectURL;
    }

    public void setCasProjectURL(String casProjectURL) {
        this.casProjectURL = casProjectURL;
    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public long getValidationInterval() {
        return validationInterval;
    }

    public void setValidationInterval(long validationInterval) {
        this.validationInterval = validationInterval;
    }

}
