package com.vendetech.project.tsms.user.service;

import com.vendetech.project.tsms.domain.Employee;

import java.util.List;

public interface EmployeeService {
	List<Employee> selectByEmployeeName(String employeeName);

	List<Employee> getOneByEmployeeNum(String employeeNum);
}
