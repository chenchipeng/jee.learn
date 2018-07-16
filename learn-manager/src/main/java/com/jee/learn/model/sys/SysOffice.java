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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the sys_office database table.
 * 
 */
@Entity
@Table(name = "sys_office")
@NamedQuery(name = "SysOffice.findAll", query = "SELECT s FROM SysOffice s WHERE s.delFlag = '0' ")
public class SysOffice implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String address;
    private SysArea sysArea;
    private String code;
    private SysUser createBy;
    private Date createDate;
    private String delFlag;
    private String deputyPerson;
    private String email;
    private String fax;
    private String grade;
    private String master;
    private String name;
    private SysOffice parent;
    private String parentIds;
    private String phone;
    private String primaryPerson;
    private String remarks;
    private BigDecimal sort;
    private String type;
    private SysUser updateBy;
    private Date updateDate;
    private String useable;
    private String zipCode;

    /* 非持久化字段 */
    private String parentId;
    private List<SysRoleOffice> sysRoleOffices;

    public SysOffice() {
    }

    public SysOffice(String id) {
        this.id = id;
    }

    @Id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id")
    @JsonIgnore
    public SysArea getSysArea() {
        return this.sysArea;
    }

    public void setSysArea(SysArea sysArea) {
        this.sysArea = sysArea;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
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

    @Column(name = "deputy_person")
    public String getDeputyPerson() {
        return this.deputyPerson;
    }

    public void setDeputyPerson(String deputyPerson) {
        this.deputyPerson = deputyPerson;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getGrade() {
        return this.grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMaster() {
        return this.master;
    }

    public void setMaster(String master) {
        this.master = master;
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
    public SysOffice getParent() {
        return this.parent;
    }

    public void setParent(SysOffice parent) {
        this.parent = parent;
    }

    @Column(name = "parent_ids")
    public String getParentIds() {
        return this.parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name = "primary_person")
    public String getPrimaryPerson() {
        return this.primaryPerson;
    }

    public void setPrimaryPerson(String primaryPerson) {
        this.primaryPerson = primaryPerson;
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

    public String getUseable() {
        return this.useable;
    }

    public void setUseable(String useable) {
        this.useable = useable;
    }

    @Column(name = "zip_code")
    public String getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
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
    public String getSysAreaName() {
        return this.getSysArea() == null ? null : this.getSysArea().getName();
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

    // bi-directional many-to-one association to SysUserRole
    @OneToMany(mappedBy = "sysOffice", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<SysRoleOffice> getSysRoleOffices() {
        return this.sysRoleOffices;
    }

    public void setSysRoleOffices(List<SysRoleOffice> sysRoleOffices) {
        this.sysRoleOffices = sysRoleOffices;
    }

    @Transient
    public List<String> getSysRoleIds() {
        List<SysRoleOffice> list = getSysRoleOffices();
        if (list == null) {
            return null;
        }
        List<String> ids = new ArrayList<String>(list.size());
        for (SysRoleOffice sro : list) {
            ids.add(sro.getSysRoleId());
        }
        return ids;
    }

}