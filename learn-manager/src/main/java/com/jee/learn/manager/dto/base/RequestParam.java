package com.jee.learn.manager.dto.base;

import java.io.Serializable;

public class RequestParam<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected HParam h;// 通用属性
    protected T d;// 具体的数据结构

    public HParam getH() {
        return h;
    }

    public void setH(HParam h) {
        this.h = h;
    }

    public T getD() {
        return d;
    }

    public void setD(T d) {
        this.d = d;
    }

}
