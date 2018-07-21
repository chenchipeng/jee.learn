package com.jee.learn.eureka.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class LearnEurekaConsumerApplication {

    /** 纯属测试学习*/
    @Bean // 定义REST客户端，RestTemplate实例
    @LoadBalanced // 开启负载均衡的能力
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /** 启动入口 */
    public static void main(String[] args) {
        SpringApplication.run(LearnEurekaConsumerApplication.class, args);
    }

}
