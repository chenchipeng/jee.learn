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
import com.jee.learn.interfaces.support.jpa.dao.Condition.Operator;
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

	/**
	 * {@link EntityDao} 解耦测试, 允许独立注入使用
	 */
	@Test
	public void findOneTest() {
		ApiUser u = null;
		try {

			Condition condition = new Condition("id", Operator.EQ, "1");
			u = entityDao.findOne(ApiUser.class, condition);
			logger.info("{}", JsonMapper.toJson(u));

			u = entityDao.findOne(ApiUser.class, "1");
			logger.info("{}", JsonMapper.toJson(u));

			u = entityDao.findOne(ApiUser.class, "id", "1");
			logger.info("{}", JsonMapper.toJson(u));

		} catch (Exception e) {
			logger.info("", e);
		}
	}

}
