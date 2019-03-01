package com.jee.learn.interfaces.support.web.dto;

import javax.validation.constraints.Digits;

import lombok.Data;

/**
 * d请求参数基类
 * 
 * @author 1002443
 * @version 1.0 2018年9月15日 下午4:26:44
 */
@Data
public class DParam {

    @Digits(integer = 1, fraction = 0)
    private Integer a;

}
