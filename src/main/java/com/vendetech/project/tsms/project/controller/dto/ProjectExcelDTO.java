package com.vendetech.project.tsms.project.controller.dto;

import java.util.Date;

public class ProjectExcelDTO {
    /**项目编码*/
    private String projectNum;
    /**项目名称*/
    private String projectName;
    /**项目经理*/
    private String projectManager;
    /**负责人*/
    private String projectLeader;
    /**项目状态*/
    private String status;
    
    /**合同状态*/
    private String contractStatus;
    /**项目所属部门*/
    private String departmentNum;
    /**项目所属公司*/
    private Long projectCompanyId;
    /**客户名称*/
    private String customerName;
    /**客户编号*/
    private String customerNum;
    /**项目经理工号*/
    private String employeeNum;

    private String projectcol;
    /**创建时间*/
    private Date createTime;
    /**修改时间*/
    private Date modifyTime;
    /**部门名称*/
    private String departmentName;
    /**公司名称*/
    private String companyName;

    private Long projectDepartmentId;

    /**项目经理备注信息*/
    private String projectManagerRemark;
    /**部门备注信息备注*/
    private String departmentNameRemark;
    /**状态备注信息*/
    private String statusRemark;
    
    /**合同状态备注信息*/
    private String contractStatusRemark;
    /**项目编码备注信息*/
    private String projectNumRemark;
    /**公司备注信息*/
    private String companyNameRemark;
    /**备注*/
    private String remark;

    
    
    public String getContractStatusRemark() {
		return contractStatusRemark;
	}

	public void setContractStatusRemark(String contractStatusRemark) {
		this.contractStatusRemark = contractStatusRemark;
	}

	public String getContractStatus() {
		return contractStatus;
	}

	public void setContractStatus(String contractStatus) {
		this.contractStatus = contractStatus;
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCompanyNameRemark() {
        return companyNameRemark;
    }

    public void setCompanyNameRemark(String companyNameRemark) {
        this.companyNameRemark = companyNameRemark;
    }

    public String getProjectNumRemark() {
        return projectNumRemark;
    }

    public void setProjectNumRemark(String projectNumRemark) {
        this.projectNumRemark = projectNumRemark;
    }

    public String getStatusRemark() {
        return statusRemark;
    }

    public void setStatusRemark(String statusRemark) {
        this.statusRemark = statusRemark;
    }

    public String getProjectManagerRemark() {
        return projectManagerRemark;
    }

    public void setProjectManagerRemark(String projectManagerRemark) {
        this.projectManagerRemark = projectManagerRemark;
    }

    public String getDepartmentNameRemark() {
        return departmentNameRemark;
    }

    public void setDepartmentNameRemark(String departmentNameRemark) {
        this.departmentNameRemark = departmentNameRemark;
    }

    public String getDepartmentNum() {
        return departmentNum;
    }

    public void setDepartmentNum(String departmentNum) {
        this.departmentNum = departmentNum;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getEmployeeNum() {
        return employeeNum;
    }

    public void setEmployeeNum(String employeeNum) {
        this.employeeNum = employeeNum;
    }

    public String getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(String projectNum) {
        this.projectNum = projectNum;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getProjectLeader() {
        return projectLeader;
    }

    public void setProjectLeader(String projectLeader) {
        this.projectLeader = projectLeader;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNum() {
        return customerNum;
    }

    public void setCustomerNum(String customerNum) {
        this.customerNum = customerNum;
    }

    public String getProjectcol() {
        return projectcol;
    }

    public void setProjectcol(String projectcol) {
        this.projectcol = projectcol;
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

    public Long getProjectCompanyId() {
        return projectCompanyId;
    }

    public void setProjectCompanyId(Long projectCompanyId) {
        this.projectCompanyId = projectCompanyId;
    }

    public Long getProjectDepartmentId() {
        return projectDepartmentId;
    }

    public void setProjectDepartmentId(Long projectDepartmentId) {
        this.projectDepartmentId = projectDepartmentId;
    }
}
