package com.jee.learn.manager.util;

/**
 * 接口常量池
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年11月2日 下午8:15:50 ccp 新建
 */
public class WebConstants {

    private WebConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SUCCESS_CODE = "200";
    public static final String SUCCESS_MESSAGE = "success";

    public static final String BUSINESS_ERROR_CODE = "400";
    public static final String BUSINESS_ERROR_MESSAGE = "业务处理出错";

    public static final String PARAMETER_ERROR_CODE = "10000";
    public static final String PARAMETER_ERROR_MESSAGE = "请求参数错误";

    public static final String RECORD_NOT_FOUND_CODE = "10001";
    public static final String RECORD_NOT_FOUND_MESSAGE = "记录不存在";

    public static final String INVALID_FILE_TYPE_CODE = "10002";
    public static final String INVALID_FILE_TYPE_MESSAGE = "文件类型不匹配";

    public static final String FILE_NOT_FOUND_CODE = "10003";
    public static final String FILE_NOT_FOUND_MESSAGE = "文件不存在";
    
    public static final String FILE_CHECK_ERROR_CODE = "10004";
    public static final String FILE_CHECK_ERROR_MESSAGE = "文件校验不通过";

}
