package com.jee.learn.test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.LearnManagerApplication;
import com.jee.learn.common.mapper.JsonMapper;
import com.jee.learn.model.sys.SysUser;
import com.jee.learn.service.sys.SysUserService;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnManagerApplication.class)
public class CacheTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private EntityManagerFactory emf;
    @Autowired
    private SysUserService sysUserService;

    /**
     * 一级缓存测试<br/>
     * 同一个session内部，一级缓存生效，同一个id的对象只有一个。不同session，一级缓存无效。
     */
    @Test
    public void firstCacheTest() {
        EntityManager em = emf.createEntityManager();
        SysUser d1 = em.find(SysUser.class, "1"); // find id为1的对象
        SysUser d2 = em.find(SysUser.class, "1"); // find id为1的对象
        logger.info((d1 == d2) + ""); // true
        em.close();

        EntityManager em1 = emf.createEntityManager();
        SysUser d3 = em1.find(SysUser.class, "1"); // find id为1的对象
        EntityManager em2 = emf.createEntityManager();
        SysUser d4 = em2.find(SysUser.class, "1"); // find id为1的对象
        logger.info((d3 == d4) + ""); // false

        em1.close();
        em2.close();
    }

    /**
     * 二级缓存测试，需要在实体上加@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)<br/>
     * 二级缓存生效，只输出了一条sql语句，同时监控中也出现了数据<br/>
     * 开启监控：spring.jpa.properties.hibernate.generate_statistics=true
     */
    @Test
    public void secondCachetest() {
        EntityManager em1 = emf.createEntityManager();
        SysUser d1 = em1.find(SysUser.class, "1");
        logger.info(d1.getName());
        em1.close();

        EntityManager em2 = emf.createEntityManager();
        SysUser d2 = em2.find(SysUser.class, "1");
        logger.info(d2.getName());
        em2.close();

        logger.info("========================================");

        SysUser d3 = sysUserService.findOne("1");
        logger.info(d3.getName());
        SysUser d4 = sysUserService.findOne("1");
        logger.info(d4.getName());

    }

    /**
     * 查询缓存测试<br/>
     * spring-data-jpa默认继承实现的一些方法，实现类为SimpleJpaRepository。该类中的方法不能通过@QueryHint来实现查询缓存。
     */
    @Test
    @Transactional(readOnly = true)
    public void queryCacheTest() {
        // 无效的spring-data-jpa实现的接口方法
        // 输出两条sql语句
        sysUserService.findAll();
        sysUserService.findAll();
        // 自己实现的dao层方法可以被查询缓存
        // 输出一条sql语句
        List<SysUser> list = sysUserService.findAllByNoDel();
        sysUserService.findAllByNoDel();
        // 使用JsonMapper.toJsonString()时需要@Transactional注解支持
        logger.info("{}", JsonMapper.toJsonString(list));

    }

}
