package com.jee.learn.manager.config.shiro;

/**
 * 用户和密码/验证码 令牌类
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月8日 下午2:47:44 ccp 新建
 */
public class CustomToken extends org.apache.shiro.authc.UsernamePasswordToken {
    private static final long serialVersionUID = 1L;

    private String captcha;

    public CustomToken() {
        super();
    }

    public CustomToken(String username, char[] password, boolean rememberMe, String host, String captcha) {
        super(username, password, rememberMe, host);
        this.captcha = captcha;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

}