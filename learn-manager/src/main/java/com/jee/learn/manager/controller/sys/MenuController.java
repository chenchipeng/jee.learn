package com.jee.learn.manager.controller.sys;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jee.learn.manager.controller.BaseController;
import com.jee.learn.manager.domain.sys.SysMenu;
import com.jee.learn.manager.security.UserUtil;
import com.jee.learn.manager.service.sys.SysMenuService;

@Controller
@RequestMapping("${system.authc-path}/sys/menu")
public class MenuController  extends BaseController{
    
    @Autowired
    private UserUtil userUtil;
    
    @Autowired
    private SysMenuService menuService;
    
    @Async
    @RequiresPermissions("sys:menu:list")
    @GetMapping(path= {"","/list"})
    public CompletableFuture<String> list() {
        return CompletableFuture.completedFuture("sys/menuList");
    }
    
    @Async
    @ResponseBody
    @RequiresPermissions("sys:menu:list")
    @PostMapping(path= "/list/data")
    public CompletableFuture<List<SysMenu>> listData() {
        
     List<SysMenu> menus =  menuService.findListByUserId(userUtil.getUser().getId());
        
        
        return CompletableFuture.completedFuture(menus);
    }

}
