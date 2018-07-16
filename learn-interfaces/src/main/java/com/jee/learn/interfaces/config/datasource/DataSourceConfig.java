package com.jee.learn.interfaces.config.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.jee.learn.interfaces.config.datasource.dynamic.DynamicDataSource;

/**
 * 自定义注入AbstractRoutingDataSource以便动态切换数据源
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月27日 上午9:27:35 1002360 新建
 */
@Configuration
public class DataSourceConfig {
    
    public final static String WRITE_DATASOURCE_KEY = "writeDruidDataSource";
    public final static String READ_DATASOURCE_KEY = "readDruidDataSource";

    /**
     * 注入AbstractRoutingDataSource
     * 
     * @param readDruidDataSource
     * @param writeDruidDataSource
     * @return
     * @throws Exception
     */
    @Bean
    public AbstractRoutingDataSource routingDataSource(@Qualifier(READ_DATASOURCE_KEY) DataSource readDruidDataSource,
            @Qualifier(WRITE_DATASOURCE_KEY) DataSource writeDruidDataSource) throws Exception {

        // 准备数据库资源
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(WRITE_DATASOURCE_KEY, writeDruidDataSource);
        targetDataSources.put(READ_DATASOURCE_KEY, readDruidDataSource);

        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setDefaultTargetDataSource(writeDruidDataSource);
        dataSource.setTargetDataSources(targetDataSources);
        return dataSource;
    }
}