package com.jee.learn.manager.util;

/**
 * 通用常量池
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月14日 下午4:41:27 1002360 新建
 */
public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    /** 逗号 */
    public static final String COMMA = ",";
    /** 点(句)号 */
    public static final String PERIOD = ".";
    /** 冒号 */
    public static final String COLON = ":";

    /** 01是否-否 */
    public static final String N = "0";
    /** 01是否-是 */
    public static final String Y = "1";

    /** 超管的id */
    public static final String ADMIN_ID = "1";

    /** 主键属性名称 */
    public static final String PRIMARY_KEY_NAME = "id";
    /** 删除标记属性名称 */
    public static final String DEL_FLAG_NAME = "delFlag";
    /** 通用排序号属性名称 */
    public static final String SORT_NAME = "sort";

}
