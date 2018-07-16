package com.jee.learn.interfaces.util;

/**
 * 返回信息常量池
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月14日 下午4:41:27 1002360 新建
 */
public interface WebConstants {

    String SUCCESS_CODE = "200";
    String SUCCESS_MESSAGE = "success";

    String BUSINESS_ERROR_CODE = "400";
    String BUSINESS_ERROR_MESSAGE = "业务处理出错";

    String PARAMETER_ERROR_CODE = "10000";
    String PARAMETER_ERROR_MESSAGE = "请求参数错误";

    String RECORD_NOT_FOUND_CODE = "10001";
    String RECORD_NOT_FOUND_MESSAGE = "记录不存在";
    
}
