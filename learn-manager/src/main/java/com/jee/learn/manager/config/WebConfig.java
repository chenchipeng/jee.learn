package com.jee.learn.manager.config;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

/**
 * mvc config
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月8日 下午2:11:36 ccp 新建
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Resource(name = "thymeleafViewResolver")
    private ThymeleafViewResolver thymeleafViewResolver;

    @Autowired
    private SystemConfig systemConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {

        if (thymeleafViewResolver != null) {
            // 可定义页面全局变量
            Map<String, Object> vars = new HashMap<>(2);
            vars.put("authcPath", systemConfig.getAuthcPath());
            vars.put("guestPath", systemConfig.getGuestPath());
            thymeleafViewResolver.setStaticVariables(vars);
        }

        WebMvcConfigurer.super.configureViewResolvers(registry);
    }

}
