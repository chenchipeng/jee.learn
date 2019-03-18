package com.jee.learn.interfaces.gen.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

/**
 * 业务表Entity
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2019年3月17日 下午9:24:45 ccp 新建
 */
@Entity
@Table(name = "gen_table")
@NamedQuery(name = "GenTable.findAll", query = "SELECT a FROM GenTable a WHERE a.delFlag = 0 ")
@DynamicInsert
@DynamicUpdate
public class GenTable implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // 编号
    private String name; // 名称
    private String comments; // 描述
    private String className; // 实体类名称
    private String parentTable; // 关联父表
    private String parentTableFk; // 关联父表外键
    private String createBy; // 创建者
    private Date createDate; // 创建时间
    private String updateBy; // 更新者
    private Date updateDate; // 更新时间
    private String remarks; // 备注信息
    private Integer delFlag; // 删除标记（0：正常；1：删除）

    public GenTable() {
        super();
    }

    public GenTable(String name, String comments, String className) {
        super();
        this.name = name;
        this.comments = comments;
        this.className = className;
    }

    @Id
    @GenericGenerator(name = "genTableGenerator", strategy = "uuid")
    @GeneratedValue(generator = "genTableGenerator")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Column(name = "class_name")
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Column(name = "parent_table")
    public String getParentTable() {
        return parentTable;
    }

    public void setParentTable(String parentTable) {
        this.parentTable = parentTable;
    }

    @Column(name = "parent_table_fk")
    public String getParentTableFk() {
        return parentTableFk;
    }

    public void setParentTableFk(String parentTableFk) {
        this.parentTableFk = parentTableFk;
    }

    @Column(name = "create_by")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "update_by")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
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

    @Column(name = "del_flag")
    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

}