package com.jee.learn.dto;

import java.io.Serializable;
import java.util.List;

public class MenuDto implements Serializable {

    private static final long serialVersionUID = -5047882205062928253L;

    private String id;
    private String text;
    private String icon;
    private String href;
    private String target;
    private String parentId;
    private boolean isOpen;
    private List<MenuDto> children;

    public MenuDto() {
    }

    public MenuDto(String id, String text, String icon, String href) {
        this.id = id;
        this.text = text;
        this.icon = icon;
        this.href = href;
    }

    public MenuDto(String id, String text, String icon, String href, boolean isOpen) {
        this.id = id;
        this.text = text;
        this.icon = icon;
        this.href = href;
        this.isOpen = isOpen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public List<MenuDto> getChildren() {
        return children;
    }

    public void setChildren(List<MenuDto> children) {
        this.children = children;
    }

}
