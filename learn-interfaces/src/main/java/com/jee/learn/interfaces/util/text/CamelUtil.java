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

    public static void main(String args[]) {
        testCaseFormat();
    }

    private static void testCaseFormat() {
        System.out.println(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "test-data"));
        System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "test_data"));
        System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "test_data"));

        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "testdata"));
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "TestData"));
        System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "testData"));
       
    }

    /**
     * test-data to testData
     * 
     * @param str
     * @return
     */
    public static String lowerHyphenToLowerCamel(String str) {
        return CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, str);
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
    /**
     * test_data to testData
     * 
     * @param str
     * @return
     */
    public static String upperUnderscoreToUpperCamel(String str) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, str);
    }
    /**
     * testdata to testdata
     * 
     * @param str
     * @return
     */
    public static String lowerCamelToLowerUnderscore(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
    }
    /**
     * TestData to test_data
     * 
     * @param str
     * @return
     */
    public static String lowerCamelToLowerCamel(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
    }
    /**
     * testData to test-data
     * 
     * @param str
     * @return
     */
    public static String upperCamelToLowerCamel(String str) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, str);
    }

    
}
