package com.jee.learn.test.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * Lambda 表达式测试
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月22日 下午12:36:58 ccp 新建
 */
public class LambdaTest {

    @Test
    public void lambdaTest1() {

        // 数据准备
        int size = 100000000;
        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < 128; i++) {
            list.add(i);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                run1(list, "normal");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                run2(list, "one");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                run2(list, "two");
            }
        }).start();

    }

    private void run1(List<Integer> list, String thName) {
        long l = System.currentTimeMillis();
        // 普通循环
        for (int i = 0, j = list.size(); i < j; i++) {
            if (i == -1) {
                // ...
            }
        }
        System.out.println(thName + "-run1耗时 " + (System.currentTimeMillis() - l));
    }

    private void run2(List<Integer> list, String thName) {
        long l = System.currentTimeMillis();
        list.forEach(item -> {
            if (item == -1) {
                // ...
            }
        });
        System.out.println(thName + "-run2耗时 " + (System.currentTimeMillis() - l));
    }

}
