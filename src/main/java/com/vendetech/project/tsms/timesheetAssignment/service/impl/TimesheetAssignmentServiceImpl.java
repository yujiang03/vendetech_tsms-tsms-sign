package com.vendetech.project.tsms.timesheetAssignment.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vendetech.common.utils.StringUtils;
import com.vendetech.project.tsms.domain.Employee;
import com.vendetech.project.tsms.domain.Project;
import com.vendetech.project.tsms.domain.TimesheetAssignment;
import com.vendetech.project.tsms.domain.TimesheetAssignmentDetail;
import com.vendetech.project.tsms.project.mapper.ProjectMapper;
import com.vendetech.project.tsms.timesheet.mapper.EmpTsAttdAsgnRecordMapper;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.CreateTimesheetAssingnmentDTO;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.CreateTimesheetAssingnmentDetailDTO;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.EmployeeDTO;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.GetByTsAsgnIdDTO;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.TimesheetAssignmentDetailByAssignTypeDTO;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.TimesheetAssignmentDetailDTO;
import com.vendetech.project.tsms.timesheetAssignment.mapper.TimesheetAssignmentDetailMapper;
import com.vendetech.project.tsms.timesheetAssignment.mapper.TimesheetAssignmentMapper;
import com.vendetech.project.tsms.timesheetAssignment.service.TimesheetAssignmentService;
import com.vendetech.project.tsms.user.mapper.EmployeeMapper;

@Service
@Transactional
public class TimesheetAssignmentServiceImpl implements TimesheetAssignmentService {

    @Autowired
    private TimesheetAssignmentMapper timesheetAssignmentMapper;

    @Autowired
    private TimesheetAssignmentDetailMapper timesheetAssignmentDetailMapper;

	@Autowired
	private EmployeeMapper employeeMapper;

	@Autowired
	private ProjectMapper projectMapper;

	@Autowired
	private EmpTsAttdAsgnRecordMapper empTsAttdAsgnRecordMapper;

	/**
	 * 工单列表
	 * 
	 * @param timesheetAssignment
	 * @return
	 */
    @Override
    public List<TimesheetAssignment> getTimesheetAssignment(TimesheetAssignment timesheetAssignment) {
        //首先查询所有数据
        List<TimesheetAssignment> timesheetAssignment2 = timesheetAssignmentMapper.getTimesheetAssignment(timesheetAssignment);
        //遍历判断
        for (TimesheetAssignment timesheetAssignment1:timesheetAssignment2) {
            if (timesheetAssignment1.getAssignmentName() == null) {
                TimesheetAssignment statusName = timesheetAssignmentMapper.getStatusName(timesheetAssignment1.getTsAsgnId());
                timesheetAssignment1.setAssignmentName(statusName.getAssignmentName());
            }
        }

        return timesheetAssignment2;
    }
    /**
     * 状态查询
     * @return
     */
    @Override
    public List<Project> getStatusList(Project project) {
        List<Project> statusList = timesheetAssignmentMapper.getStatusList(project);
        return statusList;
    }

    /**
     * 部门员工模糊查询
     * @param keyword
     * @return
     */
    @Override
    public List<HashMap<String, Object>> getDeparmentEmployeeName(String keyword) {
        List<HashMap<String, Object>>  hashMaps = timesheetAssignmentMapper.getDeparmentEmployeeName(keyword);
        return hashMaps;
    }

    /**
     * 工单名称模糊查询
     * @param assignmentName
     * @return
     */
    @Override
    public List<EmployeeDTO> getAssignmentName(EmployeeDTO employeeDTO) {
        List<EmployeeDTO> assignmentName1 = timesheetAssignmentDetailMapper.getAssignmentName(employeeDTO);
        return assignmentName1;
    }

    /**
     * 添加工单
     * @param createTimesheetAssingnmentDTO
     * @return
     */
    @Override
    public List<TimesheetAssignment> saveAssignment(CreateTimesheetAssingnmentDTO createTimesheetAssingnmentDTO) {
        long start = System.currentTimeMillis();
        //添加工单
        TimesheetAssignment timesheetAssignment = new TimesheetAssignment();
        timesheetAssignment.setTsProjectId(createTimesheetAssingnmentDTO.getTsProjectId());
        timesheetAssignment.setAssignType(createTimesheetAssingnmentDTO.getAssignType());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(createTimesheetAssingnmentDTO.getTsStartDate());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(createTimesheetAssingnmentDTO.getTsEndDate());
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        timesheetAssignment.setTsStartDate(date1);
        timesheetAssignment.setTsEndDate(date2);
        if(createTimesheetAssingnmentDTO.getTsProjectId()!=null) {
            Project projectDepartment = projectMapper.getProjectDepartment(createTimesheetAssingnmentDTO.getTsProjectId());
            timesheetAssignment.setTsDeparmentId(projectDepartment.getProjectDepartmentId());
        }
        timesheetAssignment.setTsCreatorUserId(createTimesheetAssingnmentDTO.getTsCreatorUserId());
        timesheetAssignment.setTsProjectId(createTimesheetAssingnmentDTO.getTsProjectId());
        timesheetAssignment.setTsCreatedBy(createTimesheetAssingnmentDTO.getTsCreatedBy());
        timesheetAssignment.setStatus(1);
        timesheetAssignmentMapper.insert(timesheetAssignment);
        //添加工时单详情
        if (createTimesheetAssingnmentDTO.getCreateTimesheetAssingnmentDetailDTOList() != null) {
            for (CreateTimesheetAssingnmentDetailDTO createTimesheetAssingnmentDetailDTO : createTimesheetAssingnmentDTO.getCreateTimesheetAssingnmentDetailDTOList()) {
                TimesheetAssignmentDetail timesheetAssignmentDetail = new TimesheetAssignmentDetail();
                long datas = getDays(date2, date1);
                for (int i = 0; i < datas + 1; i++) {
                    timesheetAssignmentDetail.setTsAsgnId(timesheetAssignment.getTsAsgnId());
                    timesheetAssignmentDetail.setTsAsgnDtlId(createTimesheetAssingnmentDetailDTO.getTsAsgnDtlId());
                    timesheetAssignmentDetail.setAssignType(createTimesheetAssingnmentDTO.getAssignType());
                    timesheetAssignmentDetail.setEmployeeName(createTimesheetAssingnmentDetailDTO.getEmployeeName());
                    timesheetAssignmentDetail.setProjectId(createTimesheetAssingnmentDTO.getTsProjectId());
                    timesheetAssignmentDetail.setIfIssue(0);
                    timesheetAssignmentDetail.setEmployeeNum(createTimesheetAssingnmentDetailDTO.getEmployeeNum());
                    timesheetAssignmentDetail.setEmployeeName(createTimesheetAssingnmentDetailDTO.getEmployeeName());
                    timesheetAssignmentDetail.setEmployeeId(createTimesheetAssingnmentDetailDTO.getEmployeeId());
                    Calendar calendar = Calendar.getInstance();
                    Date date3 = null;
                    try {
                        date3 = simpleDateFormat.parse(createTimesheetAssingnmentDTO.getTsStartDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    }
                    calendar.setTime(date3);
                    calendar.add(calendar.DATE, i);
                    date3 = calendar.getTime();
                    timesheetAssignmentDetail.setTsDate(date3);
                    List<TimesheetAssignmentDetail> timesheetAssignmentDetails = timesheetAssignmentDetailMapper.selectByTsDate(timesheetAssignmentDetail);
                    for(TimesheetAssignmentDetail timesheetAssignmentDetail1:timesheetAssignmentDetails){
                        if(timesheetAssignmentDetail1!=null){
                            timesheetAssignmentDetailMapper.updateByTsDate(timesheetAssignmentDetail1);
                        }
                    }
                    timesheetAssignmentDetailMapper.insert(timesheetAssignmentDetail);
                }
            }
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("msg", new String());
        params.put("tsAssgnId",timesheetAssignment.getTsAsgnId());
        empTsAttdAsgnRecordMapper.freshEmpTsAttdAsgnRecord(params);
        long end = System.currentTimeMillis();
        System.err.println(end-start);
        return null;
    }
    
    
    /**
     * 添加工单
     * @param createTimesheetAssingnmentDTO
     * @return
     */
    @SuppressWarnings("unchecked")
	@Override
    @Transactional
    public Long saveAssignmentNew(HashMap<String,Object> params) {
    	long start = System.currentTimeMillis();
    	TimesheetAssignment ta = new TimesheetAssignment();
    	ta.setAssignType(Integer.parseInt(""+params.get("assignType")));
		if (null != params.get("tsProjectId") && StringUtils.isNotBlank("" + params.get("tsProjectId"))) {
			ta.setProjectId(Long.parseLong("" + params.get("tsProjectId")));
		} else {
			ta.setProjectId(null);
		}
  		ta.setStatus(1);
  		
    	if(StringUtils.isNotEmpty(params.get("tsProjectId"))) {
            Project projectDepartment = projectMapper.getProjectDepartment(Long.parseLong(params.get("tsProjectId").toString()));
            params.put("tsDeparmentId", projectDepartment.getProjectDepartmentId());
      		ta.setDepartmentId(projectDepartment.getProjectDepartmentId());
        }
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
    	try {
        	ta.setTsStartDate(sf.parse(params.get("tsStartDate").toString()));
			ta.setTsEndDate(sf.parse(params.get("tsEndDate").toString()));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
    	ta.setTsCreatorUserId(Long.parseLong(""+params.get("currentUserId")));
    	ta.setTsCreatedBy(""+(params.get("currentUserName")));
    	timesheetAssignmentMapper.insertTimesheetAssignment(ta);
//    	timesheetAssignmentMapper.insertTimeSheet(params);
    	Long tsAssgnId = ta.getTsAsgnId();
    	if (StringUtils.isNotEmpty(params.get("createTimesheetAssingnmentDetailDTOList"))) {
    		List<HashMap<String,Object>> list = (ArrayList<HashMap<String,Object>>)params.get("createTimesheetAssingnmentDetailDTOList");
//    		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM
    		long datas = 0;
    		try {
    			datas = getDays(sf.parse(params.get("tsEndDate").toString()), sf.parse(params.get("tsStartDate").toString()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	            throw new RuntimeException();
			}
			for (HashMap<String, Object> map : list) {
				map.put("tsAsgnId", tsAssgnId);
				map.put("assignType", params.get("assignType"));
				map.put("tsProjectId", params.get("tsProjectId"));
				Long empId = 0L + (Integer)map.get("employeeId");
				Employee emp = employeeMapper.selectByPrimaryKey(empId);
				if (!(0 == emp.getStatus())) {
					for (int i = 0; i < datas + 1; i++) {
						Date date = null;
						Calendar calendar = Calendar.getInstance();
						try {
							calendar.setTime(sf.parse(params.get("tsStartDate").toString()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new RuntimeException();
						}
						calendar.add(Calendar.DATE, i);
						date = calendar.getTime();
						map.put("tsDate", date);
						timesheetAssignmentDetailMapper.batchUpdateByTsDate(map);
						timesheetAssignmentDetailMapper.insertTimeSheetDetail(map);
					}
				}
    		}
    	}
    	long end = System.currentTimeMillis();
        System.err.println(end-start);
        return tsAssgnId;
    }

    /**
     * 根据工单Id 查询
     * @param param
     * @return
     */
    @Override
    public List<HashMap<String, Object>> selectByTsAsgnId(TimesheetAssignmentDetailByAssignTypeDTO timesheetAssignmentDetailByAssignTypeDTO) {
        List<HashMap<String, Object>> hashMaps = timesheetAssignmentDetailMapper.selectByTsAsgnId(timesheetAssignmentDetailByAssignTypeDTO);
        return hashMaps;
    }

	@Override
	public int updateByPrimaryKey(List<CreateTimesheetAssingnmentDetailDTO> createTimesheetAssingnmentDetailDTO) {
		for (CreateTimesheetAssingnmentDetailDTO createTimesheetAssingnmentDetailDTO1 : createTimesheetAssingnmentDetailDTO) {
			TimesheetAssignmentDetail timesheetAssignmentDetail = new TimesheetAssignmentDetail();
			timesheetAssignmentDetail.setTsAsgnDtlId(createTimesheetAssingnmentDetailDTO1.getTsAsgnDtlId());
			timesheetAssignmentDetail.setAssignType(createTimesheetAssingnmentDetailDTO1.getAssignType());
			timesheetAssignmentDetail.setProjectId(createTimesheetAssingnmentDetailDTO1.getProjectId());
			timesheetAssignmentDetailMapper.updateByPrimaryKey(timesheetAssignmentDetail);
		}
		return 0;
	}

    @Override
    public List<Project> getByProjectNum(Project project) {
        return projectMapper.getByProjectNum(project);
    }

	@Override
	public int deleteTimesheetAssignmentDetail(TimesheetAssignmentDetailDTO timesheetAssignmentDetailDTO) {
		String[] ids = timesheetAssignmentDetailDTO.getId();
		if (null != ids) {
			timesheetAssignmentDetailMapper.disableTimesheetAssignmentDetail(ids);
			empTsAttdAsgnRecordMapper.clearEmpTsAttdAsgnRecords(ids);
		}
//		Map<String, Object> proc = new HashMap<String, Object>();
//		proc.put("msg", new String());
//		proc.put("tsAssgnId", timesheetAssignmentDetailDTO.getTsAsgnId());
//		empTsAttdAsgnRecordService.freshEmpTsAttdAsgnRecord(proc);
//		if (null != ids) {
//			timesheetAssignmentDetailMapper.deleteTimesheetAssignmentDetail(ids);
//		}
		return 0;
	}

	@Override
	public List<GetByTsAsgnIdDTO> getByTsAsgnId(Long tsAsgnId) {
		return timesheetAssignmentDetailMapper.getByTsAsgnId(tsAsgnId);
	}

	@Override
	public List<Employee> getOneByEmployeeNum(String employeeNum) {
		return employeeMapper.getOneByEmployeeNum(employeeNum);
	}

	private static long getDays(Date sDate, Date eDate) {
		long days = (sDate.getTime() - eDate.getTime()) / (1000 * 3600 * 24);
		return days;
	}
}
