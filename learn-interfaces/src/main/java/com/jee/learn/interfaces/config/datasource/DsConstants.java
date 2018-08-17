package com.jee.learn.interfaces.config.datasource;

/**
 * 数据源常量池
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年8月17日 上午9:09:32 1002360 新建
 */
public interface DsConstants {

    /* 每新增一个数据源, 需要手动定义一个常量作为Bean的名称以及切换时的KEY */
    String MASTER_DATASOURCE = "masterDatasource";
    String SLAVE_DATASOURCE = "slaveDatasource";

}
