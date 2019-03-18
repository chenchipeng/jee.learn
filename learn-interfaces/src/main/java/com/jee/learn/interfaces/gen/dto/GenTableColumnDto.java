package com.jee.learn.interfaces.gen.dto;

import java.math.BigDecimal;

/**
 * GenTableColumnDto
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月7日 下午5:06:07 ccp 新建
 */
public class GenTableColumnDto {

    private String id; // 编号
    private String genTableId; // 归属表编号
    private String name; // 名称
    private String comments; // 描述
    private String jdbcType; // 列的数据类型的字节长度
    private String javaType; // JAVA类型
    private String javaField; // JAVA字段名
    private Integer isPk; // 是否主键
    private Integer isNull; // 是否可为空
    private Integer isInc;// 是否自增
    private Integer isUuid;// 是否使用uuid[0:否,1:是]
    private Integer isInsert; // 是否为插入字段
    private Integer isEdit; // 是否编辑字段
    private Integer isList; // 是否列表字段
    private Integer isQuery; // 是否查询字段
    private String queryType; // 查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）
    private String showType; // 字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）
    private String dictType; // 字典类型
    private String settings; // 其它设置（扩展字段JSON）
    private BigDecimal sort; // 排序（升序）
    private String classType; // JAVA属性类名

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGenTableId() {
        return genTableId;
    }

    public void setGenTableId(String genTableId) {
        this.genTableId = genTableId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJavaField() {
        return javaField;
    }

    public void setJavaField(String javaField) {
        this.javaField = javaField;
    }

    public Integer getIsPk() {
        return isPk;
    }

    public void setIsPk(Integer isPk) {
        this.isPk = isPk;
    }

    public Integer getIsNull() {
        return isNull;
    }

    public void setIsNull(Integer isNull) {
        this.isNull = isNull;
    }

    public Integer getIsInc() {
        return isInc;
    }

    public void setIsInc(Integer isInc) {
        this.isInc = isInc;
    }

    public Integer getIsUuid() {
        return isUuid;
    }

    public void setIsUuid(Integer isUuid) {
        this.isUuid = isUuid;
    }

    public Integer getIsInsert() {
        return isInsert;
    }

    public void setIsInsert(Integer isInsert) {
        this.isInsert = isInsert;
    }

    public Integer getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(Integer isEdit) {
        this.isEdit = isEdit;
    }

    public Integer getIsList() {
        return isList;
    }

    public void setIsList(Integer isList) {
        this.isList = isList;
    }

    public Integer getIsQuery() {
        return isQuery;
    }

    public void setIsQuery(Integer isQuery) {
        this.isQuery = isQuery;
    }

    public String getQueryType() {
        return queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getShowType() {
        return showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    public BigDecimal getSort() {
        return sort;
    }

    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

}
