package com.vendetech.project.tsms.timesheetAssignment.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.vendetech.project.tsms.domain.TimesheetAssignmentDetail;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.EmployeeDTO;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.GetByTsAsgnIdDTO;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.TimesheetAssignmentDetailByAssignTypeDTO;

public interface TimesheetAssignmentDetailMapper {
	int deleteByPrimaryKey(Long tsAsgnDtlId);

	int insert(TimesheetAssignmentDetail record);

	int insertSelective(TimesheetAssignmentDetail record);

	TimesheetAssignmentDetail selectByPrimaryKey(Long tsAsgnDtlId);

	int updateByPrimaryKeySelective(TimesheetAssignmentDetail record);

	int updateByPrimaryKey(TimesheetAssignmentDetail timesheetAssignmentDetail);

	List<EmployeeDTO> getAssignmentName(EmployeeDTO employeeDTO);

	List<HashMap<String, Object>> selectByTsAsgnId(
			TimesheetAssignmentDetailByAssignTypeDTO timesheetAssignmentDetailByAssignTypeDTO);

	int disableTimesheetAssignmentDetail(@Param("id") String[] id);

	int deleteTimesheetAssignmentDetail(@Param("id") String[] id);

	List<GetByTsAsgnIdDTO> getByTsAsgnId(Long tsAsgnId);

	int updateStatusByProjectId(TimesheetAssignmentDetail timesheetAssignmentDetail);

	List<TimesheetAssignmentDetail> selectByTsDate(TimesheetAssignmentDetail timesheetAssignmentDetail);

	int updateByTsDate(TimesheetAssignmentDetail timesheetAssignmentDetail);

	/**
	 * @param map
	 */
	void insertTimeSheetDetail(HashMap<String, Object> map);

	/**
	 * @param map
	 */
	void batchUpdateByTsDate(HashMap<String, Object> map);
}
