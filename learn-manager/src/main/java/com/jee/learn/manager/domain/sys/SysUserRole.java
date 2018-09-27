package com.jee.learn.manager.domain.sys;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sys_user_role database table.
 * 
 */
@Entity
@Table(name="sys_user_role")
@NamedQuery(name="SysUserRole.findAll", query="SELECT s FROM SysUserRole s")
public class SysUserRole implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String roleId;
	private String userId;

	public SysUserRole() {
	}


	@Id
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Column(name="role_id")
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}


	@Column(name="user_id")
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}