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
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.pool.DruidDataSource;
import com.jee.learn.interfaces.config.datasource.dynamic.DynamicDataSource;

@Configuration
public class DataSourceConfig {

    private final static String DYNAMIC_DATASOURCE = "dataSource";// 默认库
    private final static String MASTER_DATASOURCE = "masterDataSource";// 主库-可读可写
    private final static String SLAVE_DATASOURCE = "slaveDataSource";// 从库-只读

    /**
     * 动态数据源配置<br/>
     * 自定义注入AbstractRoutingDataSource以便动态切换数据源<br/>
     * 注意: 默认数据源会被自动初始化连接, 其余的将在使用时进行初始化, 如需要在系统启动时进行初始化则需要手工指定
     * 
     * @param masterDS
     * @param slaveDS
     * @return
     * @throws Exception
     */
    @Bean(name = DYNAMIC_DATASOURCE)
    public DynamicDataSource dynamicDataSource(@Qualifier(MASTER_DATASOURCE) DataSource masterDS,
            @Qualifier(SLAVE_DATASOURCE) DataSource slaveDS) throws Exception {

        // 准备数据库资源
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DsTypeEnum.MASTER.value(), masterDS);
        targetDataSources.put(DsTypeEnum.SLAVE.value(), slaveDS);

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
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.druid.write")
    @Bean(name = MASTER_DATASOURCE)
    public DataSource masterDS() {
        return new DruidDataSource();
    }

    /**
     * DataSource 从库配置
     * 
     * @return
     */
    @ConfigurationProperties(prefix = "spring.datasource.druid.read")
    @Bean(name = SLAVE_DATASOURCE)
    public DataSource slaveDS() {
        return new DruidDataSource();
    }

    /**
     * 配置默认 entity manager factory
     * 
     * @return
     */
//    @Bean(name = "entityManagerFactory")
//    @Primary
    public EntityManagerFactory entityManagerFactory(@Qualifier(DYNAMIC_DATASOURCE) DynamicDataSource dataSource,
            JpaProperties jpaProperties) {
        LocalContainerEntityManagerFactoryBean factory = initEntityManagerFactory(dataSource, jpaProperties);
        return factory.getObject();
    }

    /**
     * 配置默认事物管理器
     * 
     * @return
     */
//    @Bean(name = "transactionManager")
//    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
        return jpaTransactionManager;
    }

    @Deprecated
    public EntityManagerFactory slaveEntityManagerFactory(@Qualifier(SLAVE_DATASOURCE) DataSource dataSource,
            JpaProperties jpaProperties) {
        LocalContainerEntityManagerFactoryBean factory = initEntityManagerFactory(dataSource, jpaProperties);
        return factory.getObject();
    }

    /** 构造LocalContainerEntityManagerFactoryBean */
    private LocalContainerEntityManagerFactoryBean initEntityManagerFactory(DataSource dataSource,
            JpaProperties jpaProperties) {
        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factory.setJpaVendorAdapter(vendorAdapter);

        factory.setPackagesToScan("com.jee.learn.interfaces.domain");
        factory.setJpaPropertyMap(jpaProperties.getProperties()); // 数据源
        factory.setDataSource(dataSource); // 在完成了其它所有相关的配置加载以及属性设置后,才初始化
        factory.afterPropertiesSet();
        return factory;
    }

}
