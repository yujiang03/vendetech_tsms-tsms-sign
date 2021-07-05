package com.vendetech.project.tsms.timesheet.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.vendetech.project.tsms.domain.DdAttendanceData;

public interface DdAttendanceDataMapper {
	int deleteByPrimaryKey(Long ddAttendanceId);

	int insert(DdAttendanceData record);

	int insertSelective(DdAttendanceData record);

	DdAttendanceData selectByPrimaryKey(Long ddAttendanceId);

	int updateByPrimaryKeySelective(DdAttendanceData record);

	int updateByPrimaryKey(DdAttendanceData record);

	int selectByEmpNumDate(@Param("empNum") String empNum, @Param("attdDate") Date attdDate);

	int updateByEmpNumDatesSelective(DdAttendanceData attendance);

	int updateByDdUseridDatesSelective(DdAttendanceData attendance);
}