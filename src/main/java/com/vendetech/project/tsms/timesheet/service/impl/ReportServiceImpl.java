package com.vendetech.project.tsms.timesheet.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vendetech.project.tsms.domain.Department;
import com.vendetech.project.tsms.domain.EmpTsAttdAsgnRecord;
import com.vendetech.project.tsms.domain.Employee;
import com.vendetech.project.tsms.domain.JobLog;
import com.vendetech.project.tsms.domain.Project;
import com.vendetech.project.tsms.mapper.JobLogMapper;
import com.vendetech.project.tsms.project.mapper.DepartmentMapper;
import com.vendetech.project.tsms.project.mapper.ProjectMapper;
import com.vendetech.project.tsms.timesheet.mapper.EmpTsAttdAsgnRecordMapper;
import com.vendetech.project.tsms.timesheet.service.ReportService;
import com.vendetech.project.tsms.timesheetAssignment.mapper.TimesheetAssignmentDetailMapper;
import com.vendetech.project.tsms.user.mapper.EmployeeMapper;
import com.vendetech.project.tsms.utils.DateUtil;
import com.vendetech.project.tsms.utils.ExportExcelUtil;
import com.vendetech.project.tsms.utils.JobConstant;
import com.vendetech.project.tsms.utils.dto.ExcelBean;

@Service
public class ReportServiceImpl implements ReportService {

	@Autowired
	EmpTsAttdAsgnRecordMapper empTsAttdAsgnRecordMapper;

	@Autowired
	EmployeeMapper employeeMapper;

	@Autowired
	DepartmentMapper departmentMapper;

	@Autowired
	ProjectMapper projectMapper;
	
	@Autowired
	TimesheetAssignmentDetailMapper timesheetAssignmentDetailMapper;

	@Autowired
	JobLogMapper jobLogMapper;

	@Override
	public List<Map<String, Object>> getCurrentTimesheetReport(Map<String, Object> params) {
		List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
		Long departmentId = (Long) params.get("departmentId");
		String ddDptId = null;
		if (null != departmentId && departmentId > 0) {
			Department dpt = departmentMapper.selectByPrimaryKey(departmentId);
			if (null != dpt) {
				ddDptId = dpt.getDdDepartmentId();
			}
		}
		params.put("ddDptId", ddDptId);
		Long prjDepartmentId = (Long) params.get("projectDepartmentId");
		String ddPrjDptId = null;
		if (null != prjDepartmentId && prjDepartmentId > 0) {
			Department dpt = departmentMapper.selectByPrimaryKey(prjDepartmentId);
			if (null != dpt) {
				ddPrjDptId = dpt.getDdDepartmentId();
			}
		}
		params.put("ddPrjDptId", ddPrjDptId);
		records = empTsAttdAsgnRecordMapper.sumNormalTimesheetList(params);
		return records;
	}

	@Override
	public List<Map<String, Object>> getNormalTimesheetRecords(Map<String, Object> params) {
		List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
		Long departmentId = (Long) params.get("departmentId");
//		List<Department> departments = null;
		String ddDptId = null;
		if (null != departmentId && departmentId > 0) {
			Department dpt = departmentMapper.selectByPrimaryKey(departmentId);
			if (null != dpt) {
//				departments = departmentMapper.getChildrenDeparments(dpt.getDdDepartmentId());
				ddDptId = dpt.getDdDepartmentId();
			}
		} else {
//			departments = departmentMapper.getDepartMentNameList();
		}
		params.put("ddDptId", ddDptId);
		Map<String, Employee> employees = new HashMap<String, Employee>();
//		if (null != departments) {
//			for (Department dpt : departments) {
//				String dptNum = dpt.getDepartmentNum();
//				if (null != dptNum && StringUtils.isNotEmpty(dptNum.trim())) {
//					List<Employee> emps = employeeMapper.getDepartmentEmployees(dptNum);
//					for (Employee emp : emps) {
//						String empNum = emp.getEmployeeNum();
//						if (null == employees.get(empNum)) {
//							employees.put(empNum, emp);
//						}
//					}
//				}
//			}
//		}

		if (null != employees) {
			List<Employee> emps = new ArrayList<Employee>(employees.values());
			for (Employee emp : emps) {
//				params.put("empId", emp.getEmployeeId());
//				List<Map<String, Object>> records = empTsAttdAsgnRecordMapper.selectNormalTimesheetList(params);
//				if (null != records && records.size() > 0) {
//					normalDataReport.addAll(records);
//				}
			}
		}
		records = empTsAttdAsgnRecordMapper.selectNormalTimesheetList(params);
		return records;
	}

	public List<Map<String, Object>> getNormalSumTimesheetRecords(Map<String, Object> params) {
		List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
		Long departmentId = (Long) params.get("departmentId");
		String ddDptId = null;
		if (null != departmentId && departmentId > 0) {
			Department dpt = departmentMapper.selectByPrimaryKey(departmentId);
			if (null != dpt) {
				ddDptId = dpt.getDdDepartmentId();
			}
		}
		params.put("ddDptId", ddDptId);
		Long prjDepartmentId = (Long) params.get("projectDepartmentId");
		String ddPrjDptId = null;
		if (null != prjDepartmentId && prjDepartmentId > 0) {
			Department dpt = departmentMapper.selectByPrimaryKey(prjDepartmentId);
			if (null != dpt) {
				ddPrjDptId = dpt.getDdDepartmentId();
			}
		}
		params.put("ddPrjDptId", ddPrjDptId);
		records = empTsAttdAsgnRecordMapper.sumNormalTimesheetList(params);
		return records;
	}


	@Override
	public List<Map<String, Object>> getHistoryTimesheetReport(Map<String, Object> params) {
		List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
		Long departmentId = (Long) params.get("departmentId");
		String ddDptId = null;
		if (null != departmentId && departmentId > 0) {
			Department dpt = departmentMapper.selectByPrimaryKey(departmentId);
			if (null != dpt) {
				ddDptId = dpt.getDdDepartmentId();
			}
		}
		params.put("ddDptId", ddDptId);
		Long prjDepartmentId = (Long) params.get("projectDepartmentId");
		String ddPrjDptId = null;
		if (null != prjDepartmentId && prjDepartmentId > 0) {
			Department dpt = departmentMapper.selectByPrimaryKey(prjDepartmentId);
			if (null != dpt) {
				ddPrjDptId = dpt.getDdDepartmentId();
			}
		}
		params.put("ddPrjDptId", ddPrjDptId);
		records = empTsAttdAsgnRecordMapper.sumHistoryTimesheetList(params);
		return records;
	}

	@Override	
	public List<Map<String, Object>> getHistoryTimesheetRecords(Map<String, Object> params) {
		List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
		Long departmentId = (Long) params.get("departmentId");
		String ddDptId = null;
		if (null != departmentId && departmentId > 0) {
			Department dpt = departmentMapper.selectByPrimaryKey(departmentId);
			if (null != dpt) {
				ddDptId = dpt.getDdDepartmentId();
			}
		}
		params.put("ddDptId", ddDptId);
		Long prjDepartmentId = (Long) params.get("projectDepartmentId");
		String ddPrjDptId = null;
		if (null != prjDepartmentId && prjDepartmentId > 0) {
			Department dpt = departmentMapper.selectByPrimaryKey(prjDepartmentId);
			if (null != dpt) {
				ddPrjDptId = dpt.getDdDepartmentId();
			}
		}
		params.put("ddPrjDptId", ddPrjDptId);
		records = empTsAttdAsgnRecordMapper.selectHistoryTimesheetList(params);
		return records;
	}

	@Override
	public List<String> getFreshStatus() {
		List<String> result = new ArrayList<String>();
		JobLog dptJob = jobLogMapper.getLatestJobLog(JobConstant.JOB_DEPARTMENT_UPDATE);
		JobLog empJob = jobLogMapper.getLatestJobLog(JobConstant.JOB_EMPLOYEE_UPDATE);
		JobLog attdJob = jobLogMapper.getLatestJobLog(JobConstant.JOB_ATTENDANCE_UPDATE);
		Date dptJobEndtime = dptJob.getJobEndTime();
		result.add("部门数据最近一次同步时间：" + (null == dptJobEndtime ? "" : DateUtil.format(dptJobEndtime, "yyyy-MM-dd HH:mm")));
		Date empJobEndtime = empJob.getJobEndTime();
		result.add("员工数据最近一次同步时间：" + (null == empJobEndtime ? "" : DateUtil.format(empJobEndtime, "yyyy-MM-dd HH:mm")));
		Date attdJobEndtime = attdJob.getJobEndTime();
		result.add("考勤数据最近一次同步时间：" + (null == attdJobEndtime ? "" : DateUtil.format(attdJobEndtime, "yyyy-MM-dd HH:mm")));
		return result;
	}

	@Override
	public List<Employee> findEmployeeByNumName(String employeeNum, String employeeName) {
		return employeeMapper.findEmployeeByNumName(employeeNum, employeeName);
	}

	@Override
	public List<Project> findProjectByNumName(String projectNum, String projectName) {
		return projectMapper.findProjectByNumName(projectNum, projectName);
	}
	
	@Override
	public int importUpdateEmpTsAttdAsgnRecord(List<EmpTsAttdAsgnRecord> recordList) {
		if (null != recordList && recordList.size() > 0) {
			for (EmpTsAttdAsgnRecord record : recordList) {
				EmpTsAttdAsgnRecord rcd = new EmpTsAttdAsgnRecord();
				rcd.setEmployeeNum(record.getEmployeeNum());
//				rcd.setEmployeeId(record.getEmployeeId());
				rcd.setTsDate(record.getTsDate());
				List<EmpTsAttdAsgnRecord> records = empTsAttdAsgnRecordMapper.findEmpTsAttdAsgnRecord(rcd);
				if (null != records && records.size() > 0) {
					record.setId(records.get(0).getId());
					empTsAttdAsgnRecordMapper.updateByPrimaryKeySelective(record);
				} else {
					empTsAttdAsgnRecordMapper.insertSelective(record);
				}
			}
			return recordList.size();
		} else {
			return 0;
		}
	}

	@Override
	public List<Map<String, Object>> getRealtimeAnalysisReport(Map<String, Object> params) {
		List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
		Long departmentId = (Long) params.get("departmentId");
		String ddDptId = null;
		if (null != departmentId && departmentId > 0) {
			Department dpt = departmentMapper.selectByPrimaryKey(departmentId);
			if (null != dpt) {
				ddDptId = dpt.getDdDepartmentId();
			}
		}
		params.put("ddDptId", ddDptId);
		Long prjDepartmentId = (Long) params.get("projectDepartmentId");
		String ddPrjDptId = null;
		if (null != prjDepartmentId && prjDepartmentId > 0) {
			Department dpt = departmentMapper.selectByPrimaryKey(prjDepartmentId);
			if (null != dpt) {
				ddPrjDptId = dpt.getDdDepartmentId();
			}
		}
		params.put("ddPrjDptId", ddPrjDptId);
		records = empTsAttdAsgnRecordMapper.getRealtimeAnalysisReport(params);
		return records;
	}

	@Override
	public XSSFWorkbook exportRealtimeAnalysisReport(List<Map<String, Object>> records)
			throws Exception {
		List<ExcelBean> excel = new ArrayList<>();
		Map<Integer, List<ExcelBean>> map = new LinkedHashMap<>();
		// 设置标题栏
		excel.add(new ExcelBean("员工工号", "employeeNum", 0));
		excel.add(new ExcelBean("员工姓名", "employeeName", 0));
		excel.add(new ExcelBean("员工所属公司", "employeeCompany", 0));
		excel.add(new ExcelBean("员工所属部门编号", "employeeDepartmentNum", 0));
		excel.add(new ExcelBean("员工所属部门名称", "employeeDepartmentName", 0));
		excel.add(new ExcelBean("项目状态", "assignType", 0));
		excel.add(new ExcelBean("项目编号", "assignProjectNum", 0));
		excel.add(new ExcelBean("项目名称", "assignProjectName", 0));
		excel.add(new ExcelBean("项目所属部门编号", "projectDepartmentNum", 0));
		excel.add(new ExcelBean("项目所属部门名称", "projectDepartmentName", 0));
		excel.add(new ExcelBean("项目所属公司", "projectCompany", 0));
		excel.add(new ExcelBean("月份", "tsMonth", 0));
		excel.add(new ExcelBean("各项目投入工时", "inputHours", 0));
		excel.add(new ExcelBean("员工当月实际总工时", "totalInputHours", 0));
		excel.add(new ExcelBean("配置总工时", "totalAssignHours", 0));
		excel.add(new ExcelBean("项目投入比例 = 各项目投入工时/配置总工时", "proportion", 0));
		excel.add(new ExcelBean("部门不一致", "departmentDiff", 0));
		excel.add(new ExcelBean("公司不一致", "companyDiff", 0));
		excel.add(new ExcelBean("合同状态", "contractStatus", 0));
		excel.add(new ExcelBean("分配人工号", "assigner", 0));
		excel.add(new ExcelBean("分配人姓名", "assignerName", 0));
		map.put(0, excel);
		String sheetName = "实时分析表";
		// 调用ExcelUtil方法
		XSSFWorkbook xssfWorkbook = null;
		xssfWorkbook = ExportExcelUtil.createExcelFile(HashMap.class, records, map, sheetName);
		System.out.println(xssfWorkbook);
		return xssfWorkbook;
	}

	@Override
	public List<Map<String, Object>> getHistoryFinanceReport(Map<String, Object> params) {
		List<Map<String, Object>> records = new ArrayList<Map<String, Object>>();
		Long departmentId = (Long) params.get("departmentId");
		String ddDptId = null;
		if (null != departmentId && departmentId > 0) {
			Department dpt = departmentMapper.selectByPrimaryKey(departmentId);
			if (null != dpt) {
				ddDptId = dpt.getDdDepartmentId();
			}
		}
		params.put("ddDptId", ddDptId);
		Long prjDepartmentId = (Long) params.get("projectDepartmentId");
		String ddPrjDptId = null;
		if (null != prjDepartmentId && prjDepartmentId > 0) {
			Department dpt = departmentMapper.selectByPrimaryKey(prjDepartmentId);
			if (null != dpt) {
				ddPrjDptId = dpt.getDdDepartmentId();
			}
		}
		params.put("ddPrjDptId", ddPrjDptId);
		records = empTsAttdAsgnRecordMapper.getHistoryFinanceReport(params);
		return records;
	}

	@Override
	public XSSFWorkbook exportHistoryFinanceReport(List<Map<String, Object>> records)
			throws Exception {
		List<ExcelBean> excel = new ArrayList<>();
		Map<Integer, List<ExcelBean>> map = new LinkedHashMap<>();
		// 设置标题栏
		excel.add(new ExcelBean("员工工号", "employeeNum", 0));
		excel.add(new ExcelBean("员工姓名", "employeeName", 0));
		excel.add(new ExcelBean("员工所属公司", "employeeCompany", 0));
		excel.add(new ExcelBean("员工所属部门编号", "employeeDepartmentNum", 0));
		excel.add(new ExcelBean("员工所属部门名称", "employeeDepartmentName", 0));
		excel.add(new ExcelBean("项目状态", "assignType", 0));
		excel.add(new ExcelBean("项目编号", "assignProjectNum", 0));
		excel.add(new ExcelBean("项目名称", "assignProjectName", 0));
		excel.add(new ExcelBean("项目所属部门编号", "projectDepartmentNum", 0));
		excel.add(new ExcelBean("项目所属部门名称", "projectDepartmentName", 0));
		excel.add(new ExcelBean("项目所属公司", "projectCompany", 0));
		excel.add(new ExcelBean("月份", "tsMonth", 0));
		excel.add(new ExcelBean("各项目投入工时", "inputHours", 0));
		excel.add(new ExcelBean("员工当月实际总工时", "totalInputHours", 0));
		excel.add(new ExcelBean("项目投入比例 = 各项目投入工时/员工当月实际总工时", "proportion", 0));
		excel.add(new ExcelBean("部门不一致", "departmentDiff", 0));
		excel.add(new ExcelBean("公司不一致", "companyDiff", 0));
		excel.add(new ExcelBean("合同状态", "contractStatus", 0));
		excel.add(new ExcelBean("分配人工号", "assigner", 0));
		excel.add(new ExcelBean("分配人姓名", "assignerName", 0));
		map.put(0, excel);
		String sheetName = "实时分析表";
		// 调用ExcelUtil方法
		XSSFWorkbook xssfWorkbook = null;
		xssfWorkbook = ExportExcelUtil.createExcelFile(HashMap.class, records, map, sheetName);
		System.out.println(xssfWorkbook);
		return xssfWorkbook;
	}

}
