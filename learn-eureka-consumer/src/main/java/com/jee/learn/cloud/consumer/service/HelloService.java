package com.jee.learn.cloud.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("LEARN-CLOUD-PROVIDER")
public interface HelloService {

    @GetMapping(value = "hello")
    String hello();

}
