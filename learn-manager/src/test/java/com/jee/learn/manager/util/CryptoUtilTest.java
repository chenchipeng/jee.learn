package com.jee.learn.manager.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jee.learn.manager.util.security.CryptoUtil;
import com.jee.learn.manager.util.text.Charsets;

public class CryptoUtilTest {

    private static final Logger log = LoggerFactory.getLogger(CryptoUtilTest.class);

    @Test
    public void generateAesKeyTest() {
        byte[] bs = CryptoUtil.generateAesKey(256);
        String str = new String(bs, Charsets.ISO_8859_1);
        log.debug(str);
    }

}
