package com.jee.learn.eureka.consumer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jee.learn.eureka.consumer.service.HelloService;

@RestController
public class HelloController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HelloService helloService;

    @GetMapping(value = "hello")
    public String hello() {
        String s = helloService.hello();
        logger.info("请求得到的结果为 {}", s);
        return s;
    }

}
