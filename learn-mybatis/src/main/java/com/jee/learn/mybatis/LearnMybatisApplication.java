package com.jee.learn.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jee.learn.mybatis.repository.Mapper;

@SpringBootApplication
@MapperScan(basePackageClasses = Mapper.class)
public class LearnMybatisApplication {

    /** 启动入口 */
    public static void main(String[] args) {
        SpringApplication.run(LearnMybatisApplication.class, args);
    }

}
