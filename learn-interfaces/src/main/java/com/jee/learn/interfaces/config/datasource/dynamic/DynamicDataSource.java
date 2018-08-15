package com.jee.learn.interfaces.config.datasource.dynamic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.jee.learn.interfaces.config.datasource.DsTypeEnum;

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

    private static int idx = 0;

    @Override
    protected Object determineCurrentLookupKey() {
        // 可以做一个简单的负载均衡策略
        String lookupKey = DynamicDataSourceHolder.getDataSource();

        if (idx < DsTypeEnum.values().length) {
            lookupKey = DsTypeEnum.values()[idx].value();
            idx++;
        }

        logger.debug("------------ the lookupKey is {} ------------", lookupKey);
        return lookupKey;
    }
}
