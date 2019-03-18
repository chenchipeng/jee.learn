package com.jee.learn.interfaces.gen;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.LearnInterfaceApplication;
import com.jee.learn.interfaces.gen.domain.GenScheme;
import com.jee.learn.interfaces.gen.domain.GenTable;
import com.jee.learn.interfaces.gen.domain.GenTableColumn;
import com.jee.learn.interfaces.gen.dto.GenTableColumnDto;
import com.jee.learn.interfaces.gen.dto.GenTableDto;
import com.jee.learn.interfaces.gen.service.GenSchemeService;
import com.jee.learn.interfaces.gen.service.GenTableColumnService;
import com.jee.learn.interfaces.gen.service.GenTableService;
import com.jee.learn.interfaces.gen.service.GeneratorService;
import com.jee.learn.interfaces.gen.thymeleaf.ThymeleafService;
import com.jee.learn.interfaces.util.mapper.BeanMapper;
import com.jee.learn.interfaces.util.time.DateFormatUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 代码生成
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月16日 下午5:04:34 ccp 新建
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LearnInterfaceApplication.class)
public class GenCode {

    @Autowired
    private ThymeleafService thymeleafService;

    @Autowired
    private GeneratorService generatorService;
    @Autowired
    private GenTableService genTableService;
    @Autowired
    private GenTableColumnService genTableColumnService;
    @Autowired
    private GenSchemeService genSchemeService;

    /** 写入所有表的元数据 */
    @Test
    @Transactional
    @Rollback(false)
    public void genCodeFromTables() {
        List<GenTableDto> tables = generatorService.getTabelList();
        for (GenTableDto genTableDto : tables) {
            genCodeFromTable(genTableDto.getName());
        }
    }

    /** 写入指定表的元数据 */
    @Test
    @Transactional
    @Rollback(false)
    public void genCodeFromTable() {
        genCodeFromTable("gen_template");
    }

    /** 元数据写入实现 */
    private void genCodeFromTable(String tableName) {
        GenTableDto tableDto = generatorService.getTebleInfo(tableName);
        // 表
        GenTable table = genTableService.findOneByName(tableName);
        if (table == null) {
            table = BeanMapper.map(tableDto, GenTable.class);
            genTableService.save(table);
        }
        log.info("{} id = {}", tableName, table.getId());
        // 列
        List<GenTableColumnDto> tableColumnDtos = tableDto.getColumnDtos();
        for (GenTableColumnDto genTableColumnDto : tableColumnDtos) {
            genTableColumnDto.setGenTableId(table.getId());
            GenTableColumn column = BeanMapper.map(genTableColumnDto, GenTableColumn.class);
            genTableColumnService.save(column);
        }
    }

    /** 写入生成方案 */
    @Test
    @Transactional
    @Rollback(false)
    public void schemeSetting() {
        String txt = "生成方案";
        String tid = "4028befe698eef8a01698eef9bda0000";

        String name = txt; // 名称
        String category = GenConstants.CURD; // 分类
        String packageName = "com.jee.learn.interfaces"; // 生成包路径
        String moduleName = "gen"; // 生成模块名
        String functionName = txt; // 生成功能名
        String functionNameSimple = txt; // 生成功能名(简写)
        String functionAuthor = "ccp"; // 生成功能作者
        String genTableId = tid; // 生成表编号

        GenScheme entity = new GenScheme(name, category, packageName, moduleName, functionName, functionNameSimple,
                functionAuthor, genTableId);
        genSchemeService.save(entity);

    }

    /** thymeleaf模板测试 */
    @Test
    public void writeToFileDemo() {
        List<String> list = new ArrayList<>(2);
        list.add("a");
        list.add("b");
        Map<String, Object> map = new HashMap<>();
        map.put("name", "xxx-" + System.currentTimeMillis());
        map.put("items", list);
        map.put("age", 30);

        try {
            thymeleafService.writeToFile("demo", map, "result.java");
        } catch (Exception e) {
            log.info("", e);
        }

        log.debug("日期格式化测试  -> {}", DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, new Date()));
    }

    /** thymeleaf模板测试 */
    @Test
    public void writeToFile() {
        String tid = "4028befe698eef8a01698eef9bda0000";
        String templet = "entity";// 模板文件
        String path = "src/main/java/com/jee/learn/interfaces/gen/domain/";// 输出路径

        GenScheme scheme = genSchemeService.findOneByGenTableId(tid);
        if (scheme == null) {
            log.info("GenScheme IS NULL");
            return;
        }
        GenTable table = genTableService.findOneById(tid);
        if (table == null) {
            log.info("GenTable IS NULL");
            return;
        }
        List<GenTableColumn> columns = genTableColumnService.findByGenTableId(tid);
        if (CollectionUtils.isEmpty(columns)) {
            log.info("List<GenTableColumn> IS EMPTY");
            return;
        }

        Map<String, Object> map = new HashMap<>(4);
        map.put("version", DateFormatUtil.formatDate(DateFormatUtil.PATTERN_DEFAULT_ON_SECOND, new Date()));
        map.put("scheme", scheme);
        map.put("table", table);
        map.put("columns", columns);
        try {
            thymeleafService.writeToFile(templet, map, path + table.getClassName() + ".java");
        } catch (Exception e) {
            log.info("thymeleaf generator reveive a exception", e);
        }
    }

}
