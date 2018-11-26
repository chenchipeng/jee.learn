package com.jee.learn.manager.controller.sys;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jee.learn.manager.controller.BaseController;
import com.jee.learn.manager.dto.sys.MenuDto;
import com.jee.learn.manager.service.sys.SysMenuService;

@Controller
@RequestMapping("${system.authc-path}/sys/menu")
public class MenuController extends BaseController {

	@Autowired
	private SysMenuService menuService;
	
	@Async
	@RequiresPermissions("sys:menu:view")
	@GetMapping(path =  "/view" )
	public CompletableFuture<String> view() {
		return CompletableFuture.completedFuture("sys/menuView");
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

}
