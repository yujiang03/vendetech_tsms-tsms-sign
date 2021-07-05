package com.vendetech.project.tsms.timesheet.service;

import java.util.List;
import java.util.Map;

import com.vendetech.project.tsms.domain.Department;
import com.vendetech.project.tsms.domain.EmpTsAttdAsgnRecord;
import com.vendetech.project.tsms.domain.Employee;

public interface DashboardService {

	public Integer getNeedAssignHours(Map<String, Object> params);

	public Integer getAtcualAssignHours(Map<String, Object> params);

	public List<Department> getLctdDepartments(String empNum); 		// 下属所有部门

	public List<Department> getMgrdDepartments(String mgrNum); 	// 下属所有部门及子部门
	
	public List<Department> getAllMgrDepartments(String mgrNum); 	// 下属所有部门及子部门

	public List<Employee> getMgrEmployees(String empNum); 			// 下属所有员工

	public List<EmpTsAttdAsgnRecord> getUnmatchList(Map<String, Object> params);

	public List<EmpTsAttdAsgnRecord> getUnmatchListByMonth(Map<String, Object> params);

	public Department getEmpMainDepartment(String empNum); 		// 主部门

	public Department getEmpMainBuDepartment(String mainDdDepartmentId); 	// 主部门所在事业部

	public List<Employee> getMainBuDeptEmployees(String mainBuDdDepartmentId); // 主部门所在事业部下属所有员工
	
	public String getChildrenDepts(String mainDdDepartmentId);

	public Department getDepartmentById(Long departmentId);

	public Integer countStaff(String depts);

}
