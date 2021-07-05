package com.vendetech.hr.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

public class SignTemplateVO extends BaseVO {
    private Integer id;
    private String tplCode;
    private String tplName;
    private String tplPath;
    private String isUpload;
    private Integer tplExpireDay;
    private Integer tplExpireYear;
    private Integer status;
    private String statusDesc;
    @JsonFormat
    private String createTime;
    private Long createByUserId;
	@JsonFormat
    private String modifyTime;
    private Long modifyByUserId;

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

    public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTplCode() {
		return tplCode;
	}

	public void setTplCode(String tplCode) {
		this.tplCode = tplCode;
	}

	public String getTplName() {
		return tplName;
	}

	public void setTplName(String tplName) {
		this.tplName = tplName;
	}

	public String getTplPath() {
		return tplPath;
	}

	public void setTplPath(String tplPath) {
		this.tplPath = tplPath;
	}

	public String getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}

	public Integer getTplExpireDay() {
		return tplExpireDay;
	}

	public void setTplExpireDay(Integer tplExpireDay) {
		this.tplExpireDay = tplExpireDay;
	}

	public Integer getTplExpireYear() {
		return tplExpireYear;
	}

	public void setTplExpireYear(Integer tplExpireYear) {
		this.tplExpireYear = tplExpireYear;
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
}