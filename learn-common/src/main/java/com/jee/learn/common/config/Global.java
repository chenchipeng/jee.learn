/**
 * Copyright &copy; 2015-2020 <a href="http://www.chinaskin.net/">chnskin</a> All rights reserved.
 */
package com.jee.learn.common.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

/**
 * 全局配置类
 * 
 * @author chnskin
 * @version 2014-06-25
 */
public class Global {

    /**
     * 当前对象实例
     */
    private static Global global = new Global();

    /**
     * 保存全局属性值
     */
    private static Map<String, String> map = new HashMap<>();

    /**
     * 属性文件加载对象
     */
    private static PropertiesLoader loader = new PropertiesLoader("application.properties");

    /**
     * 获取当前对象实例
     */
    public static Global getInstance() {
        return global;
    }

    /**
     * 获取配置
     * 
     * @see ${fns:getConfig('adminPath')}
     */
    public static String getConfig(String key) {
        String value = map.get(key);
        if (value == null) {
            value = loader.getProperty(key);
            map.put(key, value != null ? value : StringUtils.EMPTY);
        }
        return value;
    }

    /**
     * 获取管理端根路径
     */
    public static String getAdminPath() {
        return getConfig("adminPath");
    }

    /**
     * 获取前端根路径
     */
    public static String getFrontPath() {
        return getConfig("frontPath");
    }

    /**
     * 获取URL后缀
     */
    public static String getUrlSuffix() {
        return getConfig("urlSuffix");
    }

    /**
     * 是否是演示模式，演示模式下不能修改用户、角色、密码、菜单、授权
     */
    public static Boolean isDemoMode() {
        String dm = getConfig("demoMode");
        return "true".equals(dm) || "1".equals(dm);
    }

    /**
     * 页面获取常量
     * 
     * @see ${fns:getConst('YES')}
     */
    public static Object getConst(String field) {
        try {
            return Global.class.getField(field).get(null);
        } catch (Exception e) {
            // 异常代表无配置，这里什么也不做
        }
        return null;
    }

    /**
     * 获取工程路径
     * 
     * @return
     */
    public static String getProjectPath() {
        // 如果配置了工程路径，则直接返回，否则自动获取。
        String projectPath = Global.getConfig("projectPath");
        if (StringUtils.isNotBlank(projectPath)) {
            return projectPath;
        }
        try {
            File file = new DefaultResourceLoader().getResource("").getFile();
            if (file != null) {
                while (true) {
                    File f = new File(file.getPath() + File.separator + "src" + File.separator + "main");
                    if (f == null || f.exists()) {
                        break;
                    }
                    if (file.getParentFile() != null) {
                        file = file.getParentFile();
                    } else {
                        break;
                    }
                }
                projectPath = file.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return projectPath;
    }

    /**
     * 写入properties信息
     * 
     * @param key
     *            名称
     * @param value
     *            值
     */
    public static void modifyConfig(String key, String value) {
        try {
            // 从输入流中读取属性列表（键和元素对）
            Properties prop = getProperties();
            prop.setProperty(key, value);
            String path = Global.class.getResource("/application.properties").getPath();
            FileOutputStream outputFile = new FileOutputStream(path);
            prop.store(outputFile, "modify");
            outputFile.close();
            outputFile.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回 Properties
     * 
     * @param fileName
     *            文件名 (注意：加载的是src下的文件,如果在某个包下．请把包名加上)
     * @param
     * @return
     */
    public static Properties getProperties() {
        Properties prop = new Properties();
        try {
            Resource resource = new DefaultResourceLoader().getResource("/application.properties");
            prop.load(resource.getInputStream());
        } catch (Exception e) {
            return null;
        }
        return prop;
    }

}
