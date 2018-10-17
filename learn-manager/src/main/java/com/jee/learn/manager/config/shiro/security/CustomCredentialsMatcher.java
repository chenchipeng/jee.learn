package com.jee.learn.manager.config.shiro.security;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import com.jee.learn.manager.config.shiro.ShiroContants;
import com.jee.learn.manager.util.text.EncodeUtils;
import com.jee.learn.manager.util.text.EscapeUtil;
import com.jee.learn.manager.util.text.HashUtil;

/**
 * 自定义shiro密码匹配器
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月12日 上午9:51:18 ccp 新建
 */
public class CustomCredentialsMatcher extends HashedCredentialsMatcher {

    public CustomCredentialsMatcher() {
        super();
        super.setHashAlgorithmName(ShiroContants.HASH_ALGORITHM);
        super.setHashIterations(ShiroContants.HASH_INTERATIONS);
    }

    /**
     * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
     */
    public static String entryptPassword(String plainPassword) {
        String plain = EscapeUtil.unescapeHtml(plainPassword);
        byte[] salt = HashUtil.generateSalt(ShiroContants.SALT_LENGTH);
        byte[] hashPassword = HashUtil.sha1(plain.getBytes(), salt, ShiroContants.HASH_INTERATIONS);
        return EncodeUtils.encodeHex(salt) + EncodeUtils.encodeHex(hashPassword);
    }

    /**
     * 验证密码
     * 
     * @param plainPassword 明文密码
     * @param password 密文密码
     * @return 验证成功返回true
     */
    public static boolean validatePassword(String plainPassword, String password) {
        String plain = EscapeUtil.unescapeHtml(plainPassword);
        byte[] salt = EncodeUtils.decodeHex(password.substring(0, ShiroContants.SALT_SIZE));
        byte[] hashPassword = HashUtil.sha1(plain.getBytes(), salt, ShiroContants.HASH_INTERATIONS);
        return password.equals(EncodeUtils.encodeHex(salt) + EncodeUtils.encodeHex(hashPassword));
    }

}
