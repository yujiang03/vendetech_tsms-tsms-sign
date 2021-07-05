package com.vendetech.project.tsms.project.mapper;

import com.vendetech.project.tsms.domain.Department;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DepartmentMapper {

	int deleteByPrimaryKey(Long departmentId);

	int insert(Department record);

	int insertSelective(Department record);

	Department selectByPrimaryKey(Long departmentId);

	int updateByPrimaryKeySelective(Department record);

	int updateByPrimaryKey(Department record);

	List<Department> getDepartMentNameList();

	List<Department> getByemployeeName(@Param("employeeName") String employeeName);

	List<Department> getLctdDepartments(@Param("mgrNum") String mgrNum);

	List<Department> getMgrdDepartments(@Param("mgrNum") String mgrNum);
	
	List<Department> getChildrenDeparments(@Param("ddDepartmentId") String ddDepartmentId);

	Department getDepartMentId(String departmentNum);

	Department getEmpMainDepartment(String empNum);

	List<Department> getEmpMainBuDepartment(String mainDdDepartmentId);

	String getChildrenDepts(String mainDdDepartmentId);

	List<Department> getDepartmentById(Long departmentId);

}