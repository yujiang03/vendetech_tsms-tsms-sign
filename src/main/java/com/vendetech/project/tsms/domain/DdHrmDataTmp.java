package com.vendetech.project.tsms.domain;

import java.util.Date;

public class DdHrmDataTmp {

	private String ddUserId;

	private String ddName;

	private String ddEmpNum;

	private String ddMainDeptId;

	private String ddMainDeptNum;

	private Date ddJoinDate;

	private String ddCompanyName;

	private Integer ddTsExclFlag;

	private Integer jobStatus;

	private Date createTime;

	public String getDdUserId() {
		return ddUserId;
	}

	public void setDdUserId(String ddUserId) {
		this.ddUserId = ddUserId;
	}

	public String getDdName() {
		return ddName;
	}

	public void setDdName(String ddName) {
		this.ddName = ddName;
	}

	public String getDdEmpNum() {
		return ddEmpNum;
	}

	public void setDdEmpNum(String ddEmpNum) {
		this.ddEmpNum = ddEmpNum;
	}

	public String getDdMainDeptId() {
		return ddMainDeptId;
	}

	public void setDdMainDeptId(String ddMainDeptId) {
		this.ddMainDeptId = ddMainDeptId;
	}

	public String getDdMainDeptNum() {
		return ddMainDeptNum;
	}

	public void setDdMainDeptNum(String ddMainDeptNum) {
		this.ddMainDeptNum = ddMainDeptNum;
	}

	public Date getDdJoinDate() {
		return ddJoinDate;
	}

	public void setDdJoinDate(Date ddJoinDate) {
		this.ddJoinDate = ddJoinDate;
	}

	public String getDdCompanyName() {
		return ddCompanyName;
	}

	public void setDdCompanyName(String ddCompanyName) {
		this.ddCompanyName = ddCompanyName;
	}

	public Integer getDdTsExclFlag() {
		return ddTsExclFlag;
	}

	public void setDdTsExclFlag(Integer ddTsExclFlag) {
		this.ddTsExclFlag = ddTsExclFlag;
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