package com.jee.learn.test.thymeleaf;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;

import com.jee.learn.thymeleaf.LearnThymeleafApplication;
import com.jee.learn.thymeleaf.util.ThymeleafUtil;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnThymeleafApplication.class)
public class ThymeleafUtilTest {

    private static final Logger log = LoggerFactory.getLogger(ThymeleafUtilTest.class);

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private ThymeleafUtil thymeleafUtil;

    @Test
    public void nullTest() {
        log.info("{}", templateEngine == null);
    }

    @Test
    public void writeToFileTest() {
        try {
            thymeleafUtil.writeToFile("example", thymeleafUtil.buildContext(), "result.html");
        } catch (IOException e) {
            log.info("", e);
        }
    }

}
