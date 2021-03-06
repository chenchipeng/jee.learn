package com.jee.learn.manager.config.shiro;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.jee.learn.manager.config.SystemConfig;
import com.jee.learn.manager.config.shiro.security.CustomFormAuthenticationFilter;
import com.jee.learn.manager.config.shiro.session.CacheSessionDAO;
import com.jee.learn.manager.config.shiro.session.CustomSessionDAO;
import com.jee.learn.manager.config.shiro.session.CustomSessionIdGenerator;
import com.jee.learn.manager.config.shiro.session.CustomWebSessionManager;
import com.jee.learn.manager.config.shiro.session.JedisSessionDAO;
import com.jee.learn.manager.security.shiro.CustomRealm;
import com.jee.learn.manager.support.cache.CacheConstants;
import com.jee.learn.manager.util.security.CryptoUtil;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import net.sf.ehcache.CacheManager;

/**
 * shiro config<br/>
 * 参考:https://blog.csdn.net/weixin_38132621/article/details/80216056 参考:https://blog.csdn.net/qq_34021712/article/details/80791219
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
    private static final String REMEMBER_ME_SUFFIX = ".rememberMe";
    private static final Integer REMEMBER_ME_MAX_AGE = 259200;// 三天内"记住我"

    @Autowired
    private SystemConfig systemConfig;
    @Autowired
    private CacheManager ehCacheManager;

    //////// shiro 拦截配置 ////////

    /** shiro url 拦截配置 */
    private Map<String, String> urlFilter() {
        // 设置拦截器
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>(9);

        // 开放测试环境
        filterChainDefinitionMap.put("/test/**", "anon");
        // 开放静态资源
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/plugins/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");

        // 登录页面拦截
        filterChainDefinitionMap.put(systemConfig.getAuthcPath() + "/login", "authc");
        filterChainDefinitionMap.put(systemConfig.getAuthcPath() + "/logout", "logout");

        // 游客，开放权限
        filterChainDefinitionMap.put(systemConfig.getGuestPath() + ANY_REQUEST, "anon");
        // 用户，需要权限 "user"
        filterChainDefinitionMap.put(systemConfig.getAuthcPath() + ANY_REQUEST, "user");

        // 拦截所有
        filterChainDefinitionMap.put(ANY_REQUEST, "user");

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

        LOGGER.debug("shiro 拦截 注入成功");
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

        // 设置cacheManager 小测后发现该设置会缓存权鉴信息, 避免doGetAuthorizationInfo()重复执行
        securityManager.setCacheManager(shiroCacheManager);

        // 注入记住我管理器
        securityManager.setRememberMeManager(rememberMeManager());

        return securityManager;
    }

    //////// shiro sessionManager配置 ////////

    @Bean("sessionManager")
    public CustomWebSessionManager sessionManager(CustomSessionDAO sessionDAO) {
        CustomWebSessionManager sessionManager = new CustomWebSessionManager();

        // Session存储容器
        sessionManager.setSessionDAO(sessionDAO);

        // 会话超时时间，单位：毫秒
        sessionManager.setGlobalSessionTimeout(systemConfig.getSessionTimeout());

        // 定时清理失效会话, 清理用户直接关闭浏览器造成的孤立会话, 用户量过多会影响性能
        sessionManager.setSessionValidationInterval(systemConfig.getSessionTimeoutClean());
        sessionManager.setSessionValidationSchedulerEnabled(true);

        // 开启cookie
        sessionManager.setSessionIdCookie(sessionIdCookie());
        sessionManager.setSessionIdCookieEnabled(true);

        return sessionManager;
    }

    /**
     * 指定本系统SESSIONID, 默认为: JSESSIONID 问题: 与SERVLET容器名冲突, 如JETTY, TOMCAT 等默认JSESSIONID,当跳出SHIRO SERVLET时如ERROR-PAGE容器会为JSESSIONID重新分配值导致登录会话丢失!
     */
    private SimpleCookie sessionIdCookie() {
        return new SimpleCookie(systemConfig.getApplicationName() + SIMPLE_COOKIE_NAME_SUFFIX);
    }

    /** 自定义Session存储容器 */
    @Bean
    public CustomSessionDAO sessionDAO(CustomSessionIdGenerator sessionIdGenerator, EhCacheManager shiroCacheManager) {
        if (SystemConfig.EHCACHE_NAME.equals(systemConfig.getShiroCacherName())) {
            CacheSessionDAO sessionDao = new CacheSessionDAO();
            sessionDao.setSessionIdGenerator(sessionIdGenerator);
            sessionDao.setCacheManager(shiroCacheManager);
            sessionDao.setActiveSessionsCacheName(CacheConstants.EHCACHE_SHIRO);
            return sessionDao;
        }
        if (SystemConfig.REDIS_NAME.equals(systemConfig.getShiroCacherName())) {
            JedisSessionDAO sessionDao = new JedisSessionDAO();
            sessionDao.setSessionIdGenerator(sessionIdGenerator);
            return sessionDao;
        }
        LOGGER.debug("没有指定 自定义Session存储容器");
        return null;
    }

    //////// cache ////////

    /** shiro cacheManager配置 */
    @Bean("shiroCacheManager")
    public EhCacheManager shiroCacheManager() {
        EhCacheManager em = new EhCacheManager();
        em.setCacheManager(ehCacheManager);
        return em;
    }

    //////// rememberMe ////////

    /**
     * rememberMeManager, 将其设置到securityManager中
     * 
     * @return
     */
    private CookieRememberMeManager rememberMeManager() {

        SimpleCookie simpleCookie = new SimpleCookie(systemConfig.getApplicationName() + REMEMBER_ME_SUFFIX);
        // 记住我cookie生效时间, 单位秒
        simpleCookie.setMaxAge(REMEMBER_ME_MAX_AGE);

        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(simpleCookie);

        // rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 192 256 位)
        cookieRememberMeManager.setCipherKey(CryptoUtil.generateAesKey(128));

        return cookieRememberMeManager;
    }

    //////// 开启shiro aop注解支持 ////////

    /** Shiro 过滤器代理配置 */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

    /** 启用Shrio授权注解拦截方式，AOP式方法级权限检查 */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    //////// 开启异步支持 ////////

    @Bean
    public FilterRegistrationBean<DelegatingFilterProxy> filterRegistrationBean() {
        FilterRegistrationBean<DelegatingFilterProxy> registration = new FilterRegistrationBean<DelegatingFilterProxy>();
        registration.setFilter(new DelegatingFilterProxy());
        registration.setName("shiroFilter");
        registration.addInitParameter("targetFilterLifecycle", "true");
        registration.addUrlPatterns("/*");
        registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
        return registration;
    }

    //////// 用于thymeleaf模板使用shiro标签 ////////

    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

}
