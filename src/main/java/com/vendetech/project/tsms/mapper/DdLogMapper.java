package com.vendetech.project.tsms.mapper;

import com.vendetech.project.tsms.domain.DdLog;

public interface DdLogMapper {
    int deleteByPrimaryKey(Long dlId);

	int insert(DdLog record);

	int insertSelective(DdLog record);

	DdLog selectByPrimaryKey(Long dlId);

	int updateByPrimaryKeySelective(DdLog record);

	int updateByPrimaryKeyWithBLOBs(DdLog record);

	int updateByPrimaryKey(DdLog record);

}