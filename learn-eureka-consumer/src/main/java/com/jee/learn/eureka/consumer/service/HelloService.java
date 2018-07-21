package com.jee.learn.eureka.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("LEARN-EUREKA-PROVIDER")
public interface HelloService {

    @GetMapping(value = "hello")
    String hello();

}
