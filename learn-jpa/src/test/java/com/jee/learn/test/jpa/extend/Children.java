package com.jee.learn.test.jpa.extend;

import org.junit.Test;

/**
 * 实例化继承了父类的匿名子类对象
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月5日 下午4:40:25 1002360 新建
 */
public class Children {

    @Test
    public void instantiationTest() {
        System.out.println(first());
    }

    public String first() {

        String str = second("123", new Parent() {
            @Override
            protected String say(String str) {
                str = "xxoo";
                return str;
            }
        });

        return str;
    }

    public String second(String string, Parent parent) {
        return "abc" + string + parent.say("HHH");

    }

}
