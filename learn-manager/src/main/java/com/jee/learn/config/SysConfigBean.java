package com.jee.learn.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 自定义属性配置文件<br/>
 * application.properties老出现中文乱码
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年4月25日 上午10:20:29 1002360 新建
 */
@Component
@ConfigurationProperties(prefix = "sys")
// @Configuration
// @PropertySource(value = "classpath:sys.properties", encoding = "UTF-8")
public class SysConfigBean {

    /** 系统名称 */
    private String name = "后台管理系统";

    /** 管理端基础路径 */
    private String adminPath = "/a";

    /** 前端访问基础路径 */
    private String frontPath = "/f";

    /** 静态文件路径 */
    private String staticPath = "/static";

    /** 登录成功后跳转到指定HTML页面 */
    private String logginSuccessPage = "main/index";

    /** 刷新管理端页面是否清除登陆状态 */
    private boolean notAllowRefreshIndex = false;

    /** 是否允许多账号同时登录 */
    private boolean multiAccountLogin = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getLogginSuccessPage() {
        return logginSuccessPage;
    }

    public void setLogginSuccessPage(String logginSuccessPage) {
        this.logginSuccessPage = logginSuccessPage;
    }

    public boolean isNotAllowRefreshIndex() {
        return notAllowRefreshIndex;
    }

    public void setNotAllowRefreshIndex(boolean notAllowRefreshIndex) {
        this.notAllowRefreshIndex = notAllowRefreshIndex;
    }

    public boolean isMultiAccountLogin() {
        return multiAccountLogin;
    }

    public void setMultiAccountLogin(boolean multiAccountLogin) {
        this.multiAccountLogin = multiAccountLogin;
    }

}
