package com.jee.learn.cloud.consumer.service.impl;

import org.springframework.stereotype.Component;

import com.jee.learn.cloud.consumer.service.HelloService;

@Component
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello() {
        return "sorry, this messge send failed";
    }

}
