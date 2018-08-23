package com.jee.learn.interfaces.config.datasource.dynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import com.jee.learn.interfaces.config.datasource.DsConstants;

/**
 * 动态路由数据源
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月27日 上午9:39:28 1002360 新建
 */
@Component("dynamicDataSource")
@Primary
public class DynamicDataSource extends AbstractRoutingDataSource {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationContext applicationContext;

    private List<String> dsKeys = new ArrayList<>();;

    @Override
    protected Object determineCurrentLookupKey() {
        // 可以做一个简单的负载均衡策略
        String lookupKey = DynamicDataSourceHolder.getDataSource();

        // 系统启动时初始化数据库连接, 可以针对个别库做延迟初始化
        if (!dsKeys.isEmpty()) {
            lookupKey = dsKeys.remove(dsKeys.size() - 1);
        }

        logger.debug("------------ the lookupKey is {} ------------", lookupKey);
        return lookupKey;
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, DataSource> dataSources = applicationContext.getBeansOfType(DataSource.class);

        if (dataSources.size() == 0) {
            throw new IllegalStateException("Datasource can not found!!!");
        }
        // exclude current datasource
        Map<Object, Object> targetDataSource = excludeCurrentDataSource(dataSources);
        setTargetDataSources(targetDataSource);

        // default datasource
        setDefaultTargetDataSource(targetDataSource.get(DsConstants.MASTER_DATASOURCE));

        super.afterPropertiesSet();
    }

    /**
     * exclude current Datasource
     * 
     * @param dataSources
     * @return
     */
    private Map<Object, Object> excludeCurrentDataSource(Map<String, DataSource> dataSources) {
        Map<Object, Object> targetDataSource = new HashMap<>(dataSources.size());
        for (Entry<String, DataSource> e : dataSources.entrySet()) {
            if (!(e.getValue() instanceof DynamicDataSource)) {
                targetDataSource.put(e.getKey(), e.getValue());
                dsKeys.add(e.getKey());
            }
        }
        return targetDataSource;
    }

}
