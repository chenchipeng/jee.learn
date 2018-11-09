/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jee.learn.manager.util.idgen;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.jee.learn.manager.util.time.ClockUtil;
import com.jee.learn.manager.util.time.DateFormatUtil;

import io.netty.util.internal.ThreadLocalRandom;

/**
 * 封装各种生成唯一性ID算法的工具类
 * 
 * @author thinkgem, vjtools, ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年9月27日 下午5:31:05 ccp 新建
 */
public class IdGenerate {

    private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    private static IdWorker idWorker = new IdWorker(-1, -1);
    private static String numIdPrefix;
    private static long numIdSuffix = 0L;

    /**
     * 生成UUID, 中间无-分割.<br/>
     * 返回使用ThreadLocalRandm的UUID，比默认的UUID性能更优
     */
    public static String fastUUID() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return String.valueOf(new UUID(random.nextLong(), random.nextLong())).replaceAll("-", "");
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static long randomLong() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return Math.abs(random.nextLong());
    }

    /**
     * 基于自定义Base62编码的SecureRandom随机生成bytes.
     */
    public static String randomBase62(int length) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = BASE62[((randomBytes[i] & 0xFF) % BASE62.length)];
        }
        return new String(chars);
    }

    /**
     * 获取新唯一编号（18为数值） 来自于twitter项目snowflake的id产生方案，全局唯一，时间有序。 64位ID (42(毫秒)+5(机器ID)+5(业务编码)+12(重复累加))
     */
    public static String nextId() {
        return String.valueOf(idWorker.nextId());
    }

    /**
     * 获取新代码编号
     */
    public static String nextCode(String code) {
        if (code != null) {
            String str = code.trim();
            int len = str.length() - 1;
            int lastNotNumIndex = 0;
            for (int i = len; i >= 0; i--) {
                if (!(str.charAt(i) >= '0' && str.charAt(i) <= '9')) {
                    lastNotNumIndex = i;
                    break;
                }
            }
            // 如果最后一位是数字，并且last索引位置还在最后，则代表是纯数字，则最后一个不是数字的索引为-1
            if ((str.charAt(len) >= '0' && str.charAt(len) <= '9') && (lastNotNumIndex == len)) {
                lastNotNumIndex = -1;
            }
            String prefix = str.substring(0, lastNotNumIndex + 1);
            String numStr = str.substring(lastNotNumIndex + 1, str.length());
            long num = Long.parseLong(numStr);
            str = prefix + StringUtils.leftPad(String.valueOf(num + 1), numStr.length(), "0");
            return str;
        }
        return null;
    }

    /** 单号生成 */
    public static synchronized String numid() {

        // yyMMddHHmmss
        String str = DateFormatUtil.SIMPLE_FORMAT.format(ClockUtil.currentDate());
        if (numIdPrefix == null || !numIdPrefix.equals(str)) {
            numIdPrefix = str;
            numIdSuffix = 0l;
        }

        numIdSuffix++;
        long orderNo = Long.parseLong(numIdPrefix) * 100;
        orderNo += numIdSuffix;

        // 生成100到999的随机数
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int r = random.nextInt(9999) % (8998) + 1000;

        return String.valueOf(orderNo) + String.valueOf(r);
    }

}
