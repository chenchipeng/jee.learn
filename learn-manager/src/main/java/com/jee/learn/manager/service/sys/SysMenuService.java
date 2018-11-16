package com.jee.learn.manager.service.sys;

import java.util.List;

import com.jee.learn.manager.domain.sys.SysMenu;
import com.jee.learn.manager.dto.ResponseDto;
import com.jee.learn.manager.dto.sys.MenuDto;
import com.jee.learn.manager.support.dao.service.EntityService;

public interface SysMenuService extends EntityService<SysMenu, String> {

    /** 当{@link SysMenu#getDelFlag()} 等于 -1 时表示获取当前用户所拥有的菜单 */
    String USER_PRIVATE_MENU_TAG = "-1";

    /**
     * 根据用户id查找其有用的菜单
     * 
     * @param userId
     * @return
     */
    List<SysMenu> findListByUserId(String userId);

    /**
     * 获取当前用户的菜单
     * 
     * @return
     */
    ResponseDto<MenuDto> getCurrentUserMenu();

}
