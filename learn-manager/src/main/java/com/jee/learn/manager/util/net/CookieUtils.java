package com.jee.learn.manager.util.net;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jee.learn.manager.util.base.ExceptionUtil;
import com.jee.learn.manager.util.text.EncodeUtils;

public class CookieUtils {

    /** cookie存活三天 */
    public static final int THREE_DAY_COOKIE = 259200;
    /** cookie存活三十分钟 */
    public static final int THREE_MINUTE_COOKIE = 1800;
    /** 会话cookie, 浏览器关闭后清除 */
    public static final int SESSION_COOKIE = -1;

    /**
     * 根据Cookie名称得到Cookie对象，不存在该对象则返回Null
     * 
     * @param request
     * @param name
     * @return
     */
    private static Cookie getCookieObj(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        Cookie cookie = null;
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) {
                cookie = c;
                break;
            }
        }
        return cookie;
    }

    /**
     * 设置cookie对象
     * 
     * @param response
     * @param cookie
     */
    private static void setCookieObj(HttpServletResponse response, Cookie cookie) {
        response.addCookie(cookie);
        try {
            response.flushBuffer();
        } catch (IOException e) {
            ExceptionUtil.unchecked(e);
        }
    }

    /**
     * 根据Cookie名称直接得到Cookie值
     * 
     * @param request
     * @param name
     * @return
     */
    public static String getCookie(HttpServletRequest request, String name) {
        Cookie cookie = getCookieObj(request, name);
        if (cookie != null) {
            return EncodeUtils.decodeUrl(cookie.getValue());
        }
        return null;
    }

    /**
     * 添加一条新的Cookie，可以指定过期时间(单位：秒)
     * 
     * @param response
     * @param name
     * @param value
     * @param maxValue
     */
    public static void setCookie(HttpServletResponse response, String name, String value, int maxValue) {
        if (StringUtils.isBlank(name)) {
            return;
        }
        if (null == value) {
            value = StringUtils.EMPTY;
        }
        Cookie cookie = new Cookie(name, EncodeUtils.encodeUrl(value));
        cookie.setPath("/");
        cookie.setMaxAge(maxValue);
        setCookieObj(response, cookie);
    }

    /**
     * 添加一条新的Cookie，默认30分钟过期时间
     * 
     * @param response
     * @param name
     * @param value
     */
    public static void setCookie(HttpServletResponse response, String name, String value) {
        setCookie(response, name, value, THREE_MINUTE_COOKIE);
    }

    /**
     * 移除cookie
     * 
     * @param request
     * @param response
     * @param name 这个是名称，不是值
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        if (null == name) {
            return;
        }
        Cookie cookie = getCookieObj(request, name);
        if (null != cookie) {
            cookie.setMaxAge(0);
            setCookieObj(response, cookie);
        }
    }

    /**
     * 将cookie封装到Map里面
     * 
     * @param request
     * @return
     */
    public static Map<String, Cookie> transMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

    //////// spring ////////

    /**
     * 根据名称获取cookie值
     * 
     * @param name
     * @return
     */
    public static String getCookie(String name) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return getCookie(request, name);
    }

    /**
     * 设置cookie值
     * 
     * @param name
     * @param value
     * @param seconds 有效期
     */
    public static void setCookie(String name, String value, int seconds) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        Cookie cookie = getCookieObj(request, name);
        if (cookie == null) {
            setCookie(response, name, value, seconds);
            return;
        }
        cookie.setValue(EncodeUtils.encodeUrl(value));
        cookie.setMaxAge(seconds);
        setCookieObj(response, cookie);
    }

}
