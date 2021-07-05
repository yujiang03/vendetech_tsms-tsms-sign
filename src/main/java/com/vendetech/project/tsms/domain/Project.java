package com.vendetech.project.tsms.domain;

import java.util.Date;

public class Project {
    /**项目ID*/
    private Long projectId;
    /**项目名称*/
    private String projectName;
    /**项目编码*/
    private String projectNum;
    /**项目经理*/
    private String projectManager;
    /**负责人*/
    private String projectLeader;
    /**部门ID*/
    private Long projectDepartmentId;
    /**公司ID*/
    private Long projectCompanyId;
    /**客户名称*/
    private String customerName;
    /**客户编码*/
    private String customerNum;
    /**项目状态*/
    private String status;
    
    /**合同状态*/
    private String contractStatus;

    private String projectcol;
    /**创建时间*/
    private Date createTime;
    /**修改时间*/
    private Date modifyTime;
    /**部门名称*/
    private String departmentName;
    /**公司名称*/
    private String companyName;

    private String userName;

    
    
    
    public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName == null ? null : projectName.trim();
    }

    public String getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(String projectNum) {
        this.projectNum = projectNum == null ? null : projectNum.trim();
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager == null ? null : projectManager.trim();
    }

    public String getProjectLeader() {
        return projectLeader;
    }

    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader == null ? null : projectLeader.trim();
    }

    public Long getProjectDepartmentId() {
        return projectDepartmentId;
    }

    public void setProjectDepartmentId(Long projectDepartmentId) {
        this.projectDepartmentId = projectDepartmentId;
    }

    public Long getProjectCompanyId() {
        return projectCompanyId;
    }

    public void setProjectCompanyId(Long projectCompanyId) {
        this.projectCompanyId = projectCompanyId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName == null ? null : customerName.trim();
    }

    public String getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(String customerNum) {
        this.customerNum = customerNum == null ? null : customerNum.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProjectcol() {
        return projectcol;
    }

    public void setProjectcol(String projectcol) {
        this.projectcol = projectcol == null ? null : projectcol.trim();
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