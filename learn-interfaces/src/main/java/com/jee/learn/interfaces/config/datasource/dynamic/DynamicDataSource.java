package com.jee.learn.interfaces.config.datasource.dynamic;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import com.jee.learn.interfaces.config.datasource.DsConstants;

/**
 * 数据源动态路由
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月27日 上午9:39:28 1002360 新建
 */
@Component("dynamicDataSource")
@Primary
public class DynamicDataSource extends AbstractRoutingDataSource implements ApplicationContextAware {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext applicationContext;
    private Object[] dsKeys;
    private int idx = 0;

    @Override
    protected Object determineCurrentLookupKey() {
        // 可以做一个简单的负载均衡策略
        String lookupKey = DynamicDataSourceHolder.getDataSource();

        if (idx < dsKeys.length) {
            lookupKey = String.valueOf(dsKeys[idx]);
            idx++;
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * exclude current Datasource
     * 
     * @param dataSources
     * @return
     */
    private Map<Object, Object> excludeCurrentDataSource(Map<String, DataSource> dataSources) {
        Map<Object, Object> targetDataSource = new HashMap<>(dataSources.size());
        Iterator<String> keys = dataSources.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            if (!(dataSources.get(key) instanceof DynamicDataSource)) {
                targetDataSource.put(key, dataSources.get(key));
            }
        }
        dsKeys = dataSources.keySet().toArray();
        return targetDataSource;
    }

}
