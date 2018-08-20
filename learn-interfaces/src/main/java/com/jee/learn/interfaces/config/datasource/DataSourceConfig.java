package com.jee.learn.interfaces.config.datasource;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.jee.learn.interfaces.config.datasource.dynamic.DynamicDataSource;

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
@EnableJpaRepositories(value = "com.jee.learn.interfaces.repository", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
public class DataSourceConfig {

    /**
     * DataSource 主库配置
     * 
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.druid.write")
    @Bean(name = DsConstants.MASTER_DATASOURCE)
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
    @Bean(name = DsConstants.SLAVE_DATASOURCE)
    public DataSource slaveDS() {
        return new DruidDataSource();
    }

    @Bean
    public DynamicDataSource dynamicDataSource(@Qualifier(DsConstants.MASTER_DATASOURCE) DataSource masterDS,
            @Qualifier(DsConstants.SLAVE_DATASOURCE) DataSource slaveDS) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        targetDataSources.put(DsConstants.MASTER_DATASOURCE, masterDS);
        targetDataSources.put(DsConstants.SLAVE_DATASOURCE, slaveDS);
        dynamicDataSource.setTargetDataSources(targetDataSources);
        dynamicDataSource.setDefaultTargetDataSource(masterDS);
        // 设置切换所需要的key, 用于初始化连接
        dynamicDataSource.setDsKeys(targetDataSources.keySet().toArray());
        return dynamicDataSource;
    }

    @Bean
    public EntityManagerFactory entityManagerFactory(JpaProperties jpaProperties, DynamicDataSource dynamicDataSource) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.jee.learn.interfaces.domain");
        factory.setDataSource(dynamicDataSource);
        factory.setJpaPropertyMap(jpaProperties.getProperties());
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

}
