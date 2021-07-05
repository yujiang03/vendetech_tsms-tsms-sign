package com.vendetech.project.tsms.domain;

import java.util.Date;

public class DdDepartmentTmp {

	private Long ddDepartmentId;

	private String ddDepartmentFullname;

	private String ddDepartmentCode;

	private Long ddDepartmentParentId;

	private String ddDepartmentParentCode;

	private String ddDepartmentOwnerId;

	private Integer jobStatus;

	private Date createTime;

	public Long getDdDepartmentId() {
		return ddDepartmentId;
	}

	public void setDdDepartmentId(Long ddDepartmentId) {
		this.ddDepartmentId = ddDepartmentId;
	}

	public String getDdDepartmentFullname() {
		return ddDepartmentFullname;
	}

	public void setDdDepartmentFullname(String ddDepartmentFullname) {
		this.ddDepartmentFullname = ddDepartmentFullname == null ? null : ddDepartmentFullname.trim();
	}

	public String getDdDepartmentCode() {
		return ddDepartmentCode;
	}

	public void setDdDepartmentCode(String ddDepartmentCode) {
		this.ddDepartmentCode = ddDepartmentCode == null ? null : ddDepartmentCode.trim();
	}

	public Long getDdDepartmentParentId() {
		return ddDepartmentParentId;
	}

	public void setDdDepartmentParentId(Long ddDepartmentParentId) {
		this.ddDepartmentParentId = ddDepartmentParentId;
	}

	public String getDdDepartmentParentCode() {
		return ddDepartmentParentCode;
	}

	public void setDdDepartmentParentCode(String ddDepartmentParentCode) {
		this.ddDepartmentParentCode = ddDepartmentParentCode == null ? null : ddDepartmentParentCode.trim();
	}

	public String getDdDepartmentOwnerId() {
		return ddDepartmentOwnerId;
	}

	public void setDdDepartmentOwnerId(String ddDepartmentOwnerId) {
		this.ddDepartmentOwnerId = ddDepartmentOwnerId;
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