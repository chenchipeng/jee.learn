package com.jee.learn.manager.dto.base;

/**
 * d请求参数基类
 * 
 * @author 1002443
 * @version 1.0 2018年9月15日 下午4:26:44
 */
public class DParam {

    private Integer a;
    private Integer b;
    private String c;
    private String t;

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

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "DParam [a=" + a + ", b=" + b + ", c=" + c + ", t=" + t + "]";
    }

}
