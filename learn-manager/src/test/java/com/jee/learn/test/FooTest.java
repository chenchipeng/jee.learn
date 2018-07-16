package com.jee.learn.test;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SimpleSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.LearnManagerApplication;
import com.jee.learn.common.mapper.JsonMapper;
import com.jee.learn.config.SysConfigBean;
import com.jee.learn.model.sys.SysLog;
import com.jee.learn.model.sys.SysUser;
import com.jee.learn.test.common.util.CustomObjectUtils;
import com.jee.learn.util.json.AjaxJson;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnManagerApplication.class)
public class FooTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${sys.name:系统}")
    private String sysCnName;

    @Autowired
    private SysConfigBean configBean;

    @Test
    public void propertiesReaddingTest() {
        logger.info("{} - {}", configBean.getName(), sysCnName);
    }

    @Test
    public void AjaxJsonTest() {
        AjaxJson j = new AjaxJson();
        j.setSuccess(false);
        j.setErrorCode("0");
        j.setMsg("没有登录!");
        logger.info("{}", JsonMapper.toJsonString(j));

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("success", false);
        map.put("errorCode", "0");
        map.put("msg", "没有登录!");
        logger.info("{}", JsonMapper.toJsonString(map));

    }

    @Test
    public void StringTest() {
        String str = "/sys/qq";
        String[] ary = str.split("/");
        String r = ary.length == 0 ? "manage" : ary[ary.length - 1];
        logger.info("{}", r);
    }

    @Test
    public void objectToMapTest() {
        SysLog log = new SysLog();
        log.setId("1");
        log.setCreateBy(new SysUser("9527"));

        try {
            Map<String, Object> so = CustomObjectUtils.objectToMap(log);
            logger.info("{}", so.size());
            Map<String, String> ss = CustomObjectUtils.objectToMapString(null, log);
            logger.info("{}", ss.size());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void sessionCaseTest() {
        SimpleSession simpleSession = new SimpleSession();
        simpleSession.setId(0);
        
        simpleSession=null;
        Session session = (Session)simpleSession;
        logger.info("{}",session==null);
    }
    
    @Test
    public void numberTest() {
        double d1=11;
        double d2=3;
        System.out.println(d1/d2);
    }

}
