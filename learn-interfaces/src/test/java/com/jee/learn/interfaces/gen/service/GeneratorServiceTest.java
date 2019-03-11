package com.jee.learn.interfaces.gen.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.interfaces.LearnInterfaceApplication;
import com.jee.learn.interfaces.util.mapper.JsonMapper;

/**
 * GeneratorServiceTest
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月6日 上午11:31:31 ccp 新建
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnInterfaceApplication.class)
public class GeneratorServiceTest {

    @Autowired
    private GeneratorService generatorService;

    @Test
    public void selectDataTablesTest() {
        generatorService.selectDataTables();
    }

    @Test
    public void selectTableColumnTest() {
        System.out.println(JsonMapper.toJson(generatorService.selectTableColumn("dual:dual")));
    }

    @Test
    public void analizeTableKeyTest() {
        System.out.println(JsonMapper.toJson(generatorService.analizeTableKey("a:1")));
    }

}
