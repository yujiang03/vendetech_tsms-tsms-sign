package com.vendetech.hr.vo;

import java.io.Serializable;

public class SignStatisticsVO implements Serializable {
	private Integer id;
	private String tplCode;
	private String tplName;
	private String tplPath;
	private Integer totalShouldSign;
	private Integer alreadySign;
	private Integer noneSign;

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

	public Integer getTotalShouldSign() {
		return totalShouldSign;
	}

	public void setTotalShouldSign(Integer totalShouldSign) {
		this.totalShouldSign = totalShouldSign;
	}

	public Integer getAlreadySign() {
		return alreadySign;
	}

	public void setAlreadySign(Integer alreadySign) {
		this.alreadySign = alreadySign;
	}

	public Integer getNoneSign() {
		return noneSign;
	}

	public void setNoneSign(Integer noneSign) {
		this.noneSign = noneSign;
	}
}
