package com.jee.learn.manager.dto.base;

import java.io.Serializable;

public class DDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer a;
    private Integer b;
    private String c;
    private String d;

    public Integer getA() {
        return a;
    }

    public void setA(Integer a) {
        this.a = a;
    }

    public Integer getB() {
        return b;
    }

    public void setB(Integer b) {
        this.b = b;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    @Override
    public String toString() {
        return "DDto [a=" + a + ", b=" + b + ", c=" + c + ", d=" + d + "]";
    }

}
