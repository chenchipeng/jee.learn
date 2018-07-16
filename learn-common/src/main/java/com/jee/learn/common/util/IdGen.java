/**
 * Copyright &copy; 2015-2020 <a href="http://www.chinaskin.net/">chnskin</a> All rights reserved.
 */
package com.jee.learn.common.util;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * 
 * @author chnskin
 * @version 2013-01-15
 */
@Component
@Lazy(false)
public class IdGen {

    /** 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割. */
    public String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private static SecureRandom random = new SecureRandom();

    /** 使用SecureRandom随机生成Long. */
    public long randomLong() {
        return random.nextInt(Integer.MAX_VALUE);
    }

    private static String date;
    private static long orderNum = 0L;

    /** 数字类型单据编号 */
    public synchronized String numid() {

        String str = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
        if (date == null || !date.equals(str)) {
            date = str;
            orderNum = 0l;
        }
        orderNum++;
        long orderNo = Long.parseLong((date)) * 100;
        orderNo += orderNum;

        // 生成100到999的随机数
        Random random = new Random();
        int r = random.nextInt(9999) % (9999 - 1000 + 1) + 1000;

        return orderNo + "" + String.valueOf(r);
    }

}
