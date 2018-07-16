/**
 * Copyright &copy; 2015-2020 <a href="http://www.chinaskin.net/">chnskin</a> All rights reserved.
 */
package com.jee.learn.component.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jee.learn.component.util.UserUtils;
import com.jee.learn.util.Servlets;
import com.jee.learn.util.json.AjaxJson;
import com.jee.learn.util.json.PrintJSON;

/**
 * 表单验证（包含验证码）过滤类
 * 
 * @author chnskin
 * @version 2014-5-19
 */
@Component
public class LoginFormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

    @Autowired
    private UserUtils userUtils;

    public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";
    public static final String DEFAULT_MOBILE_PARAM = "mobileLogin";
    public static final String DEFAULT_MESSAGE_PARAM = "message";

    /** realmType - 根据传入的realm名称选择不同的认证规则 */
    public static final String DEFAULT_REALMTYPE_PARAM = "rt";
    /** isApiLogin - 是否通过API实现前后端分离登录 */
    public static final String DEFAULT_APILOGIN_PARAM = "al";

    private String captchaParam = DEFAULT_CAPTCHA_PARAM;
    private String mobileLoginParam = DEFAULT_MOBILE_PARAM;
    private String messageParam = DEFAULT_MESSAGE_PARAM;
    private String realmTypeParam = DEFAULT_REALMTYPE_PARAM;
    private String apiLoginParam = DEFAULT_APILOGIN_PARAM;

    /**
     * 登录成功之后跳转URL
     */
    @Override
    public String getSuccessUrl() {
        return super.getSuccessUrl();
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        String username = getUsername(request);
        String password = getPassword(request);
        if (password == null) {
            password = StringUtils.EMPTY;
        }
        boolean rememberMe = isRememberMe(request);
        String host = Servlets.getRemoteAddr((HttpServletRequest) request);
        String captcha = getCaptcha(request);
        boolean mobile = isMobileLogin(request);
        String realmType = getRealmType(request);
        boolean api = isApiLogin(request);

        return new CustomUsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha, mobile,
                realmType, api);
    }

    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        Principal p = userUtils.getPrincipal();
        
        if(p!=null&&p.isApiLogin()) {
            System.out.println("=====================================");
            
            return;
        }
        
        
        
        if (p != null && !p.isMobileLogin()) {
            WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
        } else {
            // super.issueSuccessRedirect(request, response);//手机登录
            AjaxJson j = new AjaxJson();
            j.setSuccess(true);
            j.setMsg("登录成功!");
            j.put("username", p.getLoginName());
            j.put("name", p.getName());
            j.put("mobileLogin", p.isMobileLogin());
            j.put("JSESSIONID", getSessionid());
            PrintJSON.write((HttpServletResponse) response, j.getJsonStr());
        }
    }

    /**
     * 登录失败调用事件
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
            ServletResponse response) {
        String className = e.getClass().getName(), message = StringUtils.EMPTY;
        if (IncorrectCredentialsException.class.getName().equals(className)
                || UnknownAccountException.class.getName().equals(className)) {
            message = "用户或密码错误, 请重试.";
        } else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), "msg:")) {
            message = StringUtils.replace(e.getMessage(), "msg:", StringUtils.EMPTY);
        } else {
            message = "系统出现点问题，请稍后再试！";
            // 输出到控制台
            e.printStackTrace();
        }
        request.setAttribute(getFailureKeyAttribute(), className);
        request.setAttribute(getMessageParam(), message);
        return true;
    }

    /**
     * 获取SESSIONID
     */
    private String getSessionid() {
        try {
            return (String) userUtils.getSession().getId();
        } catch (Exception e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, getCaptchaParam());
    }

    protected boolean isMobileLogin(ServletRequest request) {
        return WebUtils.isTrue(request, getMobileLoginParam());
    }

    protected String getRealmType(ServletRequest request) {
        return WebUtils.getCleanParam(request, getRealmTypeParam());
    }

    protected boolean isApiLogin(ServletRequest request) {
        return WebUtils.isTrue(request, getApiLoginParam());
    }

    public String getCaptchaParam() {
        return captchaParam;
    }

    public String getMobileLoginParam() {
        return mobileLoginParam;
    }

    public String getMessageParam() {
        return messageParam;
    }

    public String getRealmTypeParam() {
        return realmTypeParam;
    }

    public String getApiLoginParam() {
        return apiLoginParam;
    }

}