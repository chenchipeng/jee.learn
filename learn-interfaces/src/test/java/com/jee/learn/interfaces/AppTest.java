package com.jee.learn.interfaces;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 小工具测试
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月5日 下午3:39:46 1002360 新建
 */
public class AppTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void hello() {
        logger.debug("hello");
    }

}
