package com.jee.learn.interfaces.support.web.base;

import java.io.IOException;
import java.util.concurrent.CompletionException;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.jee.learn.interfaces.support.cache.redis.RedisService;
import com.jee.learn.interfaces.support.web.WebConstants;
import com.jee.learn.interfaces.support.web.dto.HDto;
import com.jee.learn.interfaces.util.net.IPUtil;
import com.jee.learn.interfaces.util.validate.ValidateUtil;

/**
 * 公共基础controller
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月1日 上午11:15:11 ccp 新建
 */
@RestController
public class AbstractBaseController {

    protected static final String REDIRECT = "redirect:";
    protected static final String HTML_SUFFIX = ".html";
    protected static final String URL_SEPARATOR = "/";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected RedisService redisService;
    @Autowired
    protected ValidateUtil validateUtil;

    /**
     * 请求参数格式不正确异常处理
     * 
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler({ HttpMessageNotReadableException.class })
    public HDto httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {
        logger.warn("", ex);
        HDto ri = new HDto();
        ri.setC(WebConstants.BUSINESS_ERROR_CODE);
        ri.setE(WebConstants.BUSINESS_ERROR_MESSAGE);
        return ri;
    }

    /**
     * 请求参数格式不正确异常处理
     * 
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler({ MismatchedInputException.class })
    public HDto mismatchedInputExceptionHandler(MismatchedInputException ex) {
        logger.warn("", ex);
        HDto ri = new HDto();
        ri.setC(WebConstants.BUSINESS_ERROR_CODE);
        ri.setE(WebConstants.BUSINESS_ERROR_MESSAGE);
        return ri;
    }

    /**
     * 客户端中止异常处理
     * 
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler({ ClientAbortException.class })
    public HDto clientAbortExceptionHandler(ClientAbortException ex) {
        logger.warn("", ex);
        HDto ri = new HDto();
        ri.setC(WebConstants.BUSINESS_ERROR_CODE);
        ri.setE(WebConstants.BUSINESS_ERROR_MESSAGE);
        return ri;
    }

    /**
     * 通用异常处理
     * 
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler({ Exception.class })
    public HDto exceptionHandler(Exception ex) {
        logger.warn("", ex);
        HDto ri = new HDto();
        ri.setC(WebConstants.BUSINESS_ERROR_CODE);
        ri.setE(WebConstants.BUSINESS_ERROR_MESSAGE);
        return ri;
    }

    /**
     * 异步接口调用异常
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler({ CompletionException.class })
    public HDto completionExceptionHandler(CompletionException ex) {
        HDto ri = new HDto();
        ri.setC(WebConstants.BUSINESS_ERROR_CODE);
        ri.setE(WebConstants.BUSINESS_ERROR_MESSAGE);

        Throwable throwable = ex.getCause();
        if (throwable instanceof RestException) {
            RestException aex = (RestException) throwable;
            ri.setC(aex.getCode());
            ri.setE(aex.getMsg());
        } else if (throwable instanceof IOException) {
            ri.setC(WebConstants.BUSINESS_ERROR_CODE);
            ri.setE(WebConstants.BUSINESS_ERROR_MESSAGE);
        }

        logger.warn("{}", ex.getMessage(), ex);
        return ri;
    }

    /** 获取客户端请求IP */
    public String getIp(HttpServletRequest request) {
        return IPUtil.getIp(request);
    }
}
