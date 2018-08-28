package com.jee.learn.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableKafka
@EnableScheduling
public class LearnKafkaApplication {

    /** 启动入口 */
    public static void main(String[] args) {
        SpringApplication.run(LearnKafkaApplication.class, args);
    }

}
