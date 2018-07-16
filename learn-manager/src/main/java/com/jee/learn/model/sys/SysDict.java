package com.jee.learn.model.sys;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the sys_dict database table.
 * 
 */
@Entity
@Table(name = "sys_dict")
@NamedQuery(name = "SysDict.findAll", query = "SELECT s FROM SysDict s WHERE s.delFlag = '0' ")
public class SysDict implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private SysUser createBy;
    private Date createDate;
    private String delFlag;
    private String description;
    private String label;
    private SysDict parent;
    private String remarks;
    private BigDecimal sort;
    private String type;
    private SysUser updateBy;
    private Date updateDate;
    private String value;

    /* 非持久化字段 */
    private String parentId;

    public SysDict() {
    }

    public SysDict(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by")
    @JsonIgnore
    public SysUser getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(SysUser createBy) {
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    public SysDict getParent() {
        return this.parent;
    }

    public void setParent(SysDict parent) {
        this.parent = parent;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public BigDecimal getSort() {
        return this.sort;
    }

    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_by")
    @JsonIgnore
    public SysUser getUpdateBy() {
        return this.updateBy;
    }

    public void setUpdateBy(SysUser updateBy) {
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

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Transient
    public String getCreaterName() {
        return this.getCreateBy() == null ? null : this.getCreateBy().getName();
    }

    @Transient
    public String getUpdaterName() {
        return this.getUpdateBy() == null ? null : this.getUpdateBy().getName();
    }

    @Transient
    public String getParentId() {
        if (StringUtils.isNotBlank(this.parentId)) {
            return this.parentId;
        }
        if (parent != null && StringUtils.isNotBlank(parent.getId())) {
            return parent.getId();
        }
        return "0";
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

}