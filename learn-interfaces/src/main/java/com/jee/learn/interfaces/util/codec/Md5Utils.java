package com.jee.learn.interfaces.util.codec;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {

    private static final String MD5 = "MD5";
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String ZERO = "0";

    public static String getMD5(String input) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance(MD5);
            messageDigest.reset();
            messageDigest.update(input.getBytes(DEFAULT_ENCODING));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException("MD5加密错误:" + e.getMessage(), e);
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append(ZERO).append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString();
    }

}
