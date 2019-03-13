package com.jee.learn.interfaces.gen.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.interfaces.LearnInterfaceApplication;
import com.jee.learn.interfaces.gen.domain.GenTable;
import com.jee.learn.interfaces.util.mapper.JsonMapper;

/**
 * GenTableServiceTest
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月6日 上午11:31:19 ccp 新建
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnInterfaceApplication.class)
public class GenTableServiceTest {

    @Autowired
    private GenTableService genTableService;

    @Test
    public void findOneByIdTest() {
        try {
            GenTable genTable = genTableService.findOneById("4028befe69511eac0169511ebb5f0000");
            System.out.println(JsonMapper.toJson(genTable));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void saveTest() {
        try {
            GenTable genTable = new GenTable();
            genTable.setName(String.valueOf(System.currentTimeMillis()));
            genTableService.save(genTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
