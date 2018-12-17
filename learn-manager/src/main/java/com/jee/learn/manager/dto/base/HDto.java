package com.jee.learn.manager.dto.base;

import java.io.Serializable;

public class HDto implements Serializable{

    private static final long serialVersionUID = 1L;
    private String c;
    private String t;
    private String e;
    private DDto d;

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

    public DDto getD() {
        return d;
    }

    public void setD(DDto d) {
        this.d = d;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

}
