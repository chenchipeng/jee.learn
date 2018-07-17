package com.jee.learn.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class LearnEurekaApplication {

    /** 启动入口 */
    public static void main(String[] args) {
        SpringApplication.run(LearnEurekaApplication.class, args);
    }

}
