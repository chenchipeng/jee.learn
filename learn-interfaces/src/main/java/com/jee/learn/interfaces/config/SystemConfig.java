package com.jee.learn.interfaces.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * 自定义系统属性配置
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年10月9日 上午11:32:17 ccp 新建
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "system")
public class SystemConfig {

    /** 实体类所在包 */
    private String entityBasePackage;

    /** 文件上传基础目录 */
    private String fileuploadBasedir = "/data/file";

    /** 文件访问基础url */
    protected String fileContent = "http://127.0.0.1";

}
