package com.jee.learn.manager.dto.sys;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jee.learn.manager.domain.sys.SysMenu;

public class MenuDto extends SysMenu {

    private static final long serialVersionUID = 1L;

    private String isShowDict;
    private String creater;
    private String updater;
    private List<MenuDto> childrenList;

    public MenuDto() {
        super();
    }

    public MenuDto(List<MenuDto> childrenList) {
        super();
        this.childrenList = childrenList;
    }

    public String getIsShowDict() {
        return isShowDict;
    }

    public void setIsShowDict(String isShowDict) {
        this.isShowDict = isShowDict;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    @JsonIgnore
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
