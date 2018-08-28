package com.jee.learn.test.kafka;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.kafka.LearnKafkaApplication;
import com.jee.learn.kafka.support.KafkaProducer;
import com.jee.learn.kafka.util.Constants;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnKafkaApplication.class)
public class KafkaMessageTest {

    private Logger logger = LoggerFactory.getLogger(KafkaMessageTest.class);

    @Autowired
    private KafkaProducer producer;

    @After
    public void sleepToReceiveMsg() {
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            logger.debug("", e);
        }
        logger.debug("finish");
    }

    @Test
    public void hello() {
        logger.debug("hello");
    }

    @Test
    public void msgSentTest() {
        producer.send(Constants.KAFKA_TOPIC_TEST, "hello");
    }

}
