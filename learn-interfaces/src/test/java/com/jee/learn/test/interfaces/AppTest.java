package com.jee.learn.test.interfaces;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jee.learn.interfaces.util.exception.IntfcException;

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
    
    @Test
    public void instanceofTest() {
        Exception e1 = new Exception();
        IntfcException e2 = new IntfcException();
        Exception e3 = new IntfcException();
        
        System.out.println(e1 instanceof Exception);
        System.out.println(e1 instanceof IntfcException);
        System.out.println(e2 instanceof Exception);
        System.out.println(e2 instanceof IntfcException);
        System.out.println(e3 instanceof Exception);
        System.out.println(e3 instanceof IntfcException);
        System.out.println("-------");
        System.out.println(e3.getClass().getName());
        System.out.println(IntfcException.class.isInstance(e3));
    }

}
