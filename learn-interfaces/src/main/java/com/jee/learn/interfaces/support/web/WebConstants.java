package com.jee.learn.interfaces.support.web;

public class WebConstants {

    private WebConstants() {
        throw new IllegalStateException("Utility class");
    }

    /** 默认token参数 */
    public static final String DEFAULT_TOKEN = "3021e68df9a7200135725c6331369a22";

    public static final String SUCCESS_CODE = "200";
    public static final String SUCCESS_MESSAGE = "OK";

    public static final String BUSINESS_ERROR_CODE = "400";
    public static final String BUSINESS_ERROR_MESSAGE = "业务处理出错";

    public static final String RELOGIN_CODE = "401";
    public static final String RELOGIN_MESSAGE = "请重新登录";

    public static final String PARAMETER_ERROR_CODE = "10000";
    public static final String PARAMETER_ERROR_MESSAGE = "请求参数错误";

    public static final String RECORD_NOT_FOUND_CODE = "10001";
    public static final String RECORD_NOT_FOUND_MESSAGE = "记录不存在";

    public static final String INVALID_TOKEN_CODE = "10002";
    public static final String INVALID_TOKEN_MESSAGE = "无效TOKEN";
    
    public static final String INVALID_FILE_CODE = "10003";
    public static final String INVALID_FILE_MESSAGE = "无效文件";
    
    public static final String FILE_VALIDATE_CODE = "10004";
    public static final String FILE_VALIDATE_MESSAGE = "文件校验不通过";
    
    public static final String APP_IDENTITY_ERROR_CODE = "20001";
    public static final String APP_IDENTITY_ERROR_MESSAGE = "APPKEY错误";
    public static final String APP_SIGN_TIMEOUT_CODE = "20002";
    public static final String APP_SIGN_TIMEOUT_MESSAGE = "签名已过期";
    public static final String APP_SIGN_INVALID_CODE = "20003";
    public static final String APP_SIGN_INVALID_MESSAGE = "签名无效";

}
