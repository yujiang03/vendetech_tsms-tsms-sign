package com.vendetech.project.tsms.domain;

import java.util.Date;

public class DdHrmDataSign {
    private String ddUserId;
    private String ddJobNumber;
    private String ddName;
    private String ddPosition;
    private Integer ddSexType;
    private String ddBirthTime;
    private String ddCertAddress;
    private String ddAddress;
    private String ddCertNo;
    private String ddMobile;
    private String ddWorkCity;
    private String ddNowContractStartTime;
    private String ddNowContractEndTime;
    private String ddProbationPeriodType;
    private String ddRegularTime;
    private String ddConfirmJoinTime;
    private String ddGraduateSchool;
    private String ddEmail;
    private String ddBankAccountNo;
    private String ddAccountBank;
    private Long ddMainDeptId;
    private String ddMainDept;
    private String ddCompanyName;
    private Integer ddTsExclFlag;
    private Integer jobStatus;
    private Date createTime;

    public String getDdPosition() {
        return ddPosition;
    }

    public void setDdPosition(String ddPosition) {
        this.ddPosition = ddPosition;
    }

    public String getDdUserId() {
        return ddUserId;
    }

    public void setDdUserId(String ddUserId) {
        this.ddUserId = ddUserId == null ? null : ddUserId.trim();
    }

    public String getDdJobNumber() {
        return ddJobNumber;
    }

    public void setDdJobNumber(String ddJobNumber) {
        this.ddJobNumber = ddJobNumber == null ? null : ddJobNumber.trim();
    }

    public String getDdName() {
        return ddName;
    }

    public void setDdName(String ddName) {
        this.ddName = ddName == null ? null : ddName.trim();
    }

    public Integer getDdSexType() {
        return ddSexType;
    }

    public void setDdSexType(Integer ddSexType) {
        this.ddSexType = ddSexType;
    }

    public String getDdBirthTime() {
        return ddBirthTime;
    }

    public void setDdBirthTime(String ddBirthTime) {
        this.ddBirthTime = ddBirthTime == null ? null : ddBirthTime.trim();
    }

    public String getDdCertAddress() {
        return ddCertAddress;
    }

    public void setDdCertAddress(String ddCertAddress) {
        this.ddCertAddress = ddCertAddress == null ? null : ddCertAddress.trim();
    }

    public String getDdAddress() {
        return ddAddress;
    }

    public void setDdAddress(String ddAddress) {
        this.ddAddress = ddAddress == null ? null : ddAddress.trim();
    }

    public String getDdCertNo() {
        return ddCertNo;
    }

    public void setDdCertNo(String ddCertNo) {
        this.ddCertNo = ddCertNo;
    }

    public String getDdMobile() {
        return ddMobile;
    }

    public void setDdMobile(String ddMobile) {
        this.ddMobile = ddMobile == null ? null : ddMobile.trim();
    }

    public String getDdWorkCity() {
        return ddWorkCity;
    }

    public void setDdWorkCity(String ddWorkCity) {
        this.ddWorkCity = ddWorkCity == null ? null : ddWorkCity.trim();
    }

    public String getDdNowContractStartTime() {
        return ddNowContractStartTime;
    }

    public void setDdNowContractStartTime(String ddNowContractStartTime) {
        this.ddNowContractStartTime = ddNowContractStartTime == null ? null : ddNowContractStartTime.trim();
    }

    public String getDdNowContractEndTime() {
        return ddNowContractEndTime;
    }

    public void setDdNowContractEndTime(String ddNowContractEndTime) {
        this.ddNowContractEndTime = ddNowContractEndTime == null ? null : ddNowContractEndTime.trim();
    }

    public String getDdProbationPeriodType() {
        return ddProbationPeriodType;
    }

    public void setDdProbationPeriodType(String ddProbationPeriodType) {
        this.ddProbationPeriodType = ddProbationPeriodType == null ? null : ddProbationPeriodType.trim();
    }

    public String getDdRegularTime() {
        return ddRegularTime;
    }

    public void setDdRegularTime(String ddRegularTime) {
        this.ddRegularTime = ddRegularTime == null ? null : ddRegularTime.trim();
    }

    public String getDdConfirmJoinTime() {
        return ddConfirmJoinTime;
    }

    public void setDdConfirmJoinTime(String ddConfirmJoinTime) {
        this.ddConfirmJoinTime = ddConfirmJoinTime == null ? null : ddConfirmJoinTime.trim();
    }

    public String getDdGraduateSchool() {
        return ddGraduateSchool;
    }

    public void setDdGraduateSchool(String ddGraduateSchool) {
        this.ddGraduateSchool = ddGraduateSchool == null ? null : ddGraduateSchool.trim();
    }

    public String getDdEmail() {
        return ddEmail;
    }

    public void setDdEmail(String ddEmail) {
        this.ddEmail = ddEmail == null ? null : ddEmail.trim();
    }

    public String getDdBankAccountNo() {
        return ddBankAccountNo;
    }

    public void setDdBankAccountNo(String ddBankAccountNo) {
        this.ddBankAccountNo = ddBankAccountNo == null ? null : ddBankAccountNo.trim();
    }

    public String getDdAccountBank() {
        return ddAccountBank;
    }

    public void setDdAccountBank(String ddAccountBank) {
        this.ddAccountBank = ddAccountBank == null ? null : ddAccountBank.trim();
    }

    public Long getDdMainDeptId() {
        return ddMainDeptId;
    }

    public void setDdMainDeptId(Long ddMainDeptId) {
        this.ddMainDeptId = ddMainDeptId;
    }

    public String getDdMainDept() {
        return ddMainDept;
    }

    public void setDdMainDept(String ddMainDept) {
        this.ddMainDept = ddMainDept == null ? null : ddMainDept.trim();
    }

    public String getDdCompanyName() {
        return ddCompanyName;
    }

    public void setDdCompanyName(String ddCompanyName) {
        this.ddCompanyName = ddCompanyName == null ? null : ddCompanyName.trim();
    }

    public Integer getDdTsExclFlag() {
        return ddTsExclFlag;
    }

    public void setDdTsExclFlag(Integer ddTsExclFlag) {
        this.ddTsExclFlag = ddTsExclFlag;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}