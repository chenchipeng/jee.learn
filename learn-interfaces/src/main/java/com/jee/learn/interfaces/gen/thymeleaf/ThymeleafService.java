package com.jee.learn.interfaces.gen.thymeleaf;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.ISpringTemplateEngine;

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
	@Qualifier("genTemplateEngine")
	private ISpringTemplateEngine genTemplateEngine;

	/**
	 * 渲染模板
	 * 
	 * @param template 模板文件路径, 不需要带后缀
	 * @param context 填充的数据内容
	 * @param filePath 生成的文件输出路径
	 * @throws IOException
	 */
	public void writeToFile(String template, Context context, String filePath) throws IOException {
		FileWriter write = new FileWriter(filePath);
		genTemplateEngine.process(template, context, write);
	}

	/**
	 * 渲染模板
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
	private Context buildContext(Map<String, Object> map) {
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
