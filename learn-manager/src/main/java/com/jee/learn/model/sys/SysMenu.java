package com.jee.learn.model.sys;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the sys_menu database table.
 * 
 */
@Entity
@Table(name = "sys_menu")
@NamedQuery(name = "SysMenu.findAll", query = "SELECT s FROM SysMenu s WHERE s.delFlag = '0' ")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private SysUser createBy;
    private Date createDate;
    private String delFlag;
    private String href;
    private String icon;
    private String isShow;
    private String name;
    private SysMenu parent;
    private String parentIds;
    private String permission;
    private String remarks;
    private BigDecimal sort;
    private String target;
    private SysUser updateBy;
    private Date updateDate;

    /* 非持久化字段 */
    private String parentId;
    private List<SysMenu> children; // 子菜单

    public SysMenu() {
    }

    public SysMenu(String id) {
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

    public String getHref() {
        return this.href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Column(name = "is_show")
    public String getIsShow() {
        return this.isShow;
    }

    public void setIsShow(String isShow) {
        this.isShow = isShow;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    public SysMenu getParent() {
        return this.parent;
    }

    public void setParent(SysMenu parent) {
        this.parent = parent;
    }

    @Column(name = "parent_ids")
    public String getParentIds() {
        return this.parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
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

    public String getTarget() {
        return this.target;
    }

    public void setTarget(String target) {
        this.target = target;
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

    @JsonIgnore
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    public List<SysMenu> getChildren() {
        if(children==null) {
            children=new ArrayList<>(0);
        }
        return children;
    }

    public void setChildren(List<SysMenu> children) {
        this.children = children;
    }

}