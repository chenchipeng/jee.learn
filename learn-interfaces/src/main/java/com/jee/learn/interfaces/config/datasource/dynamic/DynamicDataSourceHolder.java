package com.jee.learn.interfaces.config.datasource.dynamic;

/**
 * 数据源动态路由切换工具
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月27日 上午9:37:30 1002360 新建
 */
public class DynamicDataSourceHolder {

    // 使用ThreadLocal把数据源与当前线程绑定
    private static final ThreadLocal<String> DATA_SOURCE = new ThreadLocal<String>();

    public static void setDataSource(String dataSourceName) {
        DATA_SOURCE.set(dataSourceName);
    }

    public static String getDataSource() {
        return (String) DATA_SOURCE.get();
    }

    public static void clearDataSource() {
        DATA_SOURCE.remove();
    }
}
