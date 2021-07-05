package com.vendetech.project.tsms.domain;

import java.util.Date;
import java.util.List;

public class TimesheetAssignment {
    private Long tsAsgnId;

    private Long tsProjectId;

    private Long tsDeparmentId;

    private Date tsStartDate;

    private Date tsEndDate;

    private Integer assignType;

    private Long tsCreatorUserId;

    private Integer status;

    private String tsCreatedBy;

    private Date createTime;

    private Date modifyTime;

    /**区间查询 合同开始开始时间*/
    private Date beginbeginTime;
    /**区间查询 合同开始结束时间*/
    private Date beginendTime;
    /**区间查询 合同结束开始时间*/
    private Date endbeginTime;
    /**区间查询 合同结束结束时间*/
    private Date endendTime;

    private String projectName;

    private String assignmentName;

    private String projectNum;

    private String departmentName;

    private Long projectId;

    private String  projectManager;

    private Long departmentId;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(String projectNum) {
        this.projectNum = projectNum;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    private List<TimesheetAssignmentDetail> timesheetAssignmentDetails;

    public List<TimesheetAssignmentDetail> getTimesheetAssignmentDetails() {
        return timesheetAssignmentDetails;
    }

    public void setTimesheetAssignmentDetails(List<TimesheetAssignmentDetail> timesheetAssignmentDetails) {
        this.timesheetAssignmentDetails = timesheetAssignmentDetails;
    }

    public Date getBeginbeginTime() {
        return beginbeginTime;
    }

    public void setBeginbeginTime(Date beginbeginTime) {
        this.beginbeginTime = beginbeginTime;
    }

    public Date getBeginendTime() {
        return beginendTime;
    }

    public void setBeginendTime(Date beginendTime) {
        this.beginendTime = beginendTime;
    }

    public Date getEndbeginTime() {
        return endbeginTime;
    }

    public void setEndbeginTime(Date endbeginTime) {
        this.endbeginTime = endbeginTime;
    }

    public Date getEndendTime() {
        return endendTime;
    }

    public void setEndendTime(Date endendTime) {
        this.endendTime = endendTime;
    }

    public Long getTsAsgnId() {
        return tsAsgnId;
    }

    public void setTsAsgnId(Long tsAsgnId) {
        this.tsAsgnId = tsAsgnId;
    }

    public Long getTsProjectId() {
        return tsProjectId;
    }

    public void setTsProjectId(Long tsProjectId) {
        this.tsProjectId = tsProjectId;
    }

    public Long getTsDeparmentId() {
        return tsDeparmentId;
    }

    public void setTsDeparmentId(Long tsDeparmentId) {
        this.tsDeparmentId = tsDeparmentId;
    }

    public Date getTsStartDate() {
        return tsStartDate;
    }

    public void setTsStartDate(Date tsStartDate) {
        this.tsStartDate = tsStartDate;
    }

    public Date getTsEndDate() {
        return tsEndDate;
    }

    public void setTsEndDate(Date tsEndDate) {
        this.tsEndDate = tsEndDate;
    }

    public Integer getAssignType() {
        return assignType;
    }

    public void setAssignType(Integer assignType) {
        this.assignType = assignType;
    }

    public Long getTsCreatorUserId() {
        return tsCreatorUserId;
    }

    public void setTsCreatorUserId(Long tsCreatorUserId) {
        this.tsCreatorUserId = tsCreatorUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTsCreatedBy() {
        return tsCreatedBy;
    }

    public void setTsCreatedBy(String tsCreatedBy) {
        this.tsCreatedBy = tsCreatedBy == null ? null : tsCreatedBy.trim();
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