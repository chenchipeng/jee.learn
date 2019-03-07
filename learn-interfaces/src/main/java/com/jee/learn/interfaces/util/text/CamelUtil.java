package com.jee.learn.interfaces.util.text;

import com.google.common.base.CaseFormat;

/**
 * 驼峰工具
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月6日 下午6:15:16 ccp 新建
 */
public class CamelUtil {

    /**
     * 根据驼峰命名规则生成类名
     * 
     * @param str
     * @return
     */
    public static String toClassName(String str) {
        return lowerUnderscoreToUpperCamel(str.toLowerCase());
    }

    /**
     * 根据驼峰命名规则生成字段
     * 
     * @param str
     * @return
     */
    public static String toFieldName(String str) {
        return lowerUnderscoreToLowerCamel(str.toLowerCase());
    }

    /**
     * TEST_DATA to TestData
     * 
     * @param str
     * @return
     */
    public static String upperUnderscoreToUpperCamel(String str) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, str);
    }

    /**
     * TEST_DATA to testData
     * 
     * @param str
     * @return
     */
    public static String upperUnderscoreToLowerCamel(String str) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
    }

    /**
     * test_data to TestData
     * 
     * @param str
     * @return
     */
    public static String lowerUnderscoreToUpperCamel(String str) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, str);
    }

    /**
     * test_data to testData
     * 
     * @param str
     * @return
     */
    public static String lowerUnderscoreToLowerCamel(String str) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
    }

}
