package com.vendetech.project.tsms.timesheetAssignment.controller.DTO;

import com.vendetech.project.tsms.domain.TimesheetAssignmentDetail;

public class TimesheetAssignmentDetailDTO extends TimesheetAssignmentDetail {
    private String keyword;

    private Integer assignType;

    private Long tsAsgnId;

    private String[] id;

	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * @return the assignType
	 */
    @Override
	public Integer getAssignType() {
		return assignType;
	}

	/**
	 * @param assignType the assignType to set
	 */
    @Override
	public void setAssignType(Integer assignType) {
		this.assignType = assignType;
	}

	/**
	 * @return the tsAsgnId
	 */
    @Override
	public Long getTsAsgnId() {
		return tsAsgnId;
	}

	/**
	 * @param tsAsgnId the tsAsgnId to set
	 */
    @Override
	public void setTsAsgnId(Long tsAsgnId) {
		this.tsAsgnId = tsAsgnId;
	}

	/**
	 * @return the id
	 */
	public String[] getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String[] id) {
		this.id = id;
	}
    
    

}
