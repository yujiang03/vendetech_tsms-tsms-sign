package com.vendetech.project.tsms.mapper;

import com.vendetech.project.tsms.domain.DdHrmDataTmp;

public interface DdHrmDataTmpMapper {

//	int insert(DdHrmDataTmp record);

	int insertSelective(DdHrmDataTmp record);

	DdHrmDataTmp selectByPrimaryKey(Long ddDepartmentId);

//	int updateByPrimaryKeySelective(DdHrmDataTmp record);

//	int updateByPrimaryKey(DdHrmDataTmp record);

	void callRefreshEmpHrmData();

}