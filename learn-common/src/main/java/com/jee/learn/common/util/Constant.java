package com.jee.learn.common.util;

/**
 * 常量存放点
 * 
 * @author 1002360
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2017年8月2日 下午6:33:47 1002360 新建
 */
public interface Constant {

    /** 01是否-否 */
    String NO_0 = "0";
    /** 01是否-是 */
    String YES_1 = "1";

    /** true */
    String TRUE = "true";
    /** false */
    String FALSE = "false";

    /** 默认分页大小 */
    int DEFAULT_PAGE_SIZE = 10;
    /** 列表页面分页大小选择 */
    int[] DEFAULT_PAGE_LIST = { DEFAULT_PAGE_SIZE, 30, 50, 100 };

    /* 通用数据表字段属性 */
    String ID_NAME = "id";// 主键id属性名称
    String PARENT = "parent";// 父节点属性名称
    String PARENT_ID = "parentId";// 父节点属性名称
    String PARENT_IDS = "parentIds";// 父节点属性名称
    String DEL_FLEG_NAME = "delFlag";// 删除标记属性名称
    String UPDATE_DATE_NAME = "updateDate";// 更新时间属性名称
    String CREATE_DATE_NAME = "createDate";// 创建时间属性名称
    String PRE_PERSIST_MOTHED = "prePersist";// 新增前调用方法名称
    String PRE_UPDATE_MOTHED = "preUpdate";// 更新前调用方法名称

    /* 排序方式 */
    String SORT_DESC = "DESC";// 降序
    String SORT_ASC = "ASC";// 升序

}
