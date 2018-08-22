package com.jee.learn.cloud.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.jee.learn.cloud.consumer.service.impl.HelloServiceImpl;

@FeignClient(name = "LEARN-CLOUD-PROVIDER", fallback = HelloServiceImpl.class)
public interface HelloService {

    @GetMapping(value = "hello")
    String hello();

}
