package com.jee.learn.manager.domain.sys;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the sys_log database table.
 * 
 */
@Entity
@Table(name = "sys_log")
@NamedQuery(name = "SysLog.findAll", query = "SELECT s FROM SysLog s")
public class SysLog implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String accountId;
	private String createBy;
	private Date createDate;
	private String exception;
	private String method;
	private String params;
	private String remoteAddr;
	private String requestUri;
	private String title;
	private String type;
	private String userAgent;

	public SysLog() {
	}

	@Id
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "account_id")
	public String getAccountId() {
		return this.accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	@Column(name = "create_by")
	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
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

	@Lob
	public String getException() {
		return this.exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Lob
	public String getParams() {
		return this.params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	@Column(name = "remote_addr")
	public String getRemoteAddr() {
		return this.remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	@Column(name = "request_uri")
	public String getRequestUri() {
		return this.requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "user_agent")
	public String getUserAgent() {
		return this.userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

}