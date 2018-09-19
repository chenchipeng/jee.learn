package com.jee.learn.mybatis.entity;

import java.io.Serializable;
import java.util.Date;

public class ApiUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String createBy;
    private Date createDate;
    private String delFlag;
    private String isEnable;
    private String loginName;
    private String password;
    private String remarks;
    private String updateBy;
    private Date updateDate;

    public ApiUser() {
        super();
    }

    public ApiUser(String id, String createBy, Date createDate, String delFlag, String isEnable, String loginName,
            String password, String remarks, String updateBy, Date updateDate) {
        super();
        this.id = id;
        this.createBy = createBy;
        this.createDate = createDate;
        this.delFlag = delFlag;
        this.isEnable = isEnable;
        this.loginName = loginName;
        this.password = password;
        this.remarks = remarks;
        this.updateBy = updateBy;
        this.updateDate = updateDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}