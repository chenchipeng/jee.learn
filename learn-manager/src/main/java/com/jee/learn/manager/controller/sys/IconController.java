package com.jee.learn.manager.controller.sys;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jee.learn.manager.controller.BaseController;

/**
 * Font Awesome 图标选择
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年12月13日 下午6:25:20 ccp 新建
 */
@Controller
@RequestMapping("${system.authc-path}/icon")
public class IconController extends BaseController {

    @RequiresPermissions("user")
    @GetMapping(value = "select")
    public String select(HttpServletRequest request, Model model) {
        model.addAttribute("value", request.getParameter("value"));
        return "sys/iconSelect";
    }

}
