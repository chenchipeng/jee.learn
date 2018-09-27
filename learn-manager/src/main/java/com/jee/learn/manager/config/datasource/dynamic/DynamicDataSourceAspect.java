package com.jee.learn.manager.config.datasource.dynamic;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.jee.learn.manager.config.datasource.DsConstants;

/**
 * AOP 方式动态切换数据源
 * <p>
 * 在配置文件中没有配置spring.jpa.aop时默认不进行dao层拦截。
 * 存在配置时，dao层优先使用{@link TargetDataSource}指定的库，其次是{@link DsConstants}的SLAVE_DATASOURCE。
 * </p>
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

    @Value("${spring.jpa.aop:}")
    private String[] aopKeys;

    /** 暴露所有被 @TargetDataSource 注解的class和具体方法 */
    @Pointcut("@annotation(com.jee.learn.manager.config.datasource.dynamic.TargetDataSource)")
    public void anno() {
        // define a pointcut
    }

    /** 暴露repository包及其子包下的所有类 */
    @Pointcut("execution(* com.jee.learn.manager.repository..*(..))")
    public void repository() {
        // define a pointcut
    }

    /** 排除repository包及其子包下以"Impl结尾的类" */
    @Pointcut("!execution(public * com.jee.learn.manager.repository..*Impl.*(..))")
    public void impl() {
        // define a pointcut
    }

    /** 拦截所有被 @TargetDataSource 注解的class和具体方法 */
    @Around("anno()")
    public Object annoAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        try {
            if (method.isAnnotationPresent(TargetDataSource.class)) {
                changeDS(method.getAnnotation(TargetDataSource.class).dsType(), methodSignature.getDeclaringTypeName(),
                        method.getName());
            }
            // 执行方法
            return pjp.proceed();
        } finally {
            // 防止内存泄露
            DynamicDataSourceHolder.clearDataSource();
        }
    }

    /** 拦截整个repository并排除impl */
    @Around("repository() && impl()")
    public Object repositoryAround(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();

        try {
            // 有注解的话就是用注解指定的
            if (method.isAnnotationPresent(TargetDataSource.class)) {
                changeDS(method.getAnnotation(TargetDataSource.class).dsType(), methodSignature.getDeclaringTypeName(),
                        method.getName());
                // 执行方法
                return pjp.proceed();
            }
            // 没有注解就检查所配置的aopKeys与方法名
            for (String key : aopKeys) {
                if (method.getName().contains(key)) {
                    changeDS(DsConstants.SLAVE_DATASOURCE, methodSignature.getDeclaringTypeName(), method.getName());
                    break;
                }
            }
            // 执行方法
            return pjp.proceed();
        } finally {
            // 防止内存泄露
            DynamicDataSourceHolder.clearDataSource();
        }
    }

    /** 发起切换通知 */
    private void changeDS(String targetDataSource, String declaringTypeName, String methodName) {
        DynamicDataSourceHolder.setDataSource(targetDataSource);
        logger.debug(">>>>>> {}.{} used {}", declaringTypeName, methodName, targetDataSource);
    }

}
