package com.jee.learn.manager.config.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jee.learn.manager.config.SystemConfig;
import com.jee.learn.manager.config.shiro.mv.CustomRealm;
import com.jee.learn.manager.config.shiro.session.CacheSessionDAO;
import com.jee.learn.manager.config.shiro.session.CustomSessionIdGenerator;
import com.jee.learn.manager.config.shiro.session.CustomWebSessionManager;
import com.jee.learn.manager.config.shiro.session.SessionDAO;
import com.jee.learn.manager.support.cache.CacheConstants;

import net.sf.ehcache.CacheManager;

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
    private static final String SIMPLE_COOKIE_NAME_SUFFIX = ".session.id";

    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private CacheManager ehCacheManager;

    //////// shiro 拦截配置 ////////

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

    //////// shiro filter factory配置 ////////

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        // 必须设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 相关URL
        shiroFilterFactoryBean.setLoginUrl(systemConfig.getAuthcPath() + "/login");
        shiroFilterFactoryBean.setSuccessUrl(systemConfig.getAuthcPath() + "?login");
        shiroFilterFactoryBean.setUnauthorizedUrl(systemConfig.getAuthcPath() + "/403");

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
    public org.apache.shiro.mgt.SecurityManager securityManager(CustomRealm customRealm,
            CustomWebSessionManager sessionManager, EhCacheManager shiroCacheManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm
        securityManager.setRealm(customRealm);
        // 设置session manager
        securityManager.setSessionManager(sessionManager);
        // 设置cacheManager
        securityManager.setCacheManager(shiroCacheManager);

        return securityManager;
    }

    //////// shiro sessionManager配置 ////////

    @Bean("sessionManager")
    public CustomWebSessionManager sessionManager(SessionDAO sessionDAO) {
        CustomWebSessionManager sessionManager = new CustomWebSessionManager();

        // Session存储容器
        sessionManager.setSessionDAO(sessionDAO);

        // 会话超时时间，单位：毫秒
        sessionManager.setGlobalSessionTimeout(systemConfig.getSessionTimeout());

        // 定时清理失效会话, 清理用户直接关闭浏览器造成的孤立会话
        sessionManager.setSessionValidationInterval(systemConfig.getSessionTimeoutClean());
        sessionManager.setSessionValidationSchedulerEnabled(true);

        // 开启cookie
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionIdCookieEnabled(true);

        return sessionManager;
    }

    /**
     * 指定本系统SESSIONID, 默认为: JSESSIONID 问题: 与SERVLET容器名冲突, 如JETTY, TOMCAT
     * 等默认JSESSIONID,当跳出SHIRO SERVLET时如ERROR-PAGE容器会为JSESSIONID重新分配值导致登录会话丢失!
     */
    private SimpleCookie sessionIdCookie() {
        return new SimpleCookie(systemConfig.getApplicationName() + SIMPLE_COOKIE_NAME_SUFFIX);
    }

    /** 自定义Session存储容器 */
    @Bean
    public SessionDAO sessionDAO(CustomSessionIdGenerator sessionIdGenerator, EhCacheManager shiroCacheManager) {
        CacheSessionDAO sessionDao = new CacheSessionDAO();
        sessionDao.setSessionIdGenerator(sessionIdGenerator);
        sessionDao.setCacheManager(shiroCacheManager);
        sessionDao.setActiveSessionsCacheName(CacheConstants.EHCACHE_SHIRO);
        return sessionDao;
    }

    //////// cache ////////

    /** shiro cacheManager配置 */
    @Bean("shiroCacheManager")
    public EhCacheManager shiroCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManager(ehCacheManager);
        return em;
    }

    //////// 开启shiro aop注解支持 ////////

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}
