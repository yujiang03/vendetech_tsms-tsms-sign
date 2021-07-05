package com.vendetech.project.tsms.timesheet.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vendetech.project.tsms.domain.EmpTsAttdAsgnRecord;
import com.vendetech.project.tsms.domain.Employee;
import com.vendetech.project.tsms.domain.Project;

public interface ReportService {

	public List<Map<String, Object>> getCurrentTimesheetReport(Map<String, Object> params);

	public List<Map<String, Object>> getNormalTimesheetRecords(Map<String, Object> params);

	public List<Map<String, Object>> getNormalSumTimesheetRecords(Map<String, Object> params);

	public List<Map<String, Object>> getRealtimeAnalysisReport(Map<String, Object> params);
	
	public XSSFWorkbook exportRealtimeAnalysisReport(List<Map<String, Object>> records) throws Exception;

	public List<Map<String, Object>> getHistoryTimesheetReport(Map<String, Object> params);

	public List<Map<String, Object>> getHistoryTimesheetRecords(Map<String, Object> params);
	
	public List<Map<String, Object>> getHistoryFinanceReport(Map<String, Object> params);

	public XSSFWorkbook exportHistoryFinanceReport(List<Map<String, Object>> records) throws Exception;

//	public Integer getNormalTimesheetReportSize(Map<String, Object> params);

	public List<String> getFreshStatus();

	public List<Employee> findEmployeeByNumName(String employeeNum, String employeeName);

	public List<Project> findProjectByNumName(String projectNum, String projectName);

	public int importUpdateEmpTsAttdAsgnRecord(List<EmpTsAttdAsgnRecord> recordList);

}
