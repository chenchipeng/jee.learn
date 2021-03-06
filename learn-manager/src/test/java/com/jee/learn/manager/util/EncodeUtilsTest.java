package com.jee.learn.manager.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jee.learn.manager.util.idgen.IdGenerate;
import com.jee.learn.manager.util.text.EncodeUtil;

public class EncodeUtilsTest {

    private static final Logger log = LoggerFactory.getLogger(EncodeUtilsTest.class);

    @Test
    public void encodeBase64Test() {
        String uuid = IdGenerate.fastUUID();
        String str = EncodeUtil.encodeBase64(uuid);
        log.debug("{}", str);
    }

}
