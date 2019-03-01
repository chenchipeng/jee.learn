/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.jee.learn.interfaces.util.idgen;

import java.util.UUID;

import com.jee.learn.interfaces.util.number.RandomUtil;
import com.jee.learn.interfaces.util.time.ClockUtil;
import com.jee.learn.interfaces.util.time.DateFormatUtil;

/**
 * 封装各种生成唯一性ID算法的工具类.
 * 
 * @author ThinkGem
 * @version 2014-8-19
 */

/**
 * 封装各种ID/KEY的工具类<br>
 * 参考1:ThinkGem https://gitee.com/thinkgem/jeesite4 参考2:calvin https://github.com/vipshop/vjtools/blob/master/vjkit/
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年2月28日 下午5:58:08 ccp 新建
 */
public class IdGenerate {

    private static IdWorker idWorker = new IdWorker(-1, -1);
    private static String numIdPrefix;
    private static long numIdSuffix = 0L;

    /**
     * 生成32位UUID, 中间无-分割.
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取新唯一编号（18为数值） 来自于twitter项目snowflake的id产生方案，全局唯一，时间有序。 64位ID (42(毫秒)+5(机器ID)+5(业务编码)+12(重复累加))
     */
    public static String nextId() {
        return String.valueOf(idWorker.nextId());
    }

    /**
     * 生成定长的随机字母数字
     * 
     * @param length
     * @return
     */
    public static String randomStringFixLength(int length) {
        return RandomUtil.randomStringFixLength(length);
    }

    /**
     * 18位数字类型单号生成
     * 
     * @return
     */
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
        int r = RandomUtil.nextInt(100, 999);
        return String.valueOf(orderNo) + String.valueOf(r);
    }

}