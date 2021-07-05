package com.vendetech.hr.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;

public class SignEmployeeRelVO implements Serializable {
	private Long id;
	private Integer templateId;
	private String tplName;
	private String tplCode;
	private Long employeeId;
	private String employeeName;
	@JsonFormat
	private String createTime;
	private Long createByUserId;
	@JsonFormat
	private String modifyTime;
	private Long modifyByUserId;

	private String companyName;
	private String companyId;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getTplName() {
		return tplName;
	}

	public void setTplName(String tplName) {
		this.tplName = tplName;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Long getCreateByUserId() {
		return createByUserId;
	}

	public void setCreateByUserId(Long createByUserId) {
		this.createByUserId = createByUserId;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getModifyByUserId() {
		return modifyByUserId;
	}

	public void setModifyByUserId(Long modifyByUserId) {
		this.modifyByUserId = modifyByUserId;
	}

	public String getTplCode() {
		return tplCode;
	}

	public void setTplCode(String tplCode) {
		this.tplCode = tplCode;
	}
}
