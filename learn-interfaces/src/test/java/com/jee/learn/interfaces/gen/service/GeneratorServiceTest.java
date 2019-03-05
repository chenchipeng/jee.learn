package com.jee.learn.interfaces.gen.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.jee.learn.interfaces.LearnInterfaceApplication;

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
		generatorService.selectTableColumn("gen_table:业务表");
	}

}
