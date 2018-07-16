package com.jee.learn.test.common;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.common.LearnCommonApplication;

/**
 * 测试 demo
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年3月30日 下午3:19:44 1002360 新建
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnCommonApplication.class)
public class CommonFooTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test() {
        logger.info("hello");
    }

}
