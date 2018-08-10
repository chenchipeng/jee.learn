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

@Configuration
@EnableJpaRepositories(value = "com.jee.learn.interfaces.repository", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
public class DataSourceConfig {

    /**
     * 动态数据源配置<br/>
     * 自定义注入AbstractRoutingDataSource以便动态切换数据源
     * 
     * @param masterDS
     * @param slaveDS
     * @return
     * @throws Exception
     */
    @Bean(name = DsConstants.DEFAULT)
    public DynamicDataSource dynamicDataSource(@Qualifier(DsConstants.MASTER) DataSource masterDS,
            @Qualifier(DsConstants.SLAVE) DataSource slaveDS) throws Exception {

        // 准备数据库资源
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DsConstants.MASTER, masterDS);
        targetDataSources.put(DsConstants.SLAVE, slaveDS);

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
    @Bean(name = DsConstants.MASTER)
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
    @Bean(name = DsConstants.SLAVE)
    public DataSource slaveDS() {
        return new DruidDataSource();
    }

    /**
     * Jpa Entity Manager 配置
     * 
     * @return
     */
    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(@Qualifier(DsConstants.DEFAULT) DynamicDataSource dataSource,
            JpaProperties jpaProperties) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);

        factory.setPackagesToScan("com.jee.learn.interfaces.domain");
        factory.setJpaPropertyMap(jpaProperties.getProperties());
        // 数据源
        factory.setDataSource(dataSource);
        // 在完成了其它所有相关的配置加载以及属性设置后,才初始化
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    /**
     * 配置事物管理器
     * 
     * @return
     */
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

}
