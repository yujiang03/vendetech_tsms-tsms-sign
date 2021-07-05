package com.vendetech.hr.vo;

import java.util.Date;

public class SignCompanySignature extends BaseVO {

    private Long signatureId;
    private Long companyId;
    private String signatureName;
    private String fddSignatureId;
    private Integer fddSignatureType;
    private Integer fddSignatureScope;
    private String fddSignatureSubInfo;
    private Date createTime;
    private Long createByUserId;
    private Date modifyTime;
    private Long modifyByUserId;
    private String fddSignatureImgBase64;

    // private String fddSignatureImgBase64Str;
    private String companyName;
    private String customerId;

    public Long getSignatureId() {
        return signatureId;
    }

    public void setSignatureId(Long signatureId) {
        this.signatureId = signatureId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getSignatureName() {
        return signatureName;
    }

    public void setSignatureName(String signatureName) {
        this.signatureName = signatureName;
    }

    public String getFddSignatureId() {
        return fddSignatureId;
    }

    public void setFddSignatureId(String fddSignatureId) {
        this.fddSignatureId = fddSignatureId == null ? null : fddSignatureId.trim();
    }

    public Integer getFddSignatureType() {
        return fddSignatureType;
    }

    public void setFddSignatureType(Integer fddSignatureType) {
        this.fddSignatureType = fddSignatureType;
    }

    public Integer getFddSignatureScope() {
        return fddSignatureScope;
    }

    public void setFddSignatureScope(Integer fddSignatureScope) {
        this.fddSignatureScope = fddSignatureScope;
    }

    public String getFddSignatureSubInfo() {
        return fddSignatureSubInfo;
    }

    public void setFddSignatureSubInfo(String fddSignatureSubInfo) {
        this.fddSignatureSubInfo = fddSignatureSubInfo == null ? null : fddSignatureSubInfo.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCreateByUserId() {
        return createByUserId;
    }

    public void setCreateByUserId(Long createByUserId) {
        this.createByUserId = createByUserId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getModifyByUserId() {
        return modifyByUserId;
    }

    public void setModifyByUserId(Long modifyByUserId) {
        this.modifyByUserId = modifyByUserId;
    }

    public String getFddSignatureImgBase64() {
        return fddSignatureImgBase64;
    }

    public void setFddSignatureImgBase64(String fddSignatureImgBase64) {
        this.fddSignatureImgBase64 = fddSignatureImgBase64;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    // public String getFddSignatureImgBase64Str() {
    //     return fddSignatureImgBase64Str;
    // }
    //
    // public void setFddSignatureImgBase64Str(String fddSignatureImgBase64Str) {
    //     this.fddSignatureImgBase64Str = fddSignatureImgBase64Str;
    // }
}