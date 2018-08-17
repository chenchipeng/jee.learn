package com.jee.learn.interfaces.config.datasource.dynamic;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * AOP 方式动态切换数据源
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月27日 下午8:14:15 1002360 新建
 */
@Order(Ordered.LOWEST_PRECEDENCE - 1)
@Aspect
@Component
public class DynamicDataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut("execution(public * com.jee.learn.interfaces.repository.*.*(..))")
    public void repository() {
    };

    @Pointcut("@annotation(com.jee.learn.interfaces.config.datasource.dynamic.TargetDataSource)")
    public void anno() {
    };

    @Around("anno() || repository()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();

        try {
            if (method.isAnnotationPresent(TargetDataSource.class)) {

                String targetDataSource = method.getAnnotation(TargetDataSource.class).dsType();
                DynamicDataSourceHolder.setDataSource(targetDataSource);

                logger.debug(">>>>>> {}.{} used {}", methodSignature.getDeclaringTypeName(), method.getName(),
                        targetDataSource);
            }
            // 执行方法
            Object result = pjp.proceed();
            return result;
        } finally {
            // 防止内存泄露
            DynamicDataSourceHolder.clearDataSource();
        }
    }

}
