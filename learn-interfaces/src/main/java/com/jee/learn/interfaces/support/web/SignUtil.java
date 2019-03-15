package com.jee.learn.interfaces.support.web;

/**
 * 接口签名工具
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月13日 下午6:20:43 ccp 新建
 */
public class SignUtil {

    private static final String SALT = "4d092ee240779ae8a34678593390422f";

    public static String sign(String str, String secret) {
        StringBuilder enValue = new StringBuilder();
        enValue.append(secret);
        enValue.append(str);
        enValue.append(SALT);
        // return encryptByMD5(enValue.toString());
        return null;
    }

}
