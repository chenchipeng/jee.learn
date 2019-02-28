package com.jee.learn.interfaces.util;

/**
 * 返回信息常量池
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月14日 下午4:41:27 1002360 新建
 */
public class WebConstants {

    private WebConstants() {
        throw new IllegalStateException("Constants class");
    }

    public static final String SUCCESS_CODE = "200";
    public static final String SUCCESS_MESSAGE = "success";

    public static final String BUSINESS_ERROR_CODE = "400";
    public static final String BUSINESS_ERROR_MESSAGE = "业务处理出错";

    public static final String PARAMETER_ERROR_CODE = "10000";
    public static final String PARAMETER_ERROR_MESSAGE = "请求参数错误";

    public static final String RECORD_NOT_FOUND_CODE = "10001";
    public static final String RECORD_NOT_FOUND_MESSAGE = "记录不存在";

}
