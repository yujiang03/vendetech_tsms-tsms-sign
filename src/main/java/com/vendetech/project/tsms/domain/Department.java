package com.vendetech.project.tsms.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Department {
	/** 部门 ID */
	private Long departmentId;
	/** 部门编码 */
	private String departmentNum;
	/** 部门名称 */
	private String departmentName;

	private String ddDepartmentId;

	private String managerNum;

	private String parentDepartmentId;

	private String buNum;

	private String buName;

	private Integer status;

	private Date createTime;

	private Date modifyTime;

	/** 子菜单 */
	private List<Department> children = new ArrayList<Department>();
    
    
	/**
	 * @return the children
	 */
	public List<Department> getChildren() {
		return children;
	}

	/**
	 * @param children the children to set
	 */
	public void setChildren(List<Department> children) {
		this.children = children;
	}

	/**
	 * @return the ddDepartmentId
	 */
	public String getDdDepartmentId() {
		return ddDepartmentId;
	}

	/**
	 * @param ddDepartmentId the ddDepartmentId to set
	 */
	public void setDdDepartmentId(String ddDepartmentId) {
		this.ddDepartmentId = ddDepartmentId;
	}

	public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentNum() {
        return departmentNum;
    }

    public void setDepartmentNum(String departmentNum) {
        this.departmentNum = departmentNum == null ? null : departmentNum.trim();
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName == null ? null : departmentName.trim();
    }

    public String getManagerNum() {
        return managerNum;
    }

    public void setManagerNum(String managerNum) {
        this.managerNum = managerNum == null ? null : managerNum.trim();
    }

    public String getParentDepartmentId() {
        return parentDepartmentId;
    }

    public void setParentDepartmentId(String parentDepartmentId) {
        this.parentDepartmentId = parentDepartmentId == null ? null : parentDepartmentId.trim();
    }

    public String getBuNum() {
        return buNum;
    }

    public void setBuNum(String buNum) {
        this.buNum = buNum == null ? null : buNum.trim();
    }

    public String getBuName() {
        return buName;
    }

    public void setBuName(String buName) {
        this.buName = buName == null ? null : buName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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