package com.jee.learn.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class LearnGateWayApplication {

    /** 启动入口 */
    public static void main(String[] args) {
        SpringApplication.run(LearnGateWayApplication.class, args);
    }

}
