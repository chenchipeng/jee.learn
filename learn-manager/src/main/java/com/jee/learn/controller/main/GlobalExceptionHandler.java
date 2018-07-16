package com.jee.learn.controller.main;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * 统一异常处理<br/>
 * 参考:https://www.jianshu.com/p/aa507b3552fa
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年5月3日 下午2:55:19 1002360 新建
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${sys.name:sys}")
    private String title;

    /** 捕获shiro禁止一个账号同时登陆所发生的异常 */
    @ExceptionHandler(value = org.apache.shiro.authc.AuthenticationException.class)
    public ModelAndView shiroErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        ModelAndView mav = new ModelAndView();
        mav.addObject("title", title);
        mav.addObject("message", e.getMessage());
        mav.setViewName("main/login");
        return mav;
    }

    /** 默认处理放到最后 */
    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {

        logger.info("", e);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error/default_error");
        return mav;
    }

}
