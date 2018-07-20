package com.jee.learn.interfaces.dto.api;

import java.io.Serializable;
import java.util.List;

public class ApiUserDto implements Serializable {

    private static final long serialVersionUID = 190913149670387421L;

    private Integer f;// 功能标识
    private String id;
    private String loginName;
    private String token;

    private List<ApiUserDto> l;

    public Integer getF() {
        return f;
    }

    public void setF(Integer f) {
        this.f = f;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<ApiUserDto> getL() {
        return l;
    }

    public void setL(List<ApiUserDto> l) {
        this.l = l;
    }

}
