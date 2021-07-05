package com.vendetech.project.tsms.domain;

import java.util.Date;

public class EmpTsAttdAsgnRecord {

	private Long id;

	private Long employeeId;

	private String employeeNum;

	private String employeeName;

	private Date tsDate;

	private Integer assignType;

	private Long assignProjectId;

	private Integer needAssign;

	private Integer assignedStatus;

	private String unmatchResult;

	private String assigner;

	private Integer ifDuty;

	private Integer ifOvertime;

	private Integer overtimeHours;

	private String attendanceResult;

	private Integer attendanceDayHours;

	private Integer ifIssue;

	private Integer source;

	private String sourceBy;

	private Date createTime;

	private Date modifyTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeNum() {
		return employeeNum;
	}

	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum == null ? null : employeeNum.trim();
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Date getTsDate() {
		return tsDate;
	}

	public void setTsDate(Date tsDate) {
		this.tsDate = tsDate;
	}

	public Integer getAssignType() {
		return assignType;
	}

	public void setAssignType(Integer assignType) {
		this.assignType = assignType;
	}

	public Long getAssignProjectId() {
		return assignProjectId;
	}

	public void setAssignProjectId(Long assignProjectId) {
		this.assignProjectId = assignProjectId;
	}

	public Integer getNeedAssign() {
		return needAssign;
	}

	public void setNeedAssign(Integer needAssign) {
		this.needAssign = needAssign;
	}

	public Integer getAssignedStatus() {
		return assignedStatus;
	}

	public void setAssignedStatus(Integer assignedStatus) {
		this.assignedStatus = assignedStatus;
	}

	public String getUnmatchResult() {
		return unmatchResult;
	}

	public void setUnmatchResult(String unmatchResult) {
		this.unmatchResult = unmatchResult;
	}

	public String getAssigner() {
		return assigner;
	}

	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}

	public Integer getIfDuty() {
		return ifDuty;
	}

	public void setIfDuty(Integer ifDuty) {
		this.ifDuty = ifDuty;
	}

	public Integer getIfOvertime() {
		return ifOvertime;
	}

	public void setIfOvertime(Integer ifOvertime) {
		this.ifOvertime = ifOvertime;
	}

	public Integer getOvertimeHours() {
		return overtimeHours;
	}

	public void setOvertimeHours(Integer overtimeHours) {
		this.overtimeHours = overtimeHours;
	}

	public String getAttendanceResult() {
		return attendanceResult;
	}

	public void setAttendanceResult(String attendanceResult) {
		this.attendanceResult = attendanceResult == null ? null : attendanceResult.trim();
	}

	public Integer getAttendanceDayHours() {
		return attendanceDayHours;
	}

	public void setAttendanceDayHours(Integer attendanceDayHours) {
		this.attendanceDayHours = attendanceDayHours;
	}

	public Integer getIfIssue() {
		return ifIssue;
	}

	public void setIfIssue(Integer ifIssue) {
		this.ifIssue = ifIssue;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public String getSourceBy() {
		return sourceBy;
	}

	public void setSourceBy(String sourceBy) {
		this.sourceBy = sourceBy == null ? null : sourceBy.trim();
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}