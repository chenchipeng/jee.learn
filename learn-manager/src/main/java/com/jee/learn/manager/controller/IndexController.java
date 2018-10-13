package com.jee.learn.manager.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.jee.learn.manager.config.SystemConfig;

/**
 * 登陆/首页 页面controller
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月8日 上午11:19:05 ccp 新建
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private SystemConfig systemConfig;

    @GetMapping("${system.authc-path}/login")
    public String loginPage(Model model) {

        // 1.统计session活跃人数

        // 2.如果已登录，再次访问主页，则退出原账号。

        // 3.如果已经登录，则跳转到管理首页

        model.addAttribute("name", systemConfig.getName());
        return "main/login";
    }

    @PostMapping("${system.authc-path}/login")
    public String loginFail() {

        // 如果已经登录，则跳转到管理首页

        logger.debug("loginFail...");

        return "main/login";
    }

    @RequiresPermissions("user")
    @GetMapping("${system.authc-path}")
    public String indexPage() {
        return "hello";
    }

}
