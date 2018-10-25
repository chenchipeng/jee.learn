package com.jee.learn.manager.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jee.learn.manager.util.idgen.IdGenerate;

public class IdGenerateTest {

    private static final Logger log = LoggerFactory.getLogger(IdGenerateTest.class);

    @Test
    public void fastUUIDTest() {
        log.debug("{}", IdGenerate.fastUUID());
    }

    @Test
    public void randomLongTest() {
        log.debug("{}", IdGenerate.randomLong());

        long x = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            IdGenerate.randomLong();
        }
        log.debug("{}", (System.currentTimeMillis() - x));
    }

    @Test
    public void randomBase62Test() {
        log.debug("{}", IdGenerate.randomBase62(8));
    }

}
