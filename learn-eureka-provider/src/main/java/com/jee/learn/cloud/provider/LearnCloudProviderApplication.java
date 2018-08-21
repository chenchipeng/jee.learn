package com.jee.learn.cloud.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class LearnCloudProviderApplication {

    /** 启动入口 */
    public static void main(String[] args) {
        SpringApplication.run(LearnCloudProviderApplication.class, args);
    }

}
