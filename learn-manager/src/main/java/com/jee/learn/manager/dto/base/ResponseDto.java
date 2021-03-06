package com.jee.learn.manager.dto.base;

import java.io.Serializable;

public class ResponseDto<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private String c;// 返回码
    private String e;// 异常信息
    private T d;

    public ResponseDto() {
        super();
    }

    public ResponseDto(String c) {
        super();
        this.c = c;
    }

    public ResponseDto(String c, String e) {
        super();
        this.c = c;
        this.e = e;
    }

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
