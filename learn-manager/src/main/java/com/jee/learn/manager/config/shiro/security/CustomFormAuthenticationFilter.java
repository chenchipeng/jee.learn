package com.jee.learn.manager.config.shiro.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jee.learn.manager.config.shiro.ShiroContants;

/**
 * 表单登录拦截器
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月16日 上午9:46:55 ccp 新建
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {

        logger.debug("CustomFormAuthenticationFilter createToken");

        String username = getUsername(request);
        String password = getPassword(request);
        password = password == null ? StringUtils.EMPTY : password;
        boolean isRememberMe = isRememberMe(request);
        String host = getHost(request);
        String captcha = getCaptcha(request);

        return new CustomToken(username, password.toCharArray(), isRememberMe, host, captcha);
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
            ServletResponse response) {

        logger.debug("CustomFormAuthenticationFilter onLoginFailure");

        String className = e.getClass().getName(), message = StringUtils.EMPTY;
        if (IncorrectCredentialsException.class.getName().equals(className)
                || UnknownAccountException.class.getName().equals(className)) {
            message = ShiroContants.USERNAME_PASSWORD_ERROR;
        } else if (e.getMessage() != null && StringUtils.startsWith(e.getMessage(), ShiroContants.MESSAGE_PREFIX)) {
            message = StringUtils.replace(e.getMessage(), ShiroContants.MESSAGE_PREFIX, StringUtils.EMPTY);
        } else {
            message = ShiroContants.SYSTEM_ERROR;
            // 输出到控制台
            logger.info("用户登录异常", e);
        }

        // 输出到页面
        request.setAttribute(getFailureKeyAttribute(), className);
        request.setAttribute(ShiroContants.DEFAULT_MESSAGE_PARAM, message);

        return true;
    }

    @Override
    public String getSuccessUrl() {
        return super.getSuccessUrl();
    }

    @Override
    protected void issueSuccessRedirect(ServletRequest request, ServletResponse response) throws Exception {
        WebUtils.issueRedirect(request, response, getSuccessUrl(), null, true);
    }

    protected String getCaptcha(ServletRequest request) {
        return WebUtils.getCleanParam(request, ShiroContants.DEFAULT_CAPTCHA_PARAM);
    }

}
