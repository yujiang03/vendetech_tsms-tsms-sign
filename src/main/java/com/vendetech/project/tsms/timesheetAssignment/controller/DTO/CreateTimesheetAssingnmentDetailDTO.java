package com.vendetech.project.tsms.timesheetAssignment.controller.DTO;


import java.util.Date;

public class CreateTimesheetAssingnmentDetailDTO {
    private Long tsAsgnDtlId;

    private Long tsAsgnId;

    private Long employeeId;

    private String employeeNum;

    private String employeeName;

    private Integer assignType;

    private String tsDate;

    private Integer detailStatus;

    private String attendanceResult;

    private Integer ifIssue;

    private Date createTime;

    private Date modifyTime;

    private Long  projectId;

	/**
	 * @return the tsAsgnDtlId
	 */
	public Long getTsAsgnDtlId() {
		return tsAsgnDtlId;
	}

	/**
	 * @param tsAsgnDtlId the tsAsgnDtlId to set
	 */
	public void setTsAsgnDtlId(Long tsAsgnDtlId) {
		this.tsAsgnDtlId = tsAsgnDtlId;
	}

	/**
	 * @return the tsAsgnId
	 */
	public Long getTsAsgnId() {
		return tsAsgnId;
	}

	/**
	 * @param tsAsgnId the tsAsgnId to set
	 */
	public void setTsAsgnId(Long tsAsgnId) {
		this.tsAsgnId = tsAsgnId;
	}

	/**
	 * @return the employeeId
	 */
	public Long getEmployeeId() {
		return employeeId;
	}

	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	/**
	 * @return the employeeNum
	 */
	public String getEmployeeNum() {
		return employeeNum;
	}

	/**
	 * @param employeeNum the employeeNum to set
	 */
	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum;
	}

	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}

	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	/**
	 * @return the assignType
	 */
	public Integer getAssignType() {
		return assignType;
	}

	/**
	 * @param assignType the assignType to set
	 */
	public void setAssignType(Integer assignType) {
		this.assignType = assignType;
	}

	/**
	 * @return the tsDate
	 */
	public String getTsDate() {
		return tsDate;
	}

	/**
	 * @param tsDate the tsDate to set
	 */
	public void setTsDate(String tsDate) {
		this.tsDate = tsDate;
	}

	/**
	 * @return the detailStatus
	 */
	public Integer getDetailStatus() {
		return detailStatus;
	}

	/**
	 * @param detailStatus the detailStatus to set
	 */
	public void setDetailStatus(Integer detailStatus) {
		this.detailStatus = detailStatus;
	}

	/**
	 * @return the attendanceResult
	 */
	public String getAttendanceResult() {
		return attendanceResult;
	}

	/**
	 * @param attendanceResult the attendanceResult to set
	 */
	public void setAttendanceResult(String attendanceResult) {
		this.attendanceResult = attendanceResult;
	}

	/**
	 * @return the ifIssue
	 */
	public Integer getIfIssue() {
		return ifIssue;
	}

	/**
	 * @param ifIssue the ifIssue to set
	 */
	public void setIfIssue(Integer ifIssue) {
		this.ifIssue = ifIssue;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime the modifyTime to set
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * @return the projectId
	 */
	public Long getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
    
    
}
