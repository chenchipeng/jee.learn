package com.jee.learn.interfaces.gen;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.LearnInterfaceApplication;
import com.jee.learn.interfaces.gen.domain.GenTable;
import com.jee.learn.interfaces.gen.domain.GenTableColumn;
import com.jee.learn.interfaces.gen.dto.GenTableColumnDto;
import com.jee.learn.interfaces.gen.dto.GenTableDto;
import com.jee.learn.interfaces.gen.service.GenTableColumnService;
import com.jee.learn.interfaces.gen.service.GenTableService;
import com.jee.learn.interfaces.gen.service.GeneratorService;
import com.jee.learn.interfaces.util.mapper.BeanMapper;

/**
 * 代码生成
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月16日 下午5:04:34 ccp 新建
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnInterfaceApplication.class)
public class GenCode {

    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private GenTableService genTableService;
    @Autowired
    private GenTableColumnService genTableColumnService;

    @Test
    public void genCodeFromTables() {
        List<GenTableDto> tables = generatorService.getTabelList();
        for (GenTableDto genTableDto : tables) {
            genCodeFromTable(genTableDto.getName());
        }
    }

    @Test
    @Transactional
    @Rollback(false)
    public void genCodeFromTable() {
        genCodeFromTable("api_user");
    }

    private void genCodeFromTable(String tableName) {
        GenTableDto tableDto = generatorService.getTebleInfo(tableName);
        // 表
        GenTable table = genTableService.findOneByName(tableName);
        if (table == null) {
            table = BeanMapper.map(tableDto, GenTable.class);
            genTableService.save(table);
        }
        // 列
        List<GenTableColumnDto> tableColumnDtos = tableDto.getColumnDtos();
        for (GenTableColumnDto genTableColumnDto : tableColumnDtos) {
            genTableColumnDto.setGenTableId(table.getId());
            GenTableColumn column = BeanMapper.map(genTableColumnDto, GenTableColumn.class);
            genTableColumnService.save(column);
        }
    }

}
