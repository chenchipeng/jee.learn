package com.jee.learn.manager.config.datasource;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 数据源配置<br/>
 * 所创建的DataSource Bean需要强制指定名称, 名称值需要在{@link DsConstants}定义
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年8月17日 上午9:07:39 1002360 新建
 */
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
