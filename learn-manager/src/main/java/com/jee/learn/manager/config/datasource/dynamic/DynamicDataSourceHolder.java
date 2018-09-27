package com.jee.learn.manager.config.datasource.dynamic;

import com.jee.learn.manager.config.datasource.DsConstants;

/**
 * 数据源动态路由切换工具
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月27日 上午9:37:30 1002360 新建
 */
public class DynamicDataSourceHolder {

    private DynamicDataSourceHolder() {
        throw new IllegalStateException("Utility class");
    }

    // 使用ThreadLocal把数据源与当前线程绑定, 以便存放变量副本。当线程的变量值为空时返回默认值
    private static final ThreadLocal<String> DATA_SOURCE_HOLDER = ThreadLocal
            .withInitial(() -> DsConstants.MASTER_DATASOURCE);

    public static void setDataSource(String dataSourceName) {
        DATA_SOURCE_HOLDER.set(dataSourceName);
    }

    public static String getDataSource() {
        return DATA_SOURCE_HOLDER.get();
    }

    public static void clearDataSource() {
        DATA_SOURCE_HOLDER.remove();
    }

}
