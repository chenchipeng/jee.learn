package com.jee.learn.manager.util.base.excrption;

import com.jee.learn.manager.util.WebConstants;

/**
 * 自定义异常, 用于在同一拦截的时候返回json
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年11月15日 下午12:54:58 ccp 新建
 */
public class RestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;

    protected RestException(String message) {
        super(message);
    }

    protected RestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestException() {
        super();
    }

    public RestException(Throwable cause) {
        super(WebConstants.BUSINESS_ERROR_MESSAGE, cause);
        this.code = WebConstants.BUSINESS_ERROR_CODE;
    }

    public RestException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
