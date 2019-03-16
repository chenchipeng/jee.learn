package com.jee.learn.interfaces.gen;

/**
 * Gen 常量库
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月6日 下午2:19:03 ccp 新建
 */
public class GenConstants {

    private GenConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String YES = "YES";
    public static final String NO = "NO";

    public static final int Y = 1;
    public static final int N = 0;

    //////// 模板字段 ////////

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TITLE = "title";
    public static final String REMARKS = "remarks";
    public static final String CREATE_BY = "create_by";
    public static final String CREATE_DATE = "create_date";
    public static final String UPDATE_BY = "update_by";
    public static final String UPDATE_DATE = "update_date";
    public static final String DEL_FLAG = "del_flag";

    //////// 查询条件 ////////

    public enum QUERY_TYPE {

        EQUAL("=", "等于"), NOT_EQUAL("!=", "不等于"), GREATER(">", "大于"), LESS("<", "小于"), GREATER_EQUAL(">=", "大于等于"),
        LESS_EQUAL("<=", "小于等于"), BETWEEN("between", "BETWEEN"), LIKE("like", "LIKE");

        private String value;
        private String desc;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        private QUERY_TYPE(String value, String desc) {
            this.value = value;
            this.desc = desc;
        }
    }

}
