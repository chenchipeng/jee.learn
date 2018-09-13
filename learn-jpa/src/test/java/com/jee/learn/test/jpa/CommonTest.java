package com.jee.learn.test.jpa;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月11日 上午9:59:10 ccp 新建
 */
public class CommonTest {

    @Test
    public void capitalizeTest() {
        String str = "del_flag";
        str = StringUtils.capitalize(str);
        System.out.println(str);
    }
}
