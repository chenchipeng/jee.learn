package com.jee.learn.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.jee.learn.common.util.Constant;
import com.jee.learn.dto.MenuDto;
import com.jee.learn.model.sys.SysMenu;

/**
 * 树形菜单工具
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年3月1日 上午8:50:38 1002360 新建
 */
public class TreeUtils {

    /**
     * 构建左侧导航菜单
     * 
     * @param orignMenuList
     *            数据源
     * @param rootLevel
     *            从第几级开始构建
     * @return
     */
    public final static List<MenuDto> buildNaviMenu(List<SysMenu> orignMenuList, int rootLevel) {
        List<MenuDto> orignList = buildNaviMenu(orignMenuList);
        return delRootElement(orignList, rootLevel);
    }

    /** 构建左侧导航菜单 */
    public final static List<MenuDto> buildNaviMenu(List<SysMenu> orignMenuList) {
        // 原始的数据=orignMenuList,最后的结果=resultMenuList
        List<SysMenu> resultMenuList = new ArrayList<SysMenu>();

        // 先找到所有的一级菜单
        for (SysMenu m : orignMenuList) {
            if (StringUtils.isBlank(m.getParentId()) || Constant.NO_0.equals(m.getParentId())) {
                resultMenuList.add(m);
            }
        }
        // 为一级菜单设置子菜单，getChild是递归调用的
        // for (SysMenu m : resultMenuList) {
        // m.setChildren(getChild(m.getId(), orignMenuList));
        // }
        List<MenuDto> dtoList = changeDto(resultMenuList);
        return dtoList;
    }

    /**
     * 递归查找子菜单
     * 
     * @param menuId
     *            当前菜单id
     * @param rootMenu
     *            要查找的列表
     * @return
     */
    @Deprecated
    protected final static List<SysMenu> getChild(String menuId, List<SysMenu> rootMenu) {

        // 子菜单
        List<SysMenu> childList = new ArrayList<>();

        // 遍历所有节点，将父菜单id与传过来的id比较
        for (SysMenu m : rootMenu) {
            if (StringUtils.isNotBlank(m.getParentId())) {
                if (m.getParentId().equals(menuId)) {
                    childList.add(m);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (SysMenu m : childList) {
            // 没有url子菜单还有子菜单
            if (StringUtils.isBlank(m.getHref())) {
                // 递归
                m.setChildren(getChild(m.getId(), rootMenu));
            }
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

    /** 递归删除根节点 */
    private final static List<MenuDto> delRootElement(List<MenuDto> orignList, int rootLevel) {

        if (rootLevel <= 1) {
            return orignList;
        }

        List<MenuDto> resultList = new ArrayList<>();
        for (int i = 0, s = orignList.size(); i < s; i++) {
            if (orignList.get(i) != null || CollectionUtils.isNotEmpty(orignList.get(i).getChildren())) {
                resultList.addAll(orignList.get(i).getChildren());
            }
        }

        // 递归删除根节点
        rootLevel = rootLevel - 1;
        resultList = delRootElement(resultList, rootLevel);

        return resultList;
    }

    /** DTO转换 */
    private final static List<MenuDto> changeDto(List<SysMenu> rootMenu) {

        List<MenuDto> mdl = new ArrayList<>();

        for (SysMenu sysMenu : rootMenu) {

            if (Constant.NO_0.equals(sysMenu.getIsShow())) {
                continue;
            }

            MenuDto md = new MenuDto();
            md.setId(sysMenu.getId());
            md.setParentId(sysMenu.getParentId());
            md.setText(sysMenu.getName());
            md.setIcon(sysMenu.getIcon());
            md.setHref(sysMenu.getHref());
            md.setTarget(sysMenu.getTarget());

            if (CollectionUtils.isNotEmpty(sysMenu.getChildren())) {
                md.setChildren(changeDto(sysMenu.getChildren()));
            }
            mdl.add(md);
        }
        return mdl;
    }

}
