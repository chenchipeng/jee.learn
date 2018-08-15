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
@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class DynamicDataSourceAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // @Pointcut("execution(public * com.jee.learn.interfaces.service..*.*(..))")
    public void publicService() {
    };

    // @Pointcut("execution(public * com.jee.learn.interfaces.repository..*.*(..))")
    public void repository() {
    };

    @Pointcut("@annotation(com.jee.learn.interfaces.config.datasource.dynamic.TargetDataSource)")
    public void targetDataSource() {
    };

    @Around("targetDataSource() && @annotation(anno)")
    public Object around(ProceedingJoinPoint pjp, TargetDataSource anno) throws Throwable {

        try {
            String targetDataSource = anno.dsType().value();
            DynamicDataSourceHolder.setDataSource(targetDataSource);

            if (logger.isDebugEnabled()) {
                MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
                Method targetMethod = methodSignature.getMethod();
                logger.debug(">>>>>> {}.{} used {}", methodSignature.getDeclaringTypeName(), targetMethod.getName(),
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
