/**
 * Copyright &copy; 2015-2020 <a href="http://www.chinaskin.net/">chnskin</a> All rights reserved.
 */
package com.jee.learn.interfaces.util.validate;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * JSR303 Validator(Hibernate Validator)工具类.
 * 
 * ConstraintViolation中包含propertyPath, message 和invalidValue等信息.
 * 提供了各种convert方法，适合不同的i18n需求: 1. List<String>, String内容为message 2.
 * List<String>, String内容为propertyPath + separator + message 3.
 * Map<propertyPath, message>
 * 
 * 详情见wiki: https://github.com/springside/springside4/wiki/HibernateValidator
 * 
 * @author calvin
 * @version 2013-01-15
 * 
 * @remark ccp: 2018-12-21: 统一变量定义,
 *         补充validatePropertyWithException和validateValueWithException两个方法
 */
public class BeanValidators {

    /**
     * 校验不通过时返回的字段与提示的分隔符. {@link ConstraintViolationException#getMessage()}
     */
    public static final String VALIDATION_ERROR_SEPARATOR = ": ";

    /**
     * 校验不通过时返回信息的标题
     */
    public static final String ERROR_MESSAGE_TITLE = "数据验证失败：";

    /**
     * 调用JSR303的validate方法, 验证失败时抛出ConstraintViolationException.
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void validateWithException(Validator validator, Object object, Class<?>... groups)
            throws ConstraintViolationException {
        Set constraintViolations = validator.validate(object, groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    /**
     * 辅助方法,
     * 转换ConstraintViolationException中的Set<ConstraintViolations>中为List<message>.
     */
    public static List<String> extractMessage(ConstraintViolationException e) {
        return extractMessage(e.getConstraintViolations());
    }

    /**
     * 辅助方法, 转换Set<ConstraintViolation>为List<message>
     */
    @SuppressWarnings("rawtypes")
    public static List<String> extractMessage(Set<? extends ConstraintViolation> constraintViolations) {
        List<String> errorMessages = Lists.newArrayList();
        for (ConstraintViolation violation : constraintViolations) {
            errorMessages.add(violation.getMessage());
        }
        return errorMessages;
    }

    /**
     * 辅助方法,
     * 转换ConstraintViolationException中的Set<ConstraintViolations>为Map<property,
     * message>.
     */
    public static Map<String, String> extractPropertyAndMessage(ConstraintViolationException e) {
        return extractPropertyAndMessage(e.getConstraintViolations());
    }

    /**
     * 辅助方法, 转换Set<ConstraintViolation>为Map<property, message>.
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, String> extractPropertyAndMessage(
            Set<? extends ConstraintViolation> constraintViolations) {
        Map<String, String> errorMessages = Maps.newHashMap();
        for (ConstraintViolation violation : constraintViolations) {
            errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errorMessages;
    }

    /**
     * 辅助方法,
     * 转换ConstraintViolationException中的Set<ConstraintViolations>为List<propertyPath
     * message>.
     */
    public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e) {
        return extractPropertyAndMessageAsList(e.getConstraintViolations(), StringUtils.SPACE);
    }

    /**
     * 辅助方法, 转换Set<ConstraintViolations>为List<propertyPath message>.
     */
    @SuppressWarnings("rawtypes")
    public static List<String> extractPropertyAndMessageAsList(
            Set<? extends ConstraintViolation> constraintViolations) {
        return extractPropertyAndMessageAsList(constraintViolations, StringUtils.SPACE);
    }

    /**
     * 辅助方法,
     * 转换ConstraintViolationException中的Set<ConstraintViolations>为List<propertyPath
     * +separator+ message>.
     */
    public static List<String> extractPropertyAndMessageAsList(ConstraintViolationException e, String separator) {
        return extractPropertyAndMessageAsList(e.getConstraintViolations(), separator);
    }

    /**
     * 辅助方法, 转换Set<ConstraintViolation>为List<propertyPath +separator+ message>.
     */
    @SuppressWarnings("rawtypes")
    public static List<String> extractPropertyAndMessageAsList(Set<? extends ConstraintViolation> constraintViolations,
            String separator) {
        List<String> errorMessages = Lists.newArrayList();
        for (ConstraintViolation violation : constraintViolations) {
            errorMessages.add(violation.getPropertyPath() + separator + violation.getMessage());
        }
        return errorMessages;
    }

    //////// 20181221 新增方法 ////////

    /**
     * 单个属性校验<br/>
     * 
     * 调用JSR303的validate方法, 验证失败时抛出ConstraintViolationException.
     * 
     * @param validator
     * @param object
     * @param propertyName
     * @param groups
     * @throws ConstraintViolationException
     */
    public static void validatePropertyWithException(Validator validator, Object object, String propertyName,
            Class<?>... groups) throws ConstraintViolationException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validateProperty(object, propertyName,
                groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    /**
     * 单个属性校验<br/>
     * 
     * 调用JSR303的validate方法, 验证失败时抛出ConstraintViolationException.
     * 
     * @param validator
     * @param beanType
     * @param propertyName
     * @param value
     * @param groups
     * @throws ConstraintViolationException
     */
    public static void validateValueWithException(Validator validator, Class<Object> beanType, String propertyName,
            Object value, Class<?>... groups) throws ConstraintViolationException {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validateValue(beanType, propertyName, value,
                groups);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }
}