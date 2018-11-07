package com.jee.learn.manager.security;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jee.learn.manager.config.shiro.security.CustomCredentialsMatcher;
import com.jee.learn.manager.util.security.MD5Util;

public class CredentialsMatcherTest {

    private static final Logger log = LoggerFactory.getLogger(CredentialsMatcherTest.class);

    @Test
    public void crestePasswordTest() {
        String plainPassword = "admin";
        plainPassword = MD5Util.md5Hex(plainPassword);
        log.debug("{}",plainPassword);
        
        String password = CustomCredentialsMatcher.entryptPassword(plainPassword);
        log.debug("{}", password);

        log.debug("{}", CustomCredentialsMatcher.validatePassword(plainPassword, password));
    }

}
