package com.jee.learn.mybatis.support;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mybatis update 拦截器, 拦截只写操作<br/>
 * 需要在mybatis-config.xml中手工注入
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月22日 下午4:48:55 ccp 新建
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class UpdateInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(UpdateInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameter = args[1];
        Executor executor = (Executor) invocation.getTarget();

        // TODO 自己要进行的各种处理
        logger.debug("进入mybatis update 拦截器");

        // 注：下面的方法可以根据自己的逻辑调用多次，在分页插件中，count 和 page 各调用了一次
        Object result = executor.update(ms, parameter);
        logger.debug("离开mybatis update 拦截器");
        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
