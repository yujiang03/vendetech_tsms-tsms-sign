package com.vendetech.project.tsms.timesheetAssignment.controller.DTO;


public class GetByTsAsgnIdDTO {
    private Long employeeId;
    private String employeeName;
    private String employeeNum;
    private Long tsAsgnId;
	/**
	 * @return the employeeId
	 */
	public Long getEmployeeId() {
		return employeeId;
	}
	/**
	 * @param employeeId the employeeId to set
	 */
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	/**
	 * @return the employeeName
	 */
	public String getEmployeeName() {
		return employeeName;
	}
	/**
	 * @param employeeName the employeeName to set
	 */
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	/**
	 * @return the employeeNum
	 */
	public String getEmployeeNum() {
		return employeeNum;
	}
	/**
	 * @param employeeNum the employeeNum to set
	 */
	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum;
	}


	public Long getTsAsgnId() {
		return tsAsgnId;
	}

	public void setTsAsgnId(Long tsAsgnId) {
		this.tsAsgnId = tsAsgnId;
	}
}
