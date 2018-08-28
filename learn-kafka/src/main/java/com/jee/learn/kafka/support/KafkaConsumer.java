package com.jee.learn.kafka.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.jee.learn.kafka.util.Constants;

/**
 * kafka自测
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年8月13日 下午2:06:58 1002360 新建
 */
@Component
public class KafkaConsumer {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    @KafkaListener(topics = { Constants.KAFKA_TOPIC_TEST })
    public void consumerMessage(String message) {
        logger.debug("OMG, I receive a [{}] message: {}", Constants.KAFKA_TOPIC_TEST, message);
    }

}
