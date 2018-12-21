package com.jee.learn.manager.controller.sys;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jee.learn.manager.controller.BaseController;
import com.jee.learn.manager.dto.sys.MenuDto;
import com.jee.learn.manager.service.sys.SysMenuService;
import com.jee.learn.manager.util.mapper.JsonMapper;

@Controller
@RequestMapping("${system.authc-path}/sys/menu")
public class MenuController extends BaseController {

    @Autowired
    private SysMenuService menuService;

    @ModelAttribute
    public MenuDto get(@RequestParam(required = false) String id) {
        MenuDto entity = null;
        if (id != null) {
            entity = menuService.findOne(id);
        }
        if (entity == null) {
            entity = new MenuDto();
        }
        return entity;
    }

    @Async
    @RequiresPermissions("sys:menu:list")
    @GetMapping(path = { "", "/list" })
    public CompletableFuture<String> list() {
        return CompletableFuture.completedFuture("sys/menuList");
    }

    @Async
    @ResponseBody
    @RequiresPermissions("sys:menu:list")
    @PostMapping(path = "/list/data", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<Object> listData() {
        List<MenuDto> menus = menuService.getCurrentUserMenuDtoList();
        return CompletableFuture.completedFuture(menus);
    }

    @Async
    @RequiresPermissions("sys:menu:view")
    @GetMapping(path = "/view")
    public CompletableFuture<String> view(MenuDto entity, Model model) {
        model.addAttribute("menu", entity);
        model.addAttribute("title", "系统菜单");
        return CompletableFuture.completedFuture("sys/menuView");
    }

    @Async
    @RequiresPermissions(value = { "sys:menu:add", "sys:menu:edit" }, logical = Logical.OR)
    @GetMapping(path = "/form")
    public CompletableFuture<String> form(MenuDto entity, Model model) {
        view(entity, model);
        return CompletableFuture.completedFuture("sys/menuForm");
    }
    
    @Async
    @RequiresPermissions(value = { "sys:menu:add", "sys:menu:edit" }, logical = Logical.OR)
    @PostMapping(path = "/save")
    public CompletableFuture<String> save(MenuDto entity, Model model) {
    	// TODO bean校验
    	logger.info("=========================");
        logger.info("{}",JsonMapper.toJson(entity));
        return CompletableFuture.completedFuture(REDIRECT + systemConfig.getAuthcPath() + "/sys/menu/list");
    }

}
