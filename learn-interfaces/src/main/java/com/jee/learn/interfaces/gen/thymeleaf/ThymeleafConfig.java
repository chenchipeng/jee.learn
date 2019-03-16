package com.jee.learn.interfaces.gen.thymeleaf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import com.jee.learn.interfaces.util.text.Charsets;

/**
 * thymeleaf 代码生成配置<br/>
 * 参考:<br/>
 * 1.https://blog.codeleak.pl/2017/03/getting-started-with-thymeleaf-3-text.html<br/>
 * 2.https://github.com/php-coder/mystamps/issues/492<br/>
 * 
 * 在配置的时候, 注意要与写在配置文件上的TemplateEngine区分.
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月17日 上午1:41:09 ccp 新建
 */
@Configuration
public class ThymeleafConfig {

	// 模板文件存放点
	private static final String GEN_TEMPLATE_PATH = "/templates/gen/";
	// 模板文件后缀
	private static final String GEN_TEMPLATE_SUFFIX = ".gen";

	/**
	 * 构造引擎
	 * 
	 * @return genTemplateEngine
	 */
	@Bean(name = "genTemplateEngine")
	public ISpringTemplateEngine genTemplateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(genTemplateResolver());
		return templateEngine;
	}

	/**
	 * 构造模板解析器
	 * 
	 * @return genTemplateResolver
	 */
	private ITemplateResolver genTemplateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix(GEN_TEMPLATE_PATH);
		templateResolver.setSuffix(GEN_TEMPLATE_SUFFIX);
		templateResolver.setTemplateMode(TemplateMode.TEXT);
		templateResolver.setCharacterEncoding(Charsets.UTF_8_NAME);
		templateResolver.setCheckExistence(true);
		templateResolver.setCacheable(false);
		return templateResolver;
	}

}
