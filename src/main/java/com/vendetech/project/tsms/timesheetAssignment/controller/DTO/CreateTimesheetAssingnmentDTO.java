package com.vendetech.project.tsms.timesheetAssignment.controller.DTO;

import java.util.Date;
import java.util.List;

public class CreateTimesheetAssingnmentDTO{
    private Long tsAsgnId;

    private Long tsProjectId;

    private Long tsDeparmentId;

    private String tsStartDate;

    private String tsEndDate;

    private Integer assignType;

    private Long tsCreatorUserId;

    private Integer status;

    private String tsCreatedBy;

    private Date createTime;

    private Date modifyTime;

    List<CreateTimesheetAssingnmentDetailDTO> createTimesheetAssingnmentDetailDTOList;

	/**
	 * @return the tsAsgnId
	 */
	public Long getTsAsgnId() {
		return tsAsgnId;
	}

	/**
	 * @param tsAsgnId the tsAsgnId to set
	 */
	public void setTsAsgnId(Long tsAsgnId) {
		this.tsAsgnId = tsAsgnId;
	}

	/**
	 * @return the tsProjectId
	 */
	public Long getTsProjectId() {
		return tsProjectId;
	}

	/**
	 * @param tsProjectId the tsProjectId to set
	 */
	public void setTsProjectId(Long tsProjectId) {
		this.tsProjectId = tsProjectId;
	}

	/**
	 * @return the tsDeparmentId
	 */
	public Long getTsDeparmentId() {
		return tsDeparmentId;
	}

	/**
	 * @param tsDeparmentId the tsDeparmentId to set
	 */
	public void setTsDeparmentId(Long tsDeparmentId) {
		this.tsDeparmentId = tsDeparmentId;
	}

	/**
	 * @return the tsStartDate
	 */
	public String getTsStartDate() {
		return tsStartDate;
	}

	/**
	 * @param tsStartDate the tsStartDate to set
	 */
	public void setTsStartDate(String tsStartDate) {
		this.tsStartDate = tsStartDate;
	}

	/**
	 * @return the tsEndDate
	 */
	public String getTsEndDate() {
		return tsEndDate;
	}

	/**
	 * @param tsEndDate the tsEndDate to set
	 */
	public void setTsEndDate(String tsEndDate) {
		this.tsEndDate = tsEndDate;
	}

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
	 * @return the tsCreatorUserId
	 */
	public Long getTsCreatorUserId() {
		return tsCreatorUserId;
	}

	/**
	 * @param tsCreatorUserId the tsCreatorUserId to set
	 */
	public void setTsCreatorUserId(Long tsCreatorUserId) {
		this.tsCreatorUserId = tsCreatorUserId;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the tsCreatedBy
	 */
	public String getTsCreatedBy() {
		return tsCreatedBy;
	}

	/**
	 * @param tsCreatedBy the tsCreatedBy to set
	 */
	public void setTsCreatedBy(String tsCreatedBy) {
		this.tsCreatedBy = tsCreatedBy;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the modifyTime
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime the modifyTime to set
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * @return the createTimesheetAssingnmentDetailDTOList
	 */
	public List<CreateTimesheetAssingnmentDetailDTO> getCreateTimesheetAssingnmentDetailDTOList() {
		return createTimesheetAssingnmentDetailDTOList;
	}

	/**
	 * @param createTimesheetAssingnmentDetailDTOList the createTimesheetAssingnmentDetailDTOList to set
	 */
	public void setCreateTimesheetAssingnmentDetailDTOList(
			List<CreateTimesheetAssingnmentDetailDTO> createTimesheetAssingnmentDetailDTOList) {
		this.createTimesheetAssingnmentDetailDTOList = createTimesheetAssingnmentDetailDTOList;
	}

    
    
}
