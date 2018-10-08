package com.jee.learn.manager.config.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    /** shiro url 拦截配置 */
    private Map<String, String> urlFilter() {
        // 设置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>(9);

        // 开放静态资源
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/plugins/**", "anon");
        // 开放登录页面
        filterChainDefinitionMap.put("/login", "anon");

        // 游客，开放权限
        filterChainDefinitionMap.put("/guest/**", "anon");
        // 用户，需要角色权限 "user"
        filterChainDefinitionMap.put("/user/**", "roles[user]");
        // 管理员，需要角色权限 "admin"
        filterChainDefinitionMap.put("/admin/**", "roles[admin]");

        // 其余接口一律拦截
        // 主要这行代码必须放在所有权限设置的最后, 不然会导致所有 url 都被拦截
        filterChainDefinitionMap.put("/**", "authc");

        return filterChainDefinitionMap;
    }

    @Bean
    public ShiroFilterFactoryBean shirFilter(org.apache.shiro.mgt.SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // setLoginUrl 如果不设置值，默认会自动寻找Web工程根目录下的"/login.jsp"页面 或 "/login" 映射
        shiroFilterFactoryBean.setLoginUrl("/notLogin");
        // 设置无权限时跳转的 url
        shiroFilterFactoryBean.setUnauthorizedUrl("/notRole");

        // 设置拦截器
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
        return securityManager;
    }

}
