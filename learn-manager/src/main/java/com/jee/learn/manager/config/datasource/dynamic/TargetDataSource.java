package com.jee.learn.manager.config.datasource.dynamic;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.persistence.EntityManagerFactory;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.manager.config.datasource.DsConstants;

/**
 * 
 * 指定操作数据源
 * <p>
 * 在没有实现jpa repository相关接口的情况下，可以用在抽象方法上；如果有实现类，务必加在实现类的具体方法上，以便更加精确的进行aop切入。
 * 用在service层时，被 {@link Transactional} 注解的方法不再受dao层切换控制，即优先拦截被
 * {@link Transactional} 注解的方法。<br/>
 * 注意：由于每个数据源都使用了同一个
 * {@link EntityManagerFactory}，在进行读写混合的操作时，强烈建议增加{@link Transactional}
 * 注解以避免数据源切换后数据不统一所引起的异常，并且，在一个事务中只能选定一个数据源。
 * </p>
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月27日 上午9:27:35 1002360 新建
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    @AliasFor("dsType")
    String value() default DsConstants.SLAVE_DATASOURCE;

    @AliasFor("value")
    String dsType() default DsConstants.SLAVE_DATASOURCE;

}
