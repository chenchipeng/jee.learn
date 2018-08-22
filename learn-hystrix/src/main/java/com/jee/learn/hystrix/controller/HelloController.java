package com.jee.learn.hystrix.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping(value = "/")
    public String index() {
        String str = "Hello world";
        logger.info("接受到请求,返回信息={}", str);
        return str;
    }
}
