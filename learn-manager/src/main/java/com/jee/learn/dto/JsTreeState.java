package com.jee.learn.dto;

import java.io.Serializable;

public class JsTreeState implements Serializable {

    private static final long serialVersionUID = 1787205651918583626L;

    public Boolean opened;

    public Boolean disabled;

    public Boolean selected;

    public JsTreeState() {
    }

    public JsTreeState(Boolean opened) {
        this.opened = opened;
    }

    public Boolean getOpened() {
        return opened;
    }

    public void setOpened(Boolean opened) {
        this.opened = opened;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}
