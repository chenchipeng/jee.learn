package com.jee.learn.interfaces.util.validate;

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
     * @return 成功时返回NULL, 校验不通过则返回List
     */
    public List<String> validate(Object object, Class<?>... groups) {
        try {
            BeanValidators.validateWithException(validator, object);
        } catch (ConstraintViolationException e) {
            List<String> list = BeanValidators.extractPropertyAndMessageAsList(e,
                    BeanValidators.VALIDATION_ERROR_SEPARATOR);
            list.add(0, BeanValidators.ERROR_MESSAGE_TITLE);
            return list;
        }
        return null;
    }

    /**
     * bean 对象校验
     * 
     * @param object
     * @param groups
     * @return 成功时返回NULL, 校验不通过则返回List
     */
    public List<String> extractValidateMessage(Object object, Class<?>... groups) {
        try {
            BeanValidators.validateWithException(validator, object);
        } catch (ConstraintViolationException e) {
            List<String> list = BeanValidators.extractMessage(e);
            list.add(0, BeanValidators.ERROR_MESSAGE_TITLE);
            return list;
        }
        return null;
    }

    /**
     * bean 对象校验
     * 
     * @param object
     * @param groups
     * @return 成功时返回NULL, 校验不通过则返回List
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
