package com.jee.learn.interfaces.util.security;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {

    /**
     * 32位MD5
     * 
     * @param text 明文
     * @return 32位密文
     * @throws Exception
     */
    public static String md5Hex(String text) {
        // 加密后的字符串
        return DigestUtils.md5Hex(text);
    }

    /**
     * 16位MD5
     * 
     * @param text 明文
     * @return 16位密文
     * @throws Exception
     */
    public static String md5(String text)  {
        // 加密后的字符串
        return DigestUtils.md5Hex(text).substring(8, 24);
    }

}
