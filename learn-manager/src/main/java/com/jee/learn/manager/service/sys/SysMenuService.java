package com.jee.learn.manager.service.sys;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.jee.learn.manager.domain.sys.SysMenu;
import com.jee.learn.manager.dto.sys.MenuDto;
import com.jee.learn.manager.support.dao.service.EntityService;

public interface SysMenuService extends EntityService<SysMenu, String> {
    
    /** 左侧菜单, 含部分必要属性 */
    int LEFT_MENU = 1;
    /** 树形列表, 含所有属性 */
    int TREE_LIST_MENU = 2;

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
     * @param dtoType menuDto属性集合[{@link SysMenuService#LEFT_MENU}:左侧菜单, {@link SysMenuService#TREE_LIST_MENU}:树形列表]
     * @return
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    MenuDto getCurrentUserMenu(int dtoType) throws IllegalAccessException, InvocationTargetException;

    /**
     * 使用递归方法建树
     * 
     * @param menuList
     * @param dtoType menuDto属性集合[{@link SysMenuService#LEFT_MENU}:左侧菜单, {@link SysMenuService#TREE_LIST_MENU}:树形列表]
     * @return
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    MenuDto listToTree(List<SysMenu> menuList, int dtoType) throws IllegalAccessException, InvocationTargetException;

}
