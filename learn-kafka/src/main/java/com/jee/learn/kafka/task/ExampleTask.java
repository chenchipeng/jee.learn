package com.jee.learn.kafka.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jee.learn.kafka.support.KafkaProducer;
import com.jee.learn.kafka.util.Constants;

@Component
public class ExampleTask {

    private Logger logger = LoggerFactory.getLogger(ExampleTask.class);

    @Autowired
    private KafkaProducer producer;

    @Scheduled(cron = "0 0/10 * * * ?")
    private void process() {
        logger.debug("this is example scheduler task runing");
    }

    @Scheduled(cron = "0/20 * * * * ?")
    private void sentMsgProcess() {
        String str = "the sent message process running in " + System.currentTimeMillis();
        producer.send(Constants.KAFKA_TOPIC_TEST, str);
    }

}
