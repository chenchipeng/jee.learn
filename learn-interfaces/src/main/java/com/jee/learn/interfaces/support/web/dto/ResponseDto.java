package com.jee.learn.interfaces.support.web.dto;

import lombok.Data;

/**
 * 接口返回基类
 * 
 * @author 1002443
 * @version 1.0 2018年9月15日 下午4:52:45
 */
@Data
public class ResponseDto<T> {

    private String c;
    private String e;
    private T d;

    public ResponseDto() {
        super();
    }

    public ResponseDto(String c) {
        super();
        this.c = c;
    }

    public ResponseDto(String c, T d) {
        super();
        this.c = c;
        this.d = d;
    }

    public ResponseDto(String c, String e) {
        super();
        this.c = c;
        this.e = e;
    }

}
