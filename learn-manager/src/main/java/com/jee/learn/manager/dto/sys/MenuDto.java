package com.jee.learn.manager.dto.sys;

import java.util.ArrayList;
import java.util.List;

import com.jee.learn.manager.domain.sys.SysMenu;

public class MenuDto extends SysMenu {

    private static final long serialVersionUID = 1L;

    public MenuDto() {
        super();
    }

    public MenuDto(List<MenuDto> childrenList) {
        super();
        this.childrenList = childrenList;
    }

    private List<MenuDto> childrenList;

    public void setEntity(SysMenu entity) {
        setId(entity.getId());
        setCreateBy(entity.getCreateBy());
        setCreateDate(entity.getCreateDate());
        setDelFlag(entity.getDelFlag());
        setHref(entity.getHref());
        setIcon(entity.getIcon());
        setIsShow(entity.getIsShow());
        setName(entity.getName());
        setParentId(entity.getParentId());
        setParentIds(entity.getParentIds());
        setPermission(entity.getPermission());
        setRemarks(entity.getRemarks());
        setSort(entity.getSort());
        setTarget(entity.getCreateBy());
        setUpdateBy(entity.getCreateBy());
        setUpdateDate(entity.getCreateDate());
    }

    public void addChildren(MenuDto module) {
        if (this.childrenList == null) {
            this.childrenList = new ArrayList<>();
        }
        this.childrenList.add(module);
    }

    public List<MenuDto> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(List<MenuDto> childrenList) {
        this.childrenList = childrenList;
    }

}
