package com.jee.learn.manager.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    
    @Value("${system.name:后台管理系统}")
    private String systemName;

    @GetMapping("login")
    public String loginPage(Model model) {
        model.addAttribute("name", systemName);
        return "base/login";
    }

}
