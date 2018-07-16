package com.jee.learn.interfaces.dto;

import java.io.Serializable;

public class ResponseDto<T> implements Serializable {

    private static final long serialVersionUID = 554670716706197653L;

    private String c;// 返回码
    private String e;// 异常信息
    private T d;

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public T getD() {
        return d;
    }

    public void setD(T d) {
        this.d = d;
    }

}
