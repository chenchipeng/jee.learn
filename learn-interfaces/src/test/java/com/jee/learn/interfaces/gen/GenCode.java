package com.jee.learn.interfaces.gen;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.jee.learn.interfaces.LearnInterfaceApplication;
import com.jee.learn.interfaces.gen.dto.GenTableDto;
import com.jee.learn.interfaces.gen.service.GeneratorService;
import com.jee.learn.interfaces.gen.thymeleaf.ThymeleafService;
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

    /** 写入所有表的元数据 */
    @Test
    @Transactional
    @Rollback(false)
    public void genCodeFromTables() {
        List<GenTableDto> tables = generatorService.getTabelList();
        for (GenTableDto genTableDto : tables) {
            generatorService.genCodeFromTable(genTableDto.getName());
        }
    }

    // 第一, 写入指定表数据
    /** 写入指定表的元数据 */
    @Test
    @Transactional
    @Rollback(false)
    public void genCodeFromTable() {
        generatorService.genCodeFromTable("gen_template");
    }

    // 第二, 配置生成方案
    /** 写入生成方案 */
    @Test
    @Transactional
    @Rollback(false)
    public void schemeSetting() {

        String packageName = "com.jee.learn.interfaces"; // 生成包路径
        String moduleName = "gen"; // 生成模块名
        String functionAuthor = "ccp"; // 生成功能作者

        generatorService.schemeSetting("gen_template", packageName, moduleName, functionAuthor);
    }

    // 第三, 生成代码文件
    /** thymeleaf生成代码文件 */
    @Test
    public void writeToFile() {
        generatorService.writeToFile("sd_user");
    }

    /** 代码生成 */
    @Test
    public void genFile() {
        boolean isContinue = true;
        String tableName = "sys_user_role";
        generatorService.genCodeFromTable(tableName);
        isContinue = generatorService.schemeSetting(tableName, "net.jee.manager", "sys", "ccp");
        if (!isContinue) {
            return;
        }
        generatorService.writeToFile(tableName);
    }

    @Test
    public void genFiles() {
        String tables = "sys_area,sys_dict,sys_log,sys_menu,sys_office,sys_role,sys_role_menu,sys_role_office,sys_user,sys_user_role";
        String packageName = "net.jee.manager";
        String modelName = "sys";
        String author = "ccp";

        boolean isContinue = true;
        String[] ary = tables.split(",");
        for (int i = 0; i < ary.length; i++) {
            String tableName = ary[i];
            generatorService.genCodeFromTable(tableName);
            isContinue = generatorService.schemeSetting(tableName, packageName, modelName, author);
            if (!isContinue) {
                return;
            }
            generatorService.writeToFile(tableName);
        }

    }

}
