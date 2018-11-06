package com.jee.learn.manager.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.jee.learn.manager.util.base.ExceptionUtil;

@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    protected static final String DEFAULT_ERROR_VIEW = "error/error";

    // https://www.jianshu.com/p/aa507b3552fa

    @ExceptionHandler({ Exception.class })
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        String error = e.getClass().getSimpleName();
        if (StringUtils.isNotBlank(e.getMessage())) {
            error += ": " + e.getMessage();
        }
        String message = ExceptionUtil.stackTraceText(e);

        ModelAndView mav = new ModelAndView();
        mav.addObject("url", req.getRequestURL());
        mav.addObject("error", error);
        mav.addObject("message", message);
        mav.setViewName(DEFAULT_ERROR_VIEW);
        return mav;
    }

}
