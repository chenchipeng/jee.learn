package com.jee.learn.manager.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jee.learn.manager.util.io.FilePathUtil;

public class FilePathUtilTest {

    private static final Logger logger = LoggerFactory.getLogger(FilePathUtilTest.class);

    @Test
    public void getParentPathTest() {
        String path = "E:data\\learn-manager\\file\\sysUserPhoto\\181114114553012346.jpg";
        String str = FilePathUtil.getParentPath(path);
        logger.debug("{}", str);
    }

}
