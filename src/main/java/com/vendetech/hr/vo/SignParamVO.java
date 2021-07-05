package com.vendetech.hr.vo;

public class SignParamVO {
	private String companyCustomerId;
	private String employeeCustomerId;

	private Integer tplId;
	private Integer type;
	private String tplCode;
	private String returnUrl;
	private String contractNo;
	private String ciNumber;

	public String getCompanyCustomerId() {
		return companyCustomerId;
	}

	public void setCompanyCustomerId(String companyCustomerId) {
		this.companyCustomerId = companyCustomerId;
	}

	public String getEmployeeCustomerId() {
		return employeeCustomerId;
	}

	public void setEmployeeCustomerId(String employeeCustomerId) {
		this.employeeCustomerId = employeeCustomerId;
	}

	public Integer getTplId() {
		return tplId;
	}

	public void setTplId(Integer tplId) {
		this.tplId = tplId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getTplCode() {
		return tplCode;
	}

	public void setTplCode(String tplCode) {
		this.tplCode = tplCode;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getCiNumber() {
		return ciNumber;
	}

	public void setCiNumber(String ciNumber) {
		this.ciNumber = ciNumber;
	}
}
