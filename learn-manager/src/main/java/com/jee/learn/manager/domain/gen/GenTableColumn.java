package com.jee.learn.manager.domain.gen;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the gen_table_column database table.
 * 
 */
@Entity
@Table(name = "gen_table_column")
@NamedQuery(name = "GenTableColumn.findAll", query = "SELECT g FROM GenTableColumn g")
public class GenTableColumn implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String comments;
    private String createBy;
    private Date createDate;
    private String delFlag;
    private String dictType;
    private String genTableId;
    private String isEdit;
    private String isInc;
    private String isInsert;
    private String isList;
    private String isNull;
    private String isPk;
    private String isQuery;
    private String javaField;
    private String javaType;
    private String jdbcType;
    private String name;
    private String queryType;
    private String remarks;
    private String settings;
    private String showType;
    private BigDecimal sort;
    private String updateBy;
    private Date updateDate;

    public GenTableColumn() {
    }

    @Id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Column(name = "create_by")
    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    public Date getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "del_flag")
    public String getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Column(name = "dict_type")
    public String getDictType() {
        return this.dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    @Column(name = "gen_table_id")
    public String getGenTableId() {
        return this.genTableId;
    }

    public void setGenTableId(String genTableId) {
        this.genTableId = genTableId;
    }

    @Column(name = "is_edit")
    public String getIsEdit() {
        return this.isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    @Column(name = "is_inc")
    public String getIsInc() {
        return this.isInc;
    }

    public void setIsInc(String isInc) {
        this.isInc = isInc;
    }

    @Column(name = "is_insert")
    public String getIsInsert() {
        return this.isInsert;
    }

    public void setIsInsert(String isInsert) {
        this.isInsert = isInsert;
    }

    @Column(name = "is_list")
    public String getIsList() {
        return this.isList;
    }

    public void setIsList(String isList) {
        this.isList = isList;
    }

    @Column(name = "is_null")
    public String getIsNull() {
        return this.isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }

    @Column(name = "is_pk")
    public String getIsPk() {
        return this.isPk;
    }

    public void setIsPk(String isPk) {
        this.isPk = isPk;
    }

    @Column(name = "is_query")
    public String getIsQuery() {
        return this.isQuery;
    }

    public void setIsQuery(String isQuery) {
        this.isQuery = isQuery;
    }

    @Column(name = "java_field")
    public String getJavaField() {
        return this.javaField;
    }

    public void setJavaField(String javaField) {
        this.javaField = javaField;
    }

    @Column(name = "java_type")
    public String getJavaType() {
        return this.javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    @Column(name = "jdbc_type")
    public String getJdbcType() {
        return this.jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "query_type")
    public String getQueryType() {
        return this.queryType;
    }

    public void setQueryType(String queryType) {
        this.queryType = queryType;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getSettings() {
        return this.settings;
    }

    public void setSettings(String settings) {
        this.settings = settings;
    }

    @Column(name = "show_type")
    public String getShowType() {
        return this.showType;
    }

    public void setShowType(String showType) {
        this.showType = showType;
    }

    public BigDecimal getSort() {
        return this.sort;
    }

    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }

    @Column(name = "update_by")
    public String getUpdateBy() {
        return this.updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_date")
    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}