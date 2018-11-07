package com.jee.learn.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.jee.learn.manager.security.LogUtil;
import com.jee.learn.manager.util.base.ExceptionUtil;
import com.jee.learn.manager.util.time.ClockUtil;
import com.jee.learn.manager.util.time.DateFormatUtil;

/**
 * 异常处理<br/>
 * 参考: https://www.jianshu.com/p/aa507b3552fa
 * 
 * @author admin
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    protected static final String DEFAULT_ERROR_VIEW = "error/error";

    @Autowired
    private LogUtil logUtil;

    @ExceptionHandler({ Exception.class })
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception ex) throws Exception {
        // 持久化错误日志
        logUtil.saveLog(request, null, ex, "错误日志");
        // 页面输出
        String error = ex.getClass().getSimpleName();
        if (StringUtils.isNotBlank(ex.getMessage())) {
            error += ": " + ex.getMessage();
        }
        String message = ExceptionUtil.stackTraceText(ex);

        ModelAndView mav = new ModelAndView();
        mav.addObject("url", request.getRequestURL());
        mav.addObject("error", error);
        mav.addObject("date", DateFormatUtil.formatDateDefault(ClockUtil.currentDate()));
        mav.addObject("message", message);
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

}
