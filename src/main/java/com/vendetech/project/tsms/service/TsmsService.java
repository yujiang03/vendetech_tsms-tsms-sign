package com.vendetech.project.tsms.service;

import java.util.List;
import java.util.Map;

import com.dingtalk.api.response.OapiSmartworkHrmEmployeeListdimissionResponse.EmpDimissionInfoVo;
import com.vendetech.project.tsms.domain.Calendars;
import com.vendetech.project.tsms.domain.DdAttendanceData;
import com.vendetech.project.tsms.domain.DdDepartmentTmp;
import com.vendetech.project.tsms.domain.DdHrmDataTmp;
import com.vendetech.project.tsms.domain.DdLog;
import com.vendetech.project.tsms.domain.DdUserTmp;
import com.vendetech.project.tsms.domain.Employee;
import com.vendetech.project.tsms.domain.JobLog;

/**
 * 
 * 
 * @author vendetech
 */
public interface TsmsService {

	public int insertDdDepartmentTmp(DdDepartmentTmp dpt);

	public void callDepartmentRefresh();

	public int insertJobLog(String type);

	public int insertJobLog(JobLog jobLog);

	public int finishJobLog(JobLog log);

	public int updateJobLog(JobLog log);

	public int insertDdUserTmp(DdUserTmp duser);

	public void callUserRefresh();

	public int insertUpdateDdAttendanceData(DdAttendanceData attendance);

	public int updateDdAttendanceData(DdAttendanceData attendance);

	public int insertDdHrmDataTmp(DdHrmDataTmp hrmData);

	public void callEmpHrmDataRefresh();

	public void callEmpTsAttdAsgnRecordRefresh(String startDate);

//	public void freshAllEmpTsAttdAsgnRecordByMonth(int year, int month);
	public void freshAllEmpTsAttdAsgnRecordByMonth(String fromDate, String toDate);

	public Employee selectByEmployeeDdUserId(String dduserid);

	public int insertDdLog(DdLog log);

	public int insertCalendar(Calendars cal);

	/**
	 * @param param
	 */
	public void createEmployeeAccount(Map<String, Object> param);

	public void snapshotReportData();

	public void batchUpdateLastWorkDate(List<EmpDimissionInfoVo> ddUserIdList);

	public void truncateDdUserTmp();

}
