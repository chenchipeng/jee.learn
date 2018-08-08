package com.jee.learn.interfaces.config.datasource.dynamic;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jee.learn.interfaces.config.datasource.DataSourceConfig;

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
@Documented
public @interface DataSourceTarget {

    /**
     * 指定使用的数据库,默认DataSourceConfig.READ_DATASOURCE_KEY<br/>
     * 具体的值请参考:<br/>
     * {@link com.jee.learn.interfaces.config.datasource.DataSourceConfig}
     * 
     * @return
     */
    String dataSource() default DataSourceConfig.READ_DATASOURCE_KEY;// 数据源

}
