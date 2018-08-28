package com.jee.learn.kafka.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * kafka 消息发送工具类
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年8月13日 上午11:49:02 1002360 新建
 */
@Component
public class KafkaProducer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String message) {
        logger.debug("send topic:{} message:{}", topic, message);
        kafkaTemplate.send(topic, message);
    }

}
