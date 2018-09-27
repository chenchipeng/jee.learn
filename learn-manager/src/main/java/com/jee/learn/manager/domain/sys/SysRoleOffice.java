package com.jee.learn.manager.domain.sys;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the sys_role_office database table.
 * 
 */
@Entity
@Table(name="sys_role_office")
@NamedQuery(name="SysRoleOffice.findAll", query="SELECT s FROM SysRoleOffice s")
public class SysRoleOffice implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String officeId;
	private String roleId;

	public SysRoleOffice() {
	}


	@Id
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	@Column(name="office_id")
	public String getOfficeId() {
		return this.officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}


	@Column(name="role_id")
	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

}