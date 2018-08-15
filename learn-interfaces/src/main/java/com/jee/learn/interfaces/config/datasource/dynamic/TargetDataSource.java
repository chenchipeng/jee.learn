package com.jee.learn.interfaces.config.datasource.dynamic;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jee.learn.interfaces.config.datasource.DsTypeEnum;

/**
 * 目标数据源
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月27日 上午9:27:35 1002360 新建
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface TargetDataSource {

    DsTypeEnum dsType() default DsTypeEnum.SLAVE;

}
