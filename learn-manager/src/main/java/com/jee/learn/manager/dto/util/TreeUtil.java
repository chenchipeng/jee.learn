package com.jee.learn.manager.dto.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.jee.learn.manager.dto.sys.MenuDto;

/**
 * 树形菜单小工具<br/>
 * 参考:https://blog.csdn.net/MassiveStars/article/details/53911620
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年11月5日 上午11:14:38 ccp 新建
 */
public class TreeUtil {

    /**
     * 使用递归方法建树
     * 
     * @param treeNodes
     * @return
     */
    public static List<MenuDto> listToTree(List<MenuDto> treeNodes) {
        List<MenuDto> trees = new ArrayList<MenuDto>();
        for (MenuDto treeNode : treeNodes) {
            if (StringUtils.isBlank(treeNode.getParentId()) || "0".equals(treeNode.getParentId())) {
                trees.add(buildByRecursive(treeNode, treeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     * 
     * @param treeNodes
     * @return
     */
    private static MenuDto buildByRecursive(MenuDto treeNode, List<MenuDto> treeNodes) {
        for (MenuDto it : treeNodes) {
            if (treeNode.getId().equals(it.getParentId())) {
                if (treeNode.getChildrenList() == null) {
                    treeNode.setChildrenList(new ArrayList<MenuDto>());
                }
                treeNode.getChildrenList().add(buildByRecursive(it, treeNodes));
            }
        }
        return treeNode;
    }

    /**
     * 将树组装成list
     * 
     * @param tree
     * @return
     */
    public static List<MenuDto> treeToList(List<MenuDto> tree) {
        List<MenuDto> list = new ArrayList<>();

        for (MenuDto note : tree) {
            list.add(note);
            if (!CollectionUtils.isEmpty(note.getChildrenList())) {
                list.addAll(treeToList(note.getChildrenList()));
            }
            note.setChildrenList(null);
        }

        return list;
    }

}
