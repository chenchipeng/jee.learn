package com.jee.learn.manager.support.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jee.learn.manager.config.WebConfig;
import com.jee.learn.manager.security.LogUtil;
import com.jee.learn.manager.util.time.DateFormatUtil;

/**
 * 日志拦截<br/>
 * 器 参考:https://www.cnblogs.com/lfjjava/p/6093388.html
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年11月6日 上午9:48:34 ccp 新建
 */
@Component
public class LogInterceptor implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(WebConfig.class);
    private static final ThreadLocal<Long> LOG_TREADLOCAL = new NamedThreadLocal<Long>("Log ThreadLocal");

    @Autowired
    private LogUtil logUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (LOG.isDebugEnabled()) {
            long beginTime = System.currentTimeMillis();// 1、开始时间
            LOG_TREADLOCAL.set(beginTime); // 线程绑定变量（该数据只有当前请求的线程可见）
            LOG.debug("开始计时: {}  URI: {}", DateFormatUtil.formatDateOnTimeMesc(beginTime), request.getRequestURI());
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

        if (LOG.isDebugEnabled() && modelAndView != null) {
            LOG.debug("ViewName: " + modelAndView.getViewName());
        }
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        // 记录请求日志
        logUtil.saveLog(request, handler, ex, StringUtils.EMPTY);

        // 打印JVM信息。
        if (LOG.isDebugEnabled()) {
            long beginTime = LOG_TREADLOCAL.get();// 得到线程绑定的局部变量（开始时间）
            long endTime = System.currentTimeMillis(); // 2、结束时间

            String consume = DateFormatUtil.formatDuration(endTime - beginTime);
            long maxMemory = Runtime.getRuntime().maxMemory() / 1232896L;
            long totalMemory = Runtime.getRuntime().totalMemory() / 1232896L;
            long freeMemory = Runtime.getRuntime().freeMemory() / 1232896L;
            long availableMemory = maxMemory - totalMemory + freeMemory;

            LOG.debug("计时结束：{}  耗时：{}  URI: {}  最大内存: {}m  已分配内存: {}m  已分配内存中的剩余空间: {}m  最大可用内存: {}m",
                    DateFormatUtil.formatDateOnTimeMesc(endTime), consume, request.getRequestURI(), maxMemory,
                    totalMemory, freeMemory, availableMemory);
        }
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
