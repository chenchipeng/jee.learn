package com.jee.learn.interfaces.controller;

import java.util.concurrent.CompletionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.jee.learn.interfaces.dto.ResponseDto;
import com.jee.learn.interfaces.util.WebConstants;
import com.jee.learn.interfaces.util.exception.IntfcException;

@RestController
public class BaseController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 自定义统一异常处理
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({ IntfcException.class })
    public ResponseDto<Object> intfcExceptionHandler(IntfcException e) {

        ResponseDto<Object> rd = new ResponseDto<Object>();
        rd.setC(e.getCode());
        rd.setE(e.getMsg());

        logger.info("自定义异常捕获", e);
        return rd;
    }

    /**
     * 异步接口调用异常捕获
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({ CompletionException.class })
    public ResponseDto<Object> completionExceptionHandler(CompletionException e) {

        ResponseDto<Object> rd = new ResponseDto<Object>();
        rd.setC(WebConstants.BUSINESS_ERROR_CODE);
        rd.setE(WebConstants.BUSINESS_ERROR_MESSAGE);

        Throwable throwable = e.getCause();
        if (throwable instanceof IntfcException) {
            IntfcException intfc = (IntfcException) throwable;
            rd.setC(intfc.getCode());
            rd.setE(intfc.getMsg());
        }
        logger.info("异步接口调用异常捕获", e);
        return rd;
    }

    /**
     * 通用异常捕获
     * 
     * @param e
     * @return
     */
    @ExceptionHandler({ Exception.class })
    public ResponseDto<Object> exceptionHandler(Exception e) {

        ResponseDto<Object> rd = new ResponseDto<Object>();
        rd.setC(WebConstants.BUSINESS_ERROR_CODE);
        rd.setE(WebConstants.BUSINESS_ERROR_MESSAGE);

        logger.info("通用异常捕获", e);
        return rd;
    }

}
