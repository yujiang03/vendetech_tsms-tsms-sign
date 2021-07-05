package com.vendetech.project.tsms.mapper;

import java.util.Map;

import com.vendetech.project.tsms.domain.DdUserTmp;
import org.apache.ibatis.annotations.Update;

public interface DdUserTmpMapper {

	int deleteByPrimaryKey(Long ddUserId);

	int insert(DdUserTmp record);

	int insertSelective(DdUserTmp record);

	DdUserTmp selectByPrimaryKey(Long ddUserId);

	int updateByPrimaryKeySelective(DdUserTmp record);

	int updateByPrimaryKey(DdUserTmp record);

	void callRefreshUsers();

	/**
	 * @param param
	 */
	void createEmployeeAccount(Map<String, Object> param);

	@Update("truncate table dd_user_tmp")
	int truncateDdUserTmp();

}