package com.vendetech.hr.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

public class SignRecordVO implements Serializable {
	private Long id;
	private Integer templateId;
	private Long employeeId;
	private String contractNo;
	@JsonFormat
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String notifyDate;
	@JsonFormat
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String notifyExpireDate;
	private String contractFilePath;
	@JsonFormat
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String signDate;
	@JsonFormat
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String expireDate;
	private Integer status;
	@JsonFormat
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String createTime;
	private String createByUserId;
	@JsonFormat
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String modifyTime;
	private String modifyByUserId;
	private Integer tplExpireDay;
	private String tplCode;
	private String mobile;

	private Long companyId;
	private String companyName;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getNotifyDate() {
		return notifyDate;
	}

	public void setNotifyDate(String notifyDate) {
		this.notifyDate = notifyDate;
	}

	public String getNotifyExpireDate() {
		return notifyExpireDate;
	}

	public void setNotifyExpireDate(String notifyExpireDate) {
		this.notifyExpireDate = notifyExpireDate;
	}

	public String getContractFilePath() {
		return contractFilePath;
	}

	public void setContractFilePath(String contractFilePath) {
		this.contractFilePath = contractFilePath;
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	public String getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreateByUserId() {
		return createByUserId;
	}

	public void setCreateByUserId(String createByUserId) {
		this.createByUserId = createByUserId;
	}

	public String getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getModifyByUserId() {
		return modifyByUserId;
	}

	public void setModifyByUserId(String modifyByUserId) {
		this.modifyByUserId = modifyByUserId;
	}

	public Integer getTplExpireDay() {
		return tplExpireDay;
	}

	public void setTplExpireDay(Integer tplExpireDay) {
		this.tplExpireDay = tplExpireDay;
	}

	public String getTplCode() {
		return tplCode;
	}

	public void setTplCode(String tplCode) {
		this.tplCode = tplCode;
	}

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
