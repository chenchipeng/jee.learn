package com.jee.learn.mybatis.support;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

/**
 * 标记实体的主键属性
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月22日 上午11:26:15 ccp 新建
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PrimaryKey {

    /**
     * Alias for {@link #increase}
     * 
     * @return
     */
    @AliasFor("increase")
    boolean value() default false;

    /**
     * 指定主键是否自增, 默认否
     * 
     * @return
     */
    @AliasFor("value")
    boolean increase() default false;

}
