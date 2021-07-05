package com.vendetech.project.tsms.timesheetAssignment.controller.DTO;

public class EmployeeDTO {
	private String employeeNum;

	private String employeeName;

	private Long employeeId;

	private String assignmentName;

	private Long tsAsgnId;

	private String projectManager;

	public String getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(String projectManager) {
		this.projectManager = projectManager;
	}

	public Long getTsAsgnId() {
		return tsAsgnId;
	}

	public void setTsAsgnId(Long tsAsgnId) {
		this.tsAsgnId = tsAsgnId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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
	 * @return the assignmentName
	 */
	public String getAssignmentName() {
		return assignmentName;
	}

	/**
	 * @param assignmentName the assignmentName to set
	 */
	public void setAssignmentName(String assignmentName) {
		this.assignmentName = assignmentName;
	}
    
    

}
