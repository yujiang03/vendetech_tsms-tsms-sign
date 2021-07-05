package com.vendetech.project.dingtalk.entity;

import java.io.Serializable;
import java.util.Date;

/*
* 
* gen by beetlsql 2018-03-16
*/
public class LoginLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 主键
	private Long id;
	// 登录状态
	private Integer status;
	// ip地址
	private String ipAddress;
	// 用户名
	private String loginName;
	private String function;
	// 用户ID
	private Long loginUserId;
	// 备注
	private String remark;
	// 登录时间
	private Date loginTime;

	public LoginLog() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public Long getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(Long loginUserId) {
		this.loginUserId = loginUserId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

}
