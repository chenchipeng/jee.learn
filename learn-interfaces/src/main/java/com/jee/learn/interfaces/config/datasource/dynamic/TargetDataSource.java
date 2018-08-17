package com.jee.learn.interfaces.config.datasource.dynamic;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.jee.learn.interfaces.config.datasource.DsConstants;

/**
 * 目标数据源<br/>
 * 可用在jpa repository dao层和service层<br/>
 * 注意: 在该应用中因为使用了 dao层的切面切换数据源, 所以 @Transactional 注解不能加在类上, 只能用于方法;
 * 有 @Trasactional 注解的方法无法切换数据源
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
