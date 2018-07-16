package com.jee.learn;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.jee.learn.common.CommonScanTag;
import com.jee.learn.component.ComponentScanTag;
import com.jee.learn.config.ManConfigScanTag;
import com.jee.learn.controller.ManControllerScanTag;
import com.jee.learn.model.ManModelScanTag;
import com.jee.learn.persist.ManRepositoryScanTag;
import com.jee.learn.service.ManServiceScanTag;

/**
 * 程序入口
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年3月30日 下午3:18:21 1002360 新建
 */
@SpringBootApplication
@EntityScan(basePackageClasses = { ManModelScanTag.class })
@EnableJpaRepositories(basePackageClasses = { ManRepositoryScanTag.class })
@ComponentScan(basePackageClasses = { ManServiceScanTag.class, ManControllerScanTag.class, CommonScanTag.class,
        ComponentScanTag.class, ManConfigScanTag.class })
@ServletComponentScan(basePackageClasses = { ComponentScanTag.class })
@EnableTransactionManagement(proxyTargetClass = true)
public class LearnManagerApplication {

    private static Logger logger = LoggerFactory.getLogger(LearnManagerApplication.class);

    /** 启动 */
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(LearnManagerApplication.class);
        application.run(args);
        logger.debug("注意: shiro 与 devtools 有很大的兼容问题,常导致 principal 或 session 类型转换出错");
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
