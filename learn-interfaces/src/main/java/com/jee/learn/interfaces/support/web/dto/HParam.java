package com.jee.learn.interfaces.support.web.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * h请求参数
 * 
 * @author 1002443
 * @version 1.0 2018年9月15日 下午4:30:06
 */
@Data
public class HParam {

    @NotNull
    private String t;
    
    private String ip;

    private String k; // APPKEY
    private String s; // SIGN
    private String i; // TIME STAMP

}
