package com.jee.learn.component.security;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;

import com.jee.learn.common.security.EntryptUtils;

/**
 * shiro 密码校验方式
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年5月10日 上午9:47:25 1002360 新建
 */
public class CustomCredentialsMatcher {

    /** 设定密码校验的Hash算法与迭代次数 */
    public static HashedCredentialsMatcher initHashedCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(EntryptUtils.HASH_ALGORITHM);
        matcher.setHashIterations(EntryptUtils.HASH_INTERATIONS);
        matcher.setStoredCredentialsHexEncoded(true);
        return matcher;
    }

}
