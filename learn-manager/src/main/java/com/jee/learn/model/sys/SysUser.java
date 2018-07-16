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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The persistent class for the sys_user database table.
 * 
 */
@Entity
@Table(name = "sys_user")
@NamedQuery(name = "SysUser.findAll", query = "SELECT s FROM SysUser s WHERE s.delFlag = '0' ")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private SysOffice company;
    private SysUser createBy;
    private Date createDate;
    private String delFlag;
    private String email;
    private Date loginDate;
    private String loginFlag;
    private String loginIp;
    private String loginName;
    private String mobile;
    private String name;
    private String no;
    private SysOffice office;
    private String password;
    private String phone;
    private String photo;
    private String remarks;
    private SysUser updateBy;
    private Date updateDate;
    private String userType;

    /* 非持久化字段 */
    private List<SysUserRole> sysUserRoles;

    private String oldLoginName;// 原登录名
    private String newPassword; // 新密码
    private String oldLoginIp; // 上次登陆IP
    private Date oldLoginDate; // 上次登陆日期

    public SysUser() {
    }

    public SysUser(String id) {
        this.id = id;
    }

    /** 判断用户是否为超级管理员 */
    @Transient
    @JsonIgnore
    public boolean isAdmin() {
        return isAdmin(this.getId());
    }

    /** 根据用户Id判断用户是否为超级管理员 */
    public static boolean isAdmin(String id) {
        return "1".equals(id);
    }

    @Id
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonIgnore
    public SysOffice getCompany() {
        return this.company;
    }

    public void setCompany(SysOffice company) {
        this.company = company;
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

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "login_date")
    public Date getLoginDate() {
        return this.loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    @Column(name = "login_flag")
    public String getLoginFlag() {
        return this.loginFlag;
    }

    public void setLoginFlag(String loginFlag) {
        this.loginFlag = loginFlag;
    }

    @Column(name = "login_ip")
    public String getLoginIp() {
        return this.loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @Column(name = "login_name")
    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return this.no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
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

    @Column(name = "user_type")
    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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
    public String getCompanyName() {
        return this.getCompany() == null ? null : this.getCompany().getName();
    }

    @Transient
    public String getOfficeName() {
        return this.getOffice() == null ? null : this.getOffice().getName();
    }

    // bi-directional many-to-one association to SysUserRole
    @OneToMany(mappedBy = "sysUser", fetch = FetchType.LAZY)
    @JsonIgnore
    public List<SysUserRole> getSysUserRoles() {
        return this.sysUserRoles;
    }

    public void setSysUserRoles(List<SysUserRole> sysUserRoles) {
        this.sysUserRoles = sysUserRoles;
    }

    @Transient
    public List<String> getSysRoleIds() {
        List<SysUserRole> list = getSysUserRoles();
        if (list == null) {
            return null;
        }
        List<String> ids = new ArrayList<String>(list.size());
        for (SysUserRole sur : list) {
            ids.add(sur.getSysRoleId());
        }
        return ids;
    }

    @JsonIgnore
    @Transient
    public String getOldLoginName() {
        return oldLoginName;
    }

    public void setOldLoginName(String oldLoginName) {
        this.oldLoginName = oldLoginName;
    }

    @JsonIgnore
    @Transient
    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @JsonIgnore
    @Transient
    public String getOldLoginIp() {
        return oldLoginIp;
    }

    public void setOldLoginIp(String oldLoginIp) {
        this.oldLoginIp = oldLoginIp;
    }

    @JsonIgnore
    @Transient
    public Date getOldLoginDate() {
        return oldLoginDate;
    }

    public void setOldLoginDate(Date oldLoginDate) {
        this.oldLoginDate = oldLoginDate;
    }

}