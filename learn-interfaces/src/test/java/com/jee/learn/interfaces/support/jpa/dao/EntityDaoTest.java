package com.jee.learn.interfaces.support.jpa.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.interfaces.LearnInterfaceApplication;
import com.jee.learn.interfaces.domain.ApiUser;
import com.jee.learn.interfaces.service.api.ApiUserService;
import com.jee.learn.interfaces.util.mapper.JsonMapper;

/**
 * EntityDaoTest
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月1日 下午5:40:07 ccp 新建
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnInterfaceApplication.class)
public class EntityDaoTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntityDao entityDao;
    
    @Autowired
    private ApiUserService apiUserService;

    @Test
    public void findOneTest() {
//        ApiUser u = entityDao.findOne(ApiUser.class, "id", "1");
        
        ApiUser u = apiUserService.findOne("1");
        logger.info("{}", JsonMapper.toJson(u));
    }

}
