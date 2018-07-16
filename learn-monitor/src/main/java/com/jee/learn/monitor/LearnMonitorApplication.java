package com.jee.learn.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@SpringBootApplication
@EnableAdminServer
public class LearnMonitorApplication {

    /** 启动入口 */
    public static void main(String[] args) {
        SpringApplication.run(LearnMonitorApplication.class, args);
    }

}
