package com.jee.learn.interfaces.util.exception;

/**
 * 自定义接口异常<br/>
 * IntfcException->RuntimeException->Exception
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月11日 上午9:31:28 1002360 新建
 */
public class IntfcException extends RuntimeException {

    private static final long serialVersionUID = -1705530401459534119L;

    private String code;
    private String msg;

    public IntfcException() {
        super();
    }

    public IntfcException(String message) {
        super(message);
    }

    public IntfcException(String code, String msg) {
        super("错误码=" + code + " 异常信息=" + msg);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
