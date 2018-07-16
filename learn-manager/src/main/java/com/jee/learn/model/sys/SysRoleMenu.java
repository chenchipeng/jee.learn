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
@Table(name = "sys_role_menu")
@NamedQuery(name = "SysRoleMenu.findAll", query = "SELECT s FROM SysRoleMenu s")
public class SysRoleMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private SysRole sysRole;
    private SysMenu sysMenu;

    public SysRoleMenu() {
    }

    @Id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
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

    // bi-directional many-to-one association to sysUser
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    @JsonIgnore
    public SysMenu getSysMenu() {
        return this.sysMenu;
    }

    public void setSysMenu(SysMenu sysMenu) {
        this.sysMenu = sysMenu;
    }

    @Transient
    public String getSysRoleId() {
        return this.getSysRole() == null ? null : this.getSysRole().getId();
    }

    @Transient
    public String getSysMenuId() {
        return this.getSysMenu() == null ? null : this.getSysMenu().getId();
    }

}