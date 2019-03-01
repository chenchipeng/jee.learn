package com.jee.learn.interfaces.util.net;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Servlet 工具, 框架选型为spring boot + thymeleaf
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月13日 下午3:51:52 ccp 新建
 */
public class ServletUtil {

    /** 静态文件列表 */
    private static final String[] STATIC_FILES_SUFFIX = { ".css", ".js", ".png", ".gif", ".jpeg", ".jpg", ".bmp",
            ".ico", ".swf", ".psd", ".htc", ".crx", ".xpi", ".exe", ".ipa", ".apk", ".map", ".hml" };
    private static final String[] STATIC_FILES_PREFIX = { "/hml, /img, /js, /css, /plugins" };

    /** 根目录 */
    private static final String ROOT = "/";

    /** API请求标识 */
    private static final String[] REST_PREFIX = { "/rest, /test" };

    /**
     * 获取当前请求对象
     * 
     * @return
     */
    public static HttpServletRequest getRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前应答对象
     * 
     * @return
     */
    public static HttpServletResponse getResponse() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断访问URI是否是静态文件请求<br/>
     * 静态文件列表 {@link #STATIC_FILES}
     * 
     * @throws Exception
     */
    public static boolean isStaticFile(String uri) {
        if (StringUtils.endsWithAny(uri, STATIC_FILES_SUFFIX)) {
            return true;
        }
        return false;
    }

    /**
     * 判断访问URI是否是静态文件请求<br/>
     * 静态文件路径
     * 
     * @param uri
     * @return
     */
    public static boolean isViewFile(String uri) {
        if (StringUtils.startsWithAny(uri, STATIC_FILES_PREFIX)) {
            return true;
        }
        return false;
    }

    /**
     * 判断访问URI是否是根目录请求
     * 
     * @param uri
     * @return
     */
    public static boolean isRoot(String uri) {
        return ROOT.equals(uri);
    }

    /**
     * 判断访问URI是否是REST请求, 结果返回json那种
     * 
     * @param uri
     * @return
     */
    public static boolean isRest(String uri) {
        if (StringUtils.startsWithAny(uri, REST_PREFIX)) {
            return true;
        }
        return false;
    }

}
