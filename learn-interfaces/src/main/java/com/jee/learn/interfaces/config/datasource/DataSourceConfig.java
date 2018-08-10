package com.jee.learn.interfaces.config.datasource;

import javax.persistence.EntityManagerFactory;

import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.jee.learn.interfaces.config.datasource.dynamic.DynamicDataSource;

@Configuration
@EnableJpaRepositories(value = "com.jee.learn.interfaces.repository", entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
public class DataSourceConfig {

    /**
     * Jpa Entity Manager 配置
     * 
     * @return
     */
    @Bean(name = "entityManagerFactory")
    public EntityManagerFactory entityManagerFactory(DynamicDataSource dataSource, JpaProperties jpaProperties) {
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
