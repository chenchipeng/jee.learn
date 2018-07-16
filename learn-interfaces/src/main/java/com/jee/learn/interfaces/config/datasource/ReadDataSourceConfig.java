package com.jee.learn.interfaces.config.datasource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * read 库配置
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月27日 上午9:20:24 1002360 新建
 */
@Configuration
@EnableJpaRepositories(value = "com.jee.learn.interfaces.repository", entityManagerFactoryRef = "readEntityManagerFactory", transactionManagerRef = "readTransactionManager")
public class ReadDataSourceConfig {

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    @Qualifier("readDruidDataSource")
    private DataSource readDruidDataSource;

    /**
     * 我们通过LocalContainerEntityManagerFactoryBean来获取EntityManagerFactory实例
     * 
     * @return
     */
    @Bean(name = "readEntityManagerFactoryBean")
    public LocalContainerEntityManagerFactoryBean readEntityManagerFactoryBean(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(readDruidDataSource).properties(jpaProperties.getProperties())
                .packages("com.jee.learn.interfaces.domain") // 设置实体类所在位置
                .persistenceUnit("readPersistenceUnit").build();

        // .getObject();//不要在这里直接获取EntityManagerFactory
        // getObject()方法可以获取到EntityManagerFactory的实例,看似跟第一种没有什么区别,但是我们不能直接用
        // getObject(),不然会获取不到,报空指针异常.
    }

    /**
     * EntityManagerFactory类似于Hibernate的SessionFactory,mybatis的SqlSessionFactory
     * 总之,在执行操作之前,我们总要获取一个EntityManager,这就类似于Hibernate的Session,mybatis的sqlSession.
     * 
     * @param builder
     * @return
     */
    // @Bean(name = "readEntityManagerFactory")
    public EntityManagerFactory readEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return this.readEntityManagerFactoryBean(builder).getObject();
    }

    /**
     * 配置事物管理器
     * 
     * @return
     */
    @Bean(name = "readTransactionManager")
    public PlatformTransactionManager readTransactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(readEntityManagerFactory(builder));
    }
}