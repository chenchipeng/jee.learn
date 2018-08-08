package com.jee.learn.interfaces.config.datasource.dynamic;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * AOP 方式动态切换数据源
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月27日 下午8:14:15 1002360 新建
 */
@Component
@Aspect
public class DynamicDataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(public * com.jee.learn.interfaces.repository..*.*(..))")
    public void repositoryPointcut() {
    }

    @Around("repositoryPointcut()")
    public Object dataSourceSwitcher(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method targetMethod = methodSignature.getMethod();

        if (targetMethod.isAnnotationPresent(DataSourceTarget.class)) {
            String targetDataSource = targetMethod.getAnnotation(DataSourceTarget.class).dataSource();
            logger.debug(">>>>>> {}.{} used {}", methodSignature.getDeclaringTypeName(), targetMethod.getName(),
                    targetDataSource);
            DynamicDataSourceHolder.setDataSource(targetDataSource);
        }

        // 执行方法
        Object result = pjp.proceed();

        DynamicDataSourceHolder.clearDataSource();
        return result;
    }
}
