package com.vendetech.project.tsms.timesheetAssignment.controller.DTO;


import java.util.List;

public class UpdateTimesheetAssingnmentDTO {

    private Integer assignType;

    private Long projectId;

    private String[] id;

	/**
	 * @return the assignType
	 */
	public Integer getAssignType() {
		return assignType;
	}

	/**
	 * @param assignType the assignType to set
	 */
	public void setAssignType(Integer assignType) {
		this.assignType = assignType;
	}

	/**
	 * @return the projectId
	 */
	public Long getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
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
