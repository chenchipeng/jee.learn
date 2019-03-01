package com.jee.learn.interfaces.util.net;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class IPUtil {

    /**
     * 获取客户端IP地址
     * 
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return proxyIP(ip);
        }

        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }

        ip = request.getHeader("PROXY_FORWARDED_FOR");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return proxyIP(ip);
        }
        return request.getRemoteAddr();
    }

    /** 处理存在代理的情况 */
    private static String proxyIP(String ip) {
        // 多次反向代理后会有多个ip值，第一个ip才是真实ip
        int index = ip.indexOf(",");
        if (index != -1) {
            return ip.substring(0, index);
        } else {
            return ip;
        }
    }

}
