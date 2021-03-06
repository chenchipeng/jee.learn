package com.jee.learn.interfaces;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableCaching
public class LearnInterfaceApplication {

    private static Logger logger = LoggerFactory.getLogger(LearnInterfaceApplication.class);

    /** 启动入口 */
    public static void main(String[] args) {
        SpringApplication.run(LearnInterfaceApplication.class, args);
    }

    /** bean打印 */
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            String[] beanNames = ctx.getBeanDefinitionNames();
            if (logger.isDebugEnabled()) {

                logger.debug("Let's inspect the beans provided by Spring Boot:");

                Arrays.sort(beanNames);
                for (String beanName : beanNames) {
                    logger.debug("{}{}", beanName, extendBeanName(ctx, beanName));
                }

            }
            logger.info("The total of bean is: {}", beanNames.length);
        };
    }

    /** 补充打印Bean所在路径 */
    private String extendBeanName(ApplicationContext ctx, String beanName) {
        if (!beanName.contains(".")) {
            Object obj = ctx.getBean(beanName);
            return " : " + obj.getClass().getName();
        }
        return "";
    }

}
