package com.jee.learn.interfaces.config.datasource;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import com.alibaba.druid.pool.DruidDataSource;
import com.jee.learn.interfaces.config.SystemConfig;
import com.jee.learn.interfaces.config.datasource.dynamic.DynamicDataSource;

/**
 * 数据源配置<br/>
 * 所创建的DataSource Bean需要强制指定名称, 名称值需要在{@link DsConstants}定义
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年8月17日 上午9:07:39 1002360 新建
 *          2.2019年3月2日 下午11:41:22 ccp 为动态数据源指定 entityManagerFactory
 */
@Configuration
public class DataSourceConfig {

	@Autowired
	private JpaProperties jpaProperties;

	@Autowired
	private DynamicDataSource dynamicDataSource;
	
	@Autowired
	private SystemConfig systemConfig;

	//////// 为动态数据源指定 entityManagerFactory ////////

	/**
	 * 此bean在这里不一定需要注入. 不注入时在启动类上可能有个小警告, 但不影响运行. 洁癖党请自觉开启 :)
	 * 
	 * @return
	 */
	@Bean(name = "entityManagerFactory")
	@Primary
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(systemConfig.getEntityBasePackage());
		factory.setDataSource(dynamicDataSource);// 数据源

		factory.setJpaPropertyMap(jpaProperties.getProperties());
		factory.afterPropertiesSet();// 在完成了其它所有相关的配置加载以及属性设置后,才初始化
		return factory.getObject();
	}

	//////// 数据源配置 ////////

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
