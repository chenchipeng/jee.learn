package com.jee.learn.test.service;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.LearnManagerApplication;
import com.jee.learn.model.sys.SysLog;
import com.jee.learn.service.sys.SysLogService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnManagerApplication.class)
public class SysLogServiceTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysLogService logService;

    /**
     * 分页测试
     */
    @Test
    @Transactional
    @Rollback(true)
    public void loadTest() {
        Map<String, String> params = new HashMap<>();
        params.put("title", "系统登录");
        Page<SysLog> page = logService.load(params, 1, 10, "createDate desc");
        logger.info("总页数 - {}", page.getTotalPages());
    }

}
