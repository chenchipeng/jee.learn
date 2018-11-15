package com.jee.learn.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.jee.learn.manager.security.LogUtil;
import com.jee.learn.manager.util.base.ExceptionUtil;
import com.jee.learn.manager.util.base.excrption.RestException;
import com.jee.learn.manager.util.time.ClockUtil;
import com.jee.learn.manager.util.time.DateFormatUtil;

/**
 * 异常处理<br/>
 * 只能处理 Controller 层未捕获（往外抛）的异常，对于 Interceptor（拦截器）层的异常，Spring 框架层的异常，就无能为力了<br/>
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

    /**
     * 通用异常捕捉, 返回自定义异常页面
     * 
     * @param request
     * @param ex
     * @return
     * @throws Exception
     */
    @ExceptionHandler({ Exception.class })
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception ex) throws Exception {
        // 持久化错误日志
        logger.info("", ex);
        logUtil.saveLog(request, null, ex, "错误日志");

        // 异常类型判断, 如果没有RestException则以html输出, 否则以json输出
        RestException rex = ExceptionUtil.findCause(ex, RestException.class);
        if (rex == null) {
            return asHtml(request, ex);
        }
        return asjson(request, rex);

    }

    /**
     * html 格式输出
     * 
     * @param request
     * @param ex
     * @return
     */
    private ModelAndView asHtml(HttpServletRequest request, Exception ex) {
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

    /**
     * json 输出
     * 
     * @param request
     * @param ex
     * @return
     */
    private ModelAndView asjson(HttpServletRequest request, RestException ex) {
        ModelAndView mav = new ModelAndView(new MappingJackson2JsonView());
        mav.addObject("e", ex.getMessage());
        mav.addObject("c", ex.getCode());
        return mav;
    }

}
