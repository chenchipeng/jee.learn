/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jee.learn.interfaces.util.security;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * 
 * @author ThinkGem
 * @version 2014-8-19
 */
public class IdGenerate {

    private static SecureRandom random = new SecureRandom();
    private static String date;
    private static long orderNum = 0L;

    /**
     * 生成32位UUID, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    /**
     * 18位数字类型编号
     */
    public static synchronized String numid() {

        String str = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
        if (date == null || !date.equals(str)) {
            date = str;
            orderNum = 0l;
        }
        orderNum++;
        long orderNo = Long.parseLong((date)) * 100;
        orderNo += orderNum;
        // 生成100到999的随机数
        int r = random.nextInt(9999) % (9999 - 1000 + 1) + 1000;
        return orderNo + "" + String.valueOf(r);

    }

    public static void main(String[] args) {
        System.out.println(uuid());
        System.out.println(randomLong());
        System.out.println(numid());
    }

}