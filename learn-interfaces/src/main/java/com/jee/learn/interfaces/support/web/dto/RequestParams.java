package com.jee.learn.interfaces.support.web.dto;

import javax.validation.Valid;

import lombok.Data;

/**
 * 请求参数基础结构
 * 
 * @author 1002443
 * @version 1.0 2018年9月15日 下午4:17:55
 */
@Data
public class RequestParams<D extends DParam> {

    @Valid
    private HParam h;
    
    @Valid
    private D d;

}
