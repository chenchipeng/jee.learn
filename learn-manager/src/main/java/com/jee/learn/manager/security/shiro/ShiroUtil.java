package com.jee.learn.manager.security.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.jee.learn.manager.config.shiro.security.CustomPrincipal;
import com.jee.learn.manager.util.base.ExceptionUtil;

public class ShiroUtil {
    
    /**
     * 获取授权主要对象
     */
    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    /**
     * 获取当前登录者对象
     */
    public static CustomPrincipal getPrincipal() {
        try {
            Subject subject = SecurityUtils.getSubject();
            CustomPrincipal principal = (CustomPrincipal) subject.getPrincipal();
            if (principal != null) {
                return principal;
            }
        } catch (Exception e) {
            throw ExceptionUtil.unchecked(e);
        }
        return null;
    }

    /**
     * 获取当前用户的session
     * 
     * @return
     */
    public static Session getSession() {
        try {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession(false);
            if (session == null) {
                session = subject.getSession();
            }
            if (session != null) {
                return session;
            }
        } catch (InvalidSessionException e) {
            throw ExceptionUtil.unchecked(e);
        }
        return null;
    }

}
