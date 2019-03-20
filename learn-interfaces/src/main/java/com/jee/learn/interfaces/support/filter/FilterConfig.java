package com.jee.learn.interfaces.support.filter;

/**
 * Filter 配置样例代码
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月20日 下午3:08:26 ccp 新建
 */
// @org.springframework.beans.factory.annotation.Configurable
public class FilterConfig {
    /**
     * <code>
    
    //////// 请求参数拦截 ////////
    
    // @org.springframework.context.annotation.Bean
    public FilterRegistrationBean<ParameterFilter> parameterFilter() {
        FilterRegistrationBean<ParameterFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(createParameterFilter());
        registration.addUrlPatterns("/*");
        registration.setName("parameterFilter");
        registration.setAsyncSupported(true);
    
        return registration;
    }
    
    // @org.springframework.context.annotation.Bean
    public ParameterFilter createParameterFilter() {
        return new ParameterFilter();
    }
    
     * </code>
     */
}
