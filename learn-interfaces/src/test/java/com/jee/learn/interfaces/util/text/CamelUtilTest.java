package com.jee.learn.interfaces.util.text;

import org.junit.Test;

/**
 * CamelUtilTest
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月7日 下午2:43:24 ccp 新建
 */
public class CamelUtilTest {

    @Test
    public void batchTest() {
        String str = "TEST_DATA";
        System.out.println(str + " -> " + CamelUtil.upperUnderscoreToUpperCamel(str));

        str = "TEST_DATA";
        System.out.println(str + " -> " + CamelUtil.upperUnderscoreToLowerCamel(str));

        str = "test_data";
        System.out.println(str + " -> " + CamelUtil.lowerUnderscoreToUpperCamel(str));

        str = "test_data";
        System.out.println(str + " -> " + CamelUtil.lowerUnderscoreToLowerCamel(str));

        str = "test_data";
        System.out.println(str + " -> " + CamelUtil.toClassName(str));
        str = "TEST_DATA";
        System.out.println(str + " -> " + CamelUtil.toClassName(str));

        str = "test_data";
        System.out.println(str + " -> " + CamelUtil.toFieldName(str));
        str = "TEST_DATA";
        System.out.println(str + " -> " + CamelUtil.toFieldName(str));
    }
}
