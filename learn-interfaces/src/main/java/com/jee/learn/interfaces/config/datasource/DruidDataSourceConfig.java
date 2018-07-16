package com.jee.learn.interfaces.config.datasource;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * druid 数据源注入<br/>
 * spring boot jpa(Hibernate)读写分离配置, 参考:https://www.jb51.net/article/111588.htm
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月26日 下午3:09:24 1002360 新建
 */
@Configuration
public class DruidDataSourceConfig {

    /**
     * DataSource 配置
     * 
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.druid.write")
    @Bean(name = "writeDruidDataSource")
    @Primary
    public DataSource writeDruidDataSource() {
        return new DruidDataSource();
    }

    /**
     * DataSource 配置
     * 
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.druid.read")
    @Bean(name = "readDruidDataSource")
    public DataSource readDruidDataSource() {
        return new DruidDataSource();
    }

}
