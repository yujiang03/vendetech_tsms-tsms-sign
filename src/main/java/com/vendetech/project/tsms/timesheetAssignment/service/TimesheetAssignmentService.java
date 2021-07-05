package com.vendetech.project.tsms.timesheetAssignment.service;

import java.util.HashMap;
import java.util.List;

import com.vendetech.project.tsms.domain.Employee;
import com.vendetech.project.tsms.domain.Project;
import com.vendetech.project.tsms.domain.TimesheetAssignment;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.CreateTimesheetAssingnmentDTO;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.CreateTimesheetAssingnmentDetailDTO;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.EmployeeDTO;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.GetByTsAsgnIdDTO;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.TimesheetAssignmentDetailByAssignTypeDTO;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.TimesheetAssignmentDetailDTO;

public interface TimesheetAssignmentService {
    List<TimesheetAssignment> getTimesheetAssignment(TimesheetAssignment timesheetAssignment);

    List<Project> getStatusList(Project project);

    List<HashMap<String, Object>> getDeparmentEmployeeName(String keyword);

    List<EmployeeDTO> getAssignmentName(EmployeeDTO employeeDTO);

    List<TimesheetAssignment> saveAssignment(CreateTimesheetAssingnmentDTO createTimesheetAssingnmentDTO);

    List<HashMap<String, Object>>  selectByTsAsgnId(TimesheetAssignmentDetailByAssignTypeDTO timesheetAssignmentDetailByAssignTypeDTO);

    int updateByPrimaryKey(List<CreateTimesheetAssingnmentDetailDTO> createTimesheetAssingnmentDetailDTO);

    List<Project> getByProjectNum(Project project);

    int deleteTimesheetAssignmentDetail(TimesheetAssignmentDetailDTO timesheetAssignmentDetailDTO);

    List<GetByTsAsgnIdDTO> getByTsAsgnId(Long tsAsgnId);

    List<Employee> getOneByEmployeeNum(String employeeNum);

	/**
	 * @param params
	 */
	Long saveAssignmentNew(HashMap<String,Object> params);
}
