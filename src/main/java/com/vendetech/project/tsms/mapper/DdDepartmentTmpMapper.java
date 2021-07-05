package com.vendetech.project.tsms.mapper;

import java.util.Map;

import com.vendetech.project.tsms.domain.DdDepartmentTmp;

public interface DdDepartmentTmpMapper {
	
	int deleteByPrimaryKey(Long ddDepartmentId);

	int insert(DdDepartmentTmp record);

	int insertSelective(DdDepartmentTmp record);

	DdDepartmentTmp selectByPrimaryKey(Long ddDepartmentId);

	int updateByPrimaryKeySelective(DdDepartmentTmp record);

	int updateByPrimaryKey(DdDepartmentTmp record);
	
	void callRefreshDepartments(Map<String, Object> params);
	
}