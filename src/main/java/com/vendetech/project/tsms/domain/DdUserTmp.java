package com.vendetech.project.tsms.domain;

import java.util.Date;

public class DdUserTmp {

	private String ddUserId;

	private String ddUserName;

	private String ddUserNum;

	private String ddUserCompany;

	private String ddUserMobile;

	private String ddUserOrgemail;

	private String ddDepartmentList;

	private String ddActiveStatus;

	private String ddUserPosition;

	private String ddUserAvatar;

	private Integer jobStatus;

	private Date createTime;

	public String getDdUserId() {
		return ddUserId;
	}

	public void setDdUserId(String ddUserId) {
		this.ddUserId = ddUserId;
	}

	public String getDdUserName() {
		return ddUserName;
	}

	public void setDdUserName(String ddUserName) {
		this.ddUserName = ddUserName == null ? null : ddUserName.trim();
	}

	public String getDdUserNum() {
		return ddUserNum;
	}

	public void setDdUserNum(String ddUserNum) {
		this.ddUserNum = ddUserNum == null ? null : ddUserNum.trim();
	}

	public String getDdUserCompany() {
		return ddUserCompany;
	}

	public void setDdUserCompany(String ddUserCompany) {
		this.ddUserCompany = ddUserCompany == null ? null : ddUserCompany.trim();
	}

	public String getDdUserMobile() {
		return ddUserMobile;
	}

	public void setDdUserMobile(String ddUserMobile) {
		this.ddUserMobile = ddUserMobile == null ? null : ddUserMobile.trim();
	}

	public String getDdUserOrgemail() {
		return ddUserOrgemail;
	}

	public void setDdUserOrgemail(String ddUserOrgemail) {
		this.ddUserOrgemail = ddUserOrgemail == null ? null : ddUserOrgemail.trim();
	}

	public String getDdDepartmentList() {
		return ddDepartmentList;
	}

	public void setDdDepartmentList(String ddDepartmentList) {
		this.ddDepartmentList = ddDepartmentList == null ? null : ddDepartmentList.trim();
	}

	public String getDdActiveStatus() {
		return ddActiveStatus;
	}

	public void setDdActiveStatus(String ddActiveStatus) {
		this.ddActiveStatus = ddActiveStatus == null ? null : ddActiveStatus.trim();
	}

	public String getDdUserPosition() {
		return ddUserPosition;
	}

	public void setDdUserPosition(String ddUserPosition) {
		this.ddUserPosition = ddUserPosition == null ? null : ddUserPosition.trim();
	}

	public String getDdUserAvatar() {
		return ddUserAvatar;
	}

	public void setDdUserAvatar(String ddUserAvatar) {
		this.ddUserAvatar = ddUserAvatar == null ? null : ddUserAvatar.trim();
	}

	public Integer getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(Integer jobStatus) {
		this.jobStatus = jobStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}