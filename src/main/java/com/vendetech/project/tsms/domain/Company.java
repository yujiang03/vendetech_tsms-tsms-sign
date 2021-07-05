package com.vendetech.project.tsms.domain;

import com.vendetech.hr.vo.BaseVO;

import java.util.Date;

public class Company extends BaseVO {
    /**公司ID*/
    private Long companyId;
    /**公司名称*/
    private String companyName;

    private String companyAddress;

    private Integer companyType;

    private Integer status;

    private String creditNo;

    private String creditImagePath;

    private String bankName;

    private String bankId;

    private String subBranchName;

    private String legalName;

    private String legalId;

    private String legalMobile;

    private String legalIdFrontPath;

    private String legalBankCardNo;

    private String customerId;

    private Integer authStatus;

    private Date authDate;

    private String verifyUrl;

    private String transactionNo;

    private String serialNo;

    private String statusDesc;

    private Integer certStatus;

    private Long createUser;

    private Date createTime;

    private Long modifyUser;

    private Date modifyTime;

    private String authSignTransactionId;
    private String authSignContractNo;
    private String authSignViewPdf;
    private String authSignResultDesc;
    private Integer authSignStatus;

    private String companyTypeDesc;
    private String authStatusDesc;
    private String authSignStatusDesc;
    private String returnUrl;

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
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    public Integer getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Integer companyType) {
        this.companyType = companyType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo == null ? null : creditNo.trim();
    }

    public String getCreditImagePath() {
        return creditImagePath;
    }

    public void setCreditImagePath(String creditImagePath) {
        this.creditImagePath = creditImagePath == null ? null : creditImagePath.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId == null ? null : bankId.trim();
    }

    public String getSubBranchName() {
        return subBranchName;
    }

    public void setSubBranchName(String subBranchName) {
        this.subBranchName = subBranchName == null ? null : subBranchName.trim();
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName == null ? null : legalName.trim();
    }

    public String getLegalId() {
        return legalId;
    }

    public void setLegalId(String legalId) {
        this.legalId = legalId == null ? null : legalId.trim();
    }

    public String getLegalMobile() {
        return legalMobile;
    }

    public void setLegalMobile(String legalMobile) {
        this.legalMobile = legalMobile == null ? null : legalMobile.trim();
    }

    public String getLegalIdFrontPath() {
        return legalIdFrontPath;
    }

    public void setLegalIdFrontPath(String legalIdFrontPath) {
        this.legalIdFrontPath = legalIdFrontPath == null ? null : legalIdFrontPath.trim();
    }

    public String getLegalBankCardNo() {
        return legalBankCardNo;
    }

    public void setLegalBankCardNo(String legalBankCardNo) {
        this.legalBankCardNo = legalBankCardNo == null ? null : legalBankCardNo.trim();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId == null ? null : customerId.trim();
    }

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    public Date getAuthDate() {
        return authDate;
    }

    public void setAuthDate(Date authDate) {
        this.authDate = authDate;
    }

    public String getVerifyUrl() {
        return verifyUrl;
    }

    public void setVerifyUrl(String verifyUrl) {
        this.verifyUrl = verifyUrl == null ? null : verifyUrl.trim();
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo == null ? null : transactionNo.trim();
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo == null ? null : serialNo.trim();
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc == null ? null : statusDesc.trim();
    }

    public Integer getCertStatus() {
        return certStatus;
    }

    public void setCertStatus(Integer certStatus) {
        this.certStatus = certStatus;
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

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Long getModifyUser() {
        return modifyUser;
    }

    public void setModifyUser(Long modifyUser) {
        this.modifyUser = modifyUser;
    }

    public String getCompanyTypeDesc() {
        return companyTypeDesc;
    }

    public void setCompanyTypeDesc(String companyTypeDesc) {
        this.companyTypeDesc = companyTypeDesc;
    }

    public String getAuthStatusDesc() {
        return authStatusDesc;
    }

    public void setAuthStatusDesc(String authStatusDesc) {
        this.authStatusDesc = authStatusDesc;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getAuthSignTransactionId() {
        return authSignTransactionId;
    }

    public void setAuthSignTransactionId(String authSignTransactionId) {
        this.authSignTransactionId = authSignTransactionId;
    }

    public String getAuthSignContractNo() {
        return authSignContractNo;
    }

    public void setAuthSignContractNo(String authSignContractNo) {
        this.authSignContractNo = authSignContractNo;
    }

    public String getAuthSignViewPdf() {
        return authSignViewPdf;
    }

    public void setAuthSignViewPdf(String authSignViewPdf) {
        this.authSignViewPdf = authSignViewPdf;
    }

    public String getAuthSignResultDesc() {
        return authSignResultDesc;
    }

    public void setAuthSignResultDesc(String authSignResultDesc) {
        this.authSignResultDesc = authSignResultDesc;
    }

    public Integer getAuthSignStatus() {
        return authSignStatus;
    }

    public void setAuthSignStatus(Integer authSignStatus) {
        this.authSignStatus = authSignStatus;
    }

    public String getAuthSignStatusDesc() {
        return authSignStatusDesc;
    }

    public void setAuthSignStatusDesc(String authSignStatusDesc) {
        this.authSignStatusDesc = authSignStatusDesc;
    }
}