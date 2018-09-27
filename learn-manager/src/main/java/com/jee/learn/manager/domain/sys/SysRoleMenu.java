package com.jee.learn.manager.domain.sys;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sys_role_menu database table.
 * 
 */
@Entity
@Table(name="sys_role_menu")
@NamedQuery(name="SysRoleMenu.findAll", query="SELECT s FROM SysRoleMenu s")
public class SysRoleMenu implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String menuId;
	private String roleId;

	public SysRoleMenu() {
	}


	@Id
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Column(name="menu_id")
	public String getMenuId() {
		return this.menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}


	@Column(name="role_id")
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}