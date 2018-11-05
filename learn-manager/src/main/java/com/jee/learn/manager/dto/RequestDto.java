package com.jee.learn.manager.dto;

import java.io.Serializable;

public class RequestDto<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    protected HeaderDto h;// 通用属性
    protected T d;// 具体的数据结构

    public HeaderDto getH() {
        return h;
    }

    public void setH(HeaderDto h) {
        this.h = h;
    }

    public T getD() {
        return d;
    }

    public void setD(T d) {
        this.d = d;
    }

}
