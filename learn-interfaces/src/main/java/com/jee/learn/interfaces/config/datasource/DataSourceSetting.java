package com.jee.learn.interfaces.config.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;
import com.jee.learn.interfaces.config.datasource.dynamic.DynamicDataSource;

/**
 * 数据源引入
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月27日 上午9:27:35 1002360 新建
 */
@Configuration
public class DataSourceSetting {

    /**
     * 自定义注入AbstractRoutingDataSource以便动态切换数据源
     * 
     * @param masterDS
     * @param slaveDS
     * @return
     * @throws Exception
     */
    @Bean(name=DS.DEFAULT)
    public DynamicDataSource dynamicDataSource(@Qualifier(DS.MASTER) DataSource masterDS,
            @Qualifier(DS.SLAVE) DataSource slaveDS) throws Exception {

        // 准备数据库资源
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DS.MASTER, masterDS);
        targetDataSources.put(DS.SLAVE, slaveDS);

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setDefaultTargetDataSource(masterDS);
        dataSource.setTargetDataSources(targetDataSources);
        return dataSource;
    }

    /**
     * DataSource 主库配置
     * 
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.druid.write")
    @Bean(name = DS.MASTER)
    @Primary
    public DataSource masterDS() {
        return new DruidDataSource();
    }

    /**
     * DataSource 从库配置
     * 
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.druid.read")
    @Bean(name = DS.SLAVE)
    public DataSource slaveDS() {
        return new DruidDataSource();
    }
}