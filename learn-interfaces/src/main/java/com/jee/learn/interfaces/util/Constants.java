package com.jee.learn.interfaces.util;

/**
 * 通用常量池
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年6月14日 下午4:41:27 1002360 新建
 */
public class Constants {

    private Constants() {
        throw new IllegalStateException("Utility class");
    }

    //////// 保存时自动填充数据的字段 ////////
    
    public static final String CREATE_BY = "create_by";
    public static final String CREATE_DATE = "create_date";
    public static final String UPDATE_BY = "update_by";
    public static final String UPDATE_DATE = "update_date";

}
