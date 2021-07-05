package com.vendetech.project.tsms.timesheetAssignment.mapper;


import com.vendetech.project.tsms.domain.Project;
import com.vendetech.project.tsms.domain.TimesheetAssignment;

import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

public interface TimesheetAssignmentMapper {
    int deleteByPrimaryKey(Long tsAsgnId);

    int insert(TimesheetAssignment record);

    int insertSelective(TimesheetAssignment record);

    TimesheetAssignment selectByPrimaryKey(Long tsAsgnId);

    int updateByPrimaryKeySelective(TimesheetAssignment record);

    int updateByPrimaryKey(TimesheetAssignment record);

    List<TimesheetAssignment> getTimesheetAssignment(TimesheetAssignment timesheetAssignment);

    List<Project> getStatusList(Project project);

    List<HashMap<String, Object>> getDeparmentEmployeeName(@Param("keyword") String keyword);

    List<TimesheetAssignment> getByTsProjectId(TimesheetAssignment timesheetAssignment);

    int updateStatusByProjectId(TimesheetAssignment timesheetAssignment2);

    List<TimesheetAssignment> getByStatusId(TimesheetAssignment timesheetAssignment1);

    TimesheetAssignment getStatusName(Long tsAsgnId);

	/**
	 * @param params
	 */
	int insertTimeSheet(HashMap<String, Object> params);

	int insertTimesheetAssignment(TimesheetAssignment timesheetAssignment);
}