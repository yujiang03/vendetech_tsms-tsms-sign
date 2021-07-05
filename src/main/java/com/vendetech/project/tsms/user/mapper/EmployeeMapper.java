package com.vendetech.project.tsms.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.vendetech.project.tsms.domain.Employee;

public interface EmployeeMapper {

	int deleteByPrimaryKey(Long employeeId);

	int insert(Employee record);

	int insertSelective(Employee record);

	Employee selectByPrimaryKey(Long employeeId);

	int updateByPrimaryKeySelective(Employee record);

	List<Employee> selectByEmployeeName(@Param("employeeName") String employeeName);

	Employee getOneByEmployeeName(@Param("employeeName") String employeeName);

	List<Employee> getMgrEmployees(@Param("mgrNum") String mgrNum);

	List<Employee> getDepartmentEmployees(@Param("dptNum") String dptNum);

	Employee selectByEmployeeDdUserId(@Param("ddUserid") String ddUserid);

	List<Employee> getOneByEmployeeNum(@Param("employeeNum") String employeeNum);

	List<Employee> findEmployeeByNumName(@Param("employeeNum") String employeeNum,
			@Param("employeeName") String employeeName);

	List<Employee> selectByEmpNum(@Param("employeeNum") String employeeNum);

	List<Employee> selectAllEmployee();
}