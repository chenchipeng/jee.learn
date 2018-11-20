package com.jee.learn.manager.controller.sys;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.collections.CollectionUtils;
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
import com.jee.learn.manager.dto.ResponseDto;
import com.jee.learn.manager.dto.sys.MenuDto;
import com.jee.learn.manager.service.sys.SysMenuService;
import com.jee.learn.manager.util.WebConstants;
import com.jee.learn.manager.util.base.excrption.RestException;

@Controller
@RequestMapping("${system.authc-path}/sys/menu")
public class MenuController extends BaseController {

    @Autowired
    private SysMenuService menuService;

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
    public CompletableFuture<ResponseDto<MenuDto>> listData() {
        MenuDto menuDto = null;
        try {
            menuDto = menuService.getCurrentUserMenu(SysMenuService.TREE_LIST_MENU);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RestException(e);
        }
        ResponseDto<MenuDto> result = new ResponseDto<>(WebConstants.SUCCESS_CODE);
        if (menuDto != null && !CollectionUtils.isEmpty(menuDto.getChildrenList())
                && menuDto.getChildrenList().get(0) != null) {
            result.setD(menuDto.getChildrenList().get(0));
        }
        return CompletableFuture.completedFuture(result);
    }

}
