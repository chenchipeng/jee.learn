package com.jee.learn.interfaces.util.validate;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateUtil {

    @Autowired
    private Validator validator;

    /**
     * bean 对象校验
     * 
     * @param object
     * @param groups
     * @return 成功时返回NULL, 校验不通过则返回List. 形如["数据验证失败：","b: 不能为null","a: 不能为null"]
     */
    public List<String> validate(Object object, Class<?>... groups) {
        try {
            BeanValidators.validateWithException(validator, object, groups);
        } catch (ConstraintViolationException e) {
            List<String> list = new ArrayList<String>();
            list.add(0, BeanValidators.ERROR_MESSAGE_TITLE);
            list.addAll(BeanValidators.extractPropertyAndMessageAsList(e, BeanValidators.VALIDATION_ERROR_SEPARATOR));
            return list;
        }
        return null;
    }

    /**
     * bean 对象校验
     * 
     * @param object
     * @param groups
     * @return 成功时返回NULL, 校验不通过则返回List. 形如["数据验证失败：","不能为null","不能为null"]
     */
    public List<String> extractValidateMessage(Object object, Class<?>... groups) {
        try {
            BeanValidators.validateWithException(validator, object, groups);
        } catch (ConstraintViolationException e) {
            List<String> list = new ArrayList<String>();
            list.add(0, BeanValidators.ERROR_MESSAGE_TITLE);
            list.addAll(BeanValidators.extractMessage(e));
            return list;
        }
        return null;
    }

    /**
     * bean 对象校验
     * 
     * @param object
     * @param groups
     * @return 成功时返回NULL, 校验不通过则返回String. 形如 "数据验证失败： a: 不能为null b: 不能为null"
     */
    public String validateToString(Object object, Class<?>... groups) {
        return listToString(validate(object, groups));
    }

    //////// 工具 ////////

    /**
     * list 转 string
     * 
     * @param list
     * @return
     */
    public String listToString(List<String> list) {
        return CollectionUtils.isEmpty(list) ? StringUtils.EMPTY : StringUtils.join(list.toArray(), StringUtils.SPACE);
    }

}
