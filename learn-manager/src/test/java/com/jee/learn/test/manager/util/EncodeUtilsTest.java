package com.jee.learn.test.manager.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jee.learn.manager.util.idgen.IdGenerate;
import com.jee.learn.manager.util.text.EncodeUtils;

public class EncodeUtilsTest {

    private static final Logger log = LoggerFactory.getLogger(EncodeUtilsTest.class);

    @Test
    public void encodeBase64Test() {
        String uuid = IdGenerate.fastUUID();
        String str = EncodeUtils.encodeBase64(uuid);
        log.debug("{}", str);
    }

}
