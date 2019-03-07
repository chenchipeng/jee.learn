package com.jee.learn.interfaces.gen.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**
 * 业务表字段Entity
 * 
 * @author ccp
 * @version 2019-03-07
 */
@Entity
@Table(name = "gen_table_column")
@NamedQuery(name = "GenTableColumn.findAll", query = "SELECT a FROM GenTableColumn a WHERE a.delFlag = 0 ")
@DynamicInsert
@DynamicUpdate
public class GenTableColumn implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
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
    private Integer isInsert; // 是否为插入字段
    private Integer isEdit; // 是否编辑字段
    private Integer isList; // 是否列表字段
    private Integer isQuery; // 是否查询字段
    private String queryType; // 查询方式（等于、不等于、大于、小于、范围、左LIKE、右LIKE、左右LIKE）
    private String showType; // 字段生成方案（文本框、文本域、下拉框、复选框、单选框、字典选择、人员选择、部门选择、区域选择）
    private String dictType; // 字典类型
    private String settings; // 其它设置（扩展字段JSON）
    private BigDecimal sort; // 排序（升序）
    private String createBy; // 创建者
    private Date createDate; // 创建时间
    private String updateBy; // 更新者
    private Date updateDate; // 更新时间
    private String remarks; // 备注信息
    private Integer delFlag; // 删除标记（0：正常；1：删除）

    public GenTableColumn() {
        super();
    }

    @Id
    @GenericGenerator(name = "genTableColumnGenerator", strategy = "uuid")
    @GeneratedValue(generator = "genTableColumnGenerator")
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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

}