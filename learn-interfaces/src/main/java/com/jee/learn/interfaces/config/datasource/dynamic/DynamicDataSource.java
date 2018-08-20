package com.jee.learn.interfaces.config.datasource.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 数据源动态路由
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月27日 上午9:39:28 1002360 新建
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    protected Logger logger = LoggerFactory.getLogger(getClass());
    private Object[] dsKeys;

    @Override
    protected Object determineCurrentLookupKey() {
        // 可以做一个简单的负载均衡策略
        String lookupKey = DynamicDataSourceHolder.getDataSource();

        // 系统启动时初始化数据库连接, 可以针对个别库做延迟初始化
        if (dsKeys.length > 0) {
            lookupKey = String.valueOf(dsKeys[dsKeys.length - 1]);
            dsKeys = removeLastItem(dsKeys);
        }

        logger.debug("------------ the lookupKey is {} ------------", lookupKey);
        return lookupKey;
    }

    public void setDsKeys(Object[] dsKeys) {
        this.dsKeys = dsKeys;
    }

    /** 删除数组的最后一个元素 */
    private Object[] removeLastItem(Object[] ary) {
        int len = ary.length;
        if (len > 0) {
            len -= 1;
            Object[] tmps = new Object[len];
            for (int i = 0; i < len; i++) {
                tmps[i] = ary[i];
            }
            ary = tmps;
        }
        return ary;
    }

}
