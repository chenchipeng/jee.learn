package com.jee.learn.manager.config.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jee.learn.manager.config.SystemConfig;
import com.jee.learn.manager.config.shiro.mv.CustomRealm;

/**
 * shiro config<br/>
 * 参考:https://blog.csdn.net/weixin_38132621/article/details/80216056
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月8日 下午2:14:05 ccp 新建
 */
@Configuration
public class ShiroConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShiroConfig.class);
    private static final String ANY_REQUEST = "/**";

    @Autowired
    private SystemConfig systemConfig;

    /** shiro url 拦截配置 */
    private Map<String, String> urlFilter() {
        // 设置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>(9);

        // 开放静态资源
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/plugins/**", "anon");

        // 登录页面拦截
        filterChainDefinitionMap.put(systemConfig.getAuthcPath() + "/login", "authc");
        filterChainDefinitionMap.put(systemConfig.getAuthcPath() + "/logout", "logout");

        // 游客，开放权限
        filterChainDefinitionMap.put(systemConfig.getGuestPath() + ANY_REQUEST, "anon");
        // 用户，需要权限 "user"
        filterChainDefinitionMap.put(systemConfig.getAuthcPath() + ANY_REQUEST, "user");

        return filterChainDefinitionMap;
    }

    /** shiro filters 拦截配置 */
    private Map<String, Filter> customFilters() {
        Map<String, Filter> map = new LinkedHashMap<>(1);
        map.put("authc", new CustomFormAuthenticationFilter());
        return map;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 相关URL
        shiroFilterFactoryBean.setLoginUrl(systemConfig.getAuthcPath() + "/login");
        shiroFilterFactoryBean.setSuccessUrl(systemConfig.getAuthcPath());
        // shiroFilterFactoryBean.setUnauthorizedUrl("https://www.baidu.com/");

        // 设置拦截器
        shiroFilterFactoryBean.setFilters(customFilters());
        shiroFilterFactoryBean.setFilterChainDefinitionMap(urlFilter());

        LOGGER.debug("shiro拦截器工厂类注入成功");
        return shiroFilterFactoryBean;
    }

    /**
     * 注入 securityManager
     */
    @Bean
    public org.apache.shiro.mgt.SecurityManager securityManager(CustomRealm customRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm
        securityManager.setRealm(customRealm);
        // session cache

        return securityManager;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
