package com.jee.learn.mybatis.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lambda 表达式测试<br/>
 * 感觉还是老旧的循环快，尽管不好看。少数据量的时候倒没啥问题
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月22日 下午3:09:37 ccp 新建
 */
@RestController
public class LambdaController {

    private List<Integer> dataList() {
        Integer size = 2000;
        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }

    @GetMapping("hello")
    public String hello() {
        return "hello ";
    }

    @GetMapping("run1")
    public String run1() {
        List<Integer> list = dataList();
        long l = System.nanoTime();
        for (int i = 0, j = list.size(); i < j; i++) {
            if (i == -1) {
            }
        }
        return "run1耗时 " + (System.nanoTime() - l);
    }

    @GetMapping("run2")
    public String run2() {
        List<Integer> list = dataList();
        long l = System.nanoTime();
        list.forEach(item -> {
            if (item == -1) {
            }
        });
        return "run2耗时 " + (System.nanoTime() - l);
    }

}
