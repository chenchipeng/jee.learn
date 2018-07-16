package com.jee.learn.config.shiro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.jee.learn.component.security.LoginFormAuthenticationFilter;
import com.jee.learn.component.security.realm.CustomAuthorizingRealm;
import com.jee.learn.component.security.realm.CustomModularRealmAuthenticator;
import com.jee.learn.component.security.realm.SystemAuthorizingRealm;
import com.jee.learn.component.session.CacheSessionDAO;
import com.jee.learn.component.session.SessionDAO;
import com.jee.learn.component.session.SessionManager;
import com.jee.learn.component.util.SessionIdGen;

/**
 * shiro配置<br/>
 * 参考:http://lihao312.iteye.com/blog/2309788
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年1月31日 下午3:42:14 1002360 新建
 */
@Configuration
@EnableConfigurationProperties({ ShiroConfigBean.class })
public class ShiroConfig {

    /** 允许Filter动态代理注入"shiroFilter"这个Bean */
    @Bean("shiroDelegatingFilterProxy")
    public FilterRegistrationBean delegatingFilterProxy() {

        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();

        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilter");

        List<String> urlPatterns = new ArrayList<String>();
        urlPatterns.add("/*");

        filterRegistrationBean.setFilter(proxy);
        filterRegistrationBean.setUrlPatterns(urlPatterns);
        filterRegistrationBean.setName("shiroFilter");

        return filterRegistrationBean;
    }

    /** shiro必要配置:安全认证过滤器 */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(ShiroConfigBean configBean,
            LoginFormAuthenticationFilter loginFilter, DefaultWebSecurityManager securityManager) {

        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 页面设置
        shiroFilterFactoryBean.setLoginUrl(configBean.getAdminPath() + "/login");
        shiroFilterFactoryBean.setSuccessUrl(configBean.getAdminPath() + "?index");
        shiroFilterFactoryBean.setUnauthorizedUrl("/error/403"); // 未授权界面

        // 自定义Filter
        Map<String, Filter> filterMap = new HashMap<>();
        // boolean isCASSupport = "true".equals(configBean.getCasSupport());
        boolean isCASSupport = configBean.isCasSupport();
        if (isCASSupport) {
            shiroFilterFactoryBean.setLoginUrl(configBean.getCasServerURL() + "?service="
                    + configBean.getCasProjectURL() + configBean.getAdminPath() + "/cas");
            filterMap.put("cas", null);
        }
        filterMap.put("authc", loginFilter);
        shiroFilterFactoryBean.setFilters(filterMap);

        // Shiro权限过滤过滤器定义:authc=所有url都必须认证通过才可以访问;anon=所有url都都可以匿名访问
        Map<String, String> filterChainDefinitionMap = new HashMap<>();
        filterChainDefinitionMap.put(configBean.getStaticPath() + "/**", "anon");
        filterChainDefinitionMap.put(configBean.getFrontPath() + "/**", "anon");
        filterChainDefinitionMap.put(configBean.getAdminPath() + "/login", "authc");
        filterChainDefinitionMap.put(configBean.getAdminPath() + "/logout", "anon");
        filterChainDefinitionMap.put(configBean.getAdminPath() + "/**", "user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;

    }

    /** shiro必要配置:定义Shiro安全管理配置 */
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(CustomModularRealmAuthenticator customAuthenticator,
            SystemAuthorizingRealm systemAuthorizingRealm, CustomAuthorizingRealm customAuthorizingRealm,
            SessionManager sessionManager, CacheManager shiroCacheManager, RememberMeManager rememberMeManager) {

        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setSessionManager(sessionManager);
        securityManager.setCacheManager(shiroCacheManager);
        securityManager.setRememberMeManager(rememberMeManager);
        securityManager.setAuthenticator(customAuthenticator);

        List<Realm> realms = new ArrayList<>(2);
        realms.add(systemAuthorizingRealm);
        realms.add(customAuthorizingRealm);
        securityManager.setRealms(realms);

        return securityManager;

    }

    /** 自定义Relam认证器 */
    @Bean("customAuthenticator")
    public CustomModularRealmAuthenticator customAuthenticator() {
        CustomModularRealmAuthenticator customAuthenticator = new CustomModularRealmAuthenticator();
        // 配置认证策略，只要有一个Realm认证成功即可，并且返回所有认证成功信息
        customAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        return customAuthenticator;
    }

    /** 自定义会话管理配置 */
    @Bean("sessionManager")
    public SessionManager sessionManager(ShiroConfigBean configBean, SessionDAO sessionDAO) {
        SessionManager sessionManager = new SessionManager();

        sessionManager.setSessionDAO(sessionDAO);

        // 会话超时时间->30分钟，单位：毫秒
        sessionManager.setGlobalSessionTimeout(configBean.getSessionTimeout());
        // 定时清理失效会话, 清理用户直接关闭浏览器造成的孤立会话
        sessionManager.setSessionValidationInterval(configBean.getValidationInterval());
        sessionManager.setSessionValidationSchedulerEnabled(true);

        /*
         * 指定本系统SESSIONID, 默认为: JSESSIONID 问题: 与SERVLET容器名冲突, 如JETTY, TOMCAT
         * 等默认JSESSIONID, 当跳出SHIRO
         * SERVLET时如ERROR-PAGE容器会为JSESSIONID重新分配值导致登录会话丢失!
         */
        SimpleCookie cookie = new SimpleCookie();
        cookie.setName(configBean.getSessionId());
        sessionManager.setSessionIdCookie(cookie);

        return sessionManager;
    }

    /** 自定义授权缓存管理器 */
    @Bean("shiroCacheManager")
    @DependsOn("lifecycleBeanPostProcessor")
    public EhCacheManager shiroCacheManager() {
        EhCacheManager ecm = new EhCacheManager();
        ecm.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return ecm;
    }

    /** 自定义Session存储容器 */
    @Bean("sessionDAO")
    public SessionDAO sessionDAO(ShiroConfigBean configBean, SessionIdGen sessionIdGen,
            CacheManager shiroCacheManager) {

        // if ("true".equals(configBean.isRedisSupport())) {
        if (configBean.isRedisSupport()) {

            // JedisSessionDAO jedisSessionDAO = new JedisSessionDAO();
            // jedisSessionDAO.setSessionIdGenerator(sessionIdGen);
            // jedisSessionDAO.setSessionKeyPrefix(configBean.getRedisKeyPrefix()
            // + "_session_");
            // return jedisSessionDAO;

            return null;

        } else {

            CacheSessionDAO cacheSessionDAO = new CacheSessionDAO();
            cacheSessionDAO.setSessionIdGenerator(sessionIdGen);
            cacheSessionDAO.setActiveSessionsCacheName("activeSessionsCache");
            cacheSessionDAO.setCacheManager(shiroCacheManager);
            return cacheSessionDAO;
        }

    }

    /**
     * shiro 记住我 功能配置<br/>
     * cookie管理对象<br/>
     * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
     */
    @Bean("rememberMeManager")
    public CookieRememberMeManager rememberMeManager() {

        SimpleCookie simpleCookie = new SimpleCookie();
        // 这个参数是cookie的名称，对应前端的checkbox的name = rememberMe
        simpleCookie.setName("rememberMe");
        // 记住我cookie生效时间30天 ,单位秒
        simpleCookie.setMaxAge(259200);

        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(simpleCookie);
        // rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
        cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
        return cookieRememberMeManager;
    }

    /*
     * AOP 配置如下
     */

    /** 保证实现了Shiro内部lifecycle函数的bean执行 */
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    /** AOP式方法级权限检查-代理中心 */
    @Bean
    // Enable Shiro Annotations for Spring-configured beans. Only run after
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
        daap.setProxyTargetClass(true);
        return daap;
    }

    /** AOP式方法级权限检查-通知器 */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
        aasa.setSecurityManager(securityManager);
        return aasa;
    }

    /** 支持Shiro对Controller的方法级AOP安全控制 */
    // // @Bean
    // public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
    // SimpleMappingExceptionResolver smer = new
    // SimpleMappingExceptionResolver();
    //
    // Properties mappings = new Properties();
    // mappings.put("org.apache.shiro.authz.UnauthorizedException",
    // "/main/login");
    // //
    // mappings.put("org.apache.shiro.authz.AuthenticationException","/a/main/login");
    // smer.setExceptionMappings(mappings);
    //
    // return smer;
    // }

}
