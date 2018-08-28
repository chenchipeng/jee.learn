package com.jee.learn.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jee.learn.kafka.support.KafkaProducer;
import com.jee.learn.kafka.util.Constants;

@RestController
public class HelloController {

    @Autowired
    private KafkaProducer producer;

    @GetMapping("hello")
    public String hello() {
        producer.send(Constants.KAFKA_TOPIC_TEST, "hello");
        return "OK";
    }

}
