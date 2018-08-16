package com.jee.learn.interfaces.config.datasource;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DataSourceConfig {

    /**
     * DataSource 主库配置
     * 
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.druid.write")
    @Bean(name = DsConstants.MASTER_DATASOURCE)

    public DataSource masterDS() {
        return new DruidDataSource();
    }

    /**
     * DataSource 从库配置
     * 
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.druid.read")
    @Bean(name = DsConstants.SLAVE_DATASOURCE)
    public DataSource slaveDS() {
        return new DruidDataSource();
    }

}
