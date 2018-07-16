package com.jee.learn.model.sys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the sys_role database table.
 * 
 */
@Entity
@Table(name = "sys_role")
@NamedQuery(name = "SysRole.findAll", query = "SELECT s FROM SysRole s WHERE s.delFlag = '0' ")
public class SysRole implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private SysUser createBy;
    private Date createDate;
    private String dataScope;
    private String delFlag;
    private String enname;
    private String isSys;
    private String name;
    private SysOffice office;
    private String remarks;
    private String roleType;
    private SysUser updateBy;
    private Date updateDate;
    private String useable;

    /* 非持久化字段 */
    private List<SysUserRole> sysUserRoles;

    public SysRole() {
    }

    public SysRole(String id) {
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

    @Column(name = "data_scope")
    public String getDataScope() {
        return this.dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    @Column(name = "del_flag")
    public String getDelFlag() {
        return this.delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getEnname() {
        return this.enname;
    }

    public void setEnname(String enname) {
        this.enname = enname;
    }

    @Column(name = "is_sys")
    public String getIsSys() {
        return this.isSys;
    }

    public void setIsSys(String isSys) {
        this.isSys = isSys;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_id")
    @JsonIgnore
    public SysOffice getOffice() {
        return this.office;
    }

    public void setOffice(SysOffice office) {
        this.office = office;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Column(name = "role_type")
    public String getRoleType() {
        return this.roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
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

    // bi-directional many-to-one association to SysUserRole
    @OneToMany(mappedBy = "sysRole")
    @JsonIgnore
    public List<SysUserRole> getSysUserRoles() {
        return this.sysUserRoles;
    }

    public void setSysUserRoles(List<SysUserRole> sysUserRoles) {
        this.sysUserRoles = sysUserRoles;
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
    public String getSysOfficeName() {
        return this.getOffice() == null ? null : this.getOffice().getName();
    }

    @Transient
    public List<String> getSysUserIds() {
        List<SysUserRole> list = getSysUserRoles();
        if (list == null) {
            return null;
        }
        List<String> ids = new ArrayList<String>(list.size());
        for (SysUserRole sur : list) {
            ids.add(sur.getSysUserId());
        }
        return ids;
    }

}