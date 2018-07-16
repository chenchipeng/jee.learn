package com.jee.learn.model.sys;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the sys_user_role database table.
 * 
 */
@Entity
@Table(name = "sys_user_role")
@NamedQuery(name = "SysUserRole.findAll", query = "SELECT s FROM SysUserRole s")
public class SysUserRole implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private SysUser sysUser;
    private SysRole sysRole;

    public SysUserRole() {
    }

    @Id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // bi-directional many-to-one association to sysUser
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    public SysUser getSysUser() {
        return this.sysUser;
    }

    public void setSysUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    // bi-directional many-to-one association to SysRole
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    @JsonIgnore
    public SysRole getSysRole() {
        return this.sysRole;
    }

    public void setSysRole(SysRole sysRole) {
        this.sysRole = sysRole;
    }

    @Transient
    public String getSysUserId() {
        return this.getSysUser() == null ? null : this.getSysUser().getId();
    }

    @Transient
    public String getSysRoleId() {
        return this.getSysRole() == null ? null : this.getSysRole().getId();
    }

}