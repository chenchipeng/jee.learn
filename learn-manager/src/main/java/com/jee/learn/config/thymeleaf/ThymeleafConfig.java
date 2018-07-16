package com.jee.learn.config.thymeleaf;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;

import com.jee.learn.config.SysConfigBean;

/**
 * thymeleaf 配置<br/>
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年2月28日 上午11:32:57 1002360 新建
 */
@Component
public class ThymeleafConfig {

    /**
     * 全局静态变量配置<br/>
     * 参考:https://segmentfault.com/a/1190000012453362
     */
    @Autowired
    private void thymeleafStaticVars(ThymeleafViewResolver viewResolver, SysConfigBean configBean) {
        if (viewResolver != null) {
            Map<String, Object> vars = new HashMap<>();

            // 管理端页面路径
            vars.put("ctxa", configBean.getAdminPath());
            // 游客端页面路径
            vars.put("ctxf", configBean.getFrontPath());
            // 静态文件路径
            vars.put("ctxs", configBean.getStaticPath());

            viewResolver.setStaticVariables(vars);
        }
    }

}
