package com.jee.learn.interfaces.dto;

public class RequestDto<T> {

    protected HeadersDto h;// 通用属性
    protected T d;// 具体的数据结构

    public HeadersDto getH() {
        return h;
    }

    public void setH(HeadersDto h) {
        this.h = h;
    }

    public T getD() {
        return d;
    }

    public void setD(T d) {
        this.d = d;
    }

}
