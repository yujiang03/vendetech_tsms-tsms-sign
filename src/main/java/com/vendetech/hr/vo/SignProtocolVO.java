package com.vendetech.hr.vo;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

public class SignProtocolVO extends BaseVO implements Serializable {
	private Long id;
	private Long employeeId;
	private String employeeName;
	private String employeeNum;
	private String mobile;
	private String cardNum;
	private String templateName;
	private String templateFilePath;
	private Integer authStatus;	//是否通过认证,这是用法大大接口认证的 Y:通过  N:待验证
	private String authStatusDesc;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String authDate;
	private String contractFilePath;
	private Integer signStatus;
	private String signStatusDesc;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String signDate;
	private String contractNo;
	private String tplCode;
	private Integer tplExpireYear;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private String notifyDate;

	private Long companyId;
	private String companyName;
	private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

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

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeNum() {
		return employeeNum;
	}

	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

    public String getTemplateFilePath() {
        return templateFilePath;
    }

    public void setTemplateFilePath(String templateFilePath) {
        this.templateFilePath = templateFilePath;
    }

    public Integer getAuthStatus() {
		return authStatus;
	}

	public void setAuthStatus(Integer authStatus) {
		this.authStatus = authStatus;
	}

	public String getAuthStatusDesc() {
		return authStatusDesc;
	}

	public void setAuthStatusDesc(String authStatusDesc) {
		this.authStatusDesc = authStatusDesc;
	}

	public String getAuthDate() {
		return authDate;
	}

	public void setAuthDate(String authDate) {
		this.authDate = authDate;
	}

	public String getContractFilePath() {
		return contractFilePath;
	}

	public void setContractFilePath(String contractFilePath) {
		this.contractFilePath = contractFilePath;
	}

	public Integer getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(Integer signStatus) {
		this.signStatus = signStatus;
	}

	public String getSignStatusDesc() {
		return signStatusDesc;
	}

	public void setSignStatusDesc(String signStatusDesc) {
		this.signStatusDesc = signStatusDesc;
	}

	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getTplCode() {
		return tplCode;
	}

	public void setTplCode(String tplCode) {
		this.tplCode = tplCode;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

    public Integer getTplExpireYear() {
        return tplExpireYear;
    }

    public void setTplExpireYear(Integer tplExpireYear) {
        this.tplExpireYear = tplExpireYear;
    }

    public String getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(String notifyDate) {
        this.notifyDate = notifyDate;
    }
}