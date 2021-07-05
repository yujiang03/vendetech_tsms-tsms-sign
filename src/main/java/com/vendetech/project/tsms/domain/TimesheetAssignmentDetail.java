package com.vendetech.project.tsms.domain;

import java.util.Date;

public class TimesheetAssignmentDetail {
    private Long tsAsgnDtlId;

    private Long tsAsgnId;

    private Long employeeId;

    private String employeeNum;

    private String employeeName;

    private Integer assignType;

    private Date tsDate;

    private Integer detailStatus;

    private String attendanceResult;

    private Integer ifIssue;

    private Date createTime;

    private Date modifyTime;

    private Long projectId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getTsAsgnDtlId() {
        return tsAsgnDtlId;
    }

    public void setTsAsgnDtlId(Long tsAsgnDtlId) {
        this.tsAsgnDtlId = tsAsgnDtlId;
    }

    public Long getTsAsgnId() {
        return tsAsgnId;
    }

    public void setTsAsgnId(Long tsAsgnId) {
        this.tsAsgnId = tsAsgnId;
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
        this.employeeName = employeeName == null ? null : employeeName.trim();
    }

    public Integer getAssignType() {
        return assignType;
    }

    public void setAssignType(Integer assignType) {
        this.assignType = assignType;
    }

    public Date getTsDate() {
        return tsDate;
    }

    public void setTsDate(Date tsDate) {
        this.tsDate = tsDate;
    }

    public Integer getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(Integer detailStatus) {
        this.detailStatus = detailStatus;
    }

    public String getAttendanceResult() {
        return attendanceResult;
    }

    public void setAttendanceResult(String attendanceResult) {
        this.attendanceResult = attendanceResult == null ? null : attendanceResult.trim();
    }

    public Integer getIfIssue() {
        return ifIssue;
    }

    public void setIfIssue(Integer ifIssue) {
        this.ifIssue = ifIssue;
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
