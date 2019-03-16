package com.jee.learn.interfaces.gen;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * thymeleaf 文件生成
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年2月12日 下午6:26:46 ccp 新建
 */
@Component
public class ThymeleafService {

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 渲染模板. 由于与前端页面共用引擎, 代码模板强烈建议与html页面放在一起, 且模板的后缀必为 ".html"
     * 
     * @param template 模板文件路径, 不需要带后缀
     * @param context 填充的数据内容
     * @param filePath 生成的文件输出路径
     * @throws IOException
     */
    public void writeToFile(String template, Context context, String filePath) throws IOException {
        FileWriter write = new FileWriter(filePath);
        templateEngine.process(template, context, write);
    }

    /**
     * 渲染模板. 由于与前端页面共用引擎, 代码模板强烈建议与html页面放在一起, 且模板的后缀必为 ".html"
     * 
     * @param template 模板文件路径, 不需要带后缀
     * @param map 填充的数据内容
     * @param filePath 生成的文件输出路径
     * @throws IOException
     */
    public void writeToFile(String template, Map<String, Object> map, String filePath) throws IOException {
        writeToFile(template, buildContext(map), filePath);
    }

    /**
     * 构造上下文(Model)
     * 
     * @param map 数据体
     * @return
     */
    public Context buildContext(Map<String, Object> map) {
        Context context = new Context();
        if (map == null) {
            return context;
        }
        for (Map.Entry<String, Object> entity : map.entrySet()) {
            context.setVariable(entity.getKey(), entity.getValue());
        }
        return context;
    }

}
