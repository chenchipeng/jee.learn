package com.jee.learn.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 程序入口
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年3月30日 下午3:18:21 1002360 新建
 */
@SpringBootApplication
@ComponentScan
public class LearnCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(LearnCommonApplication.class, args);
    }

}
