package com.vendetech.project.tsms.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dingtalk.api.response.OapiSmartworkHrmEmployeeListdimissionResponse.EmpDimissionInfoVo;
import com.vendetech.project.tsms.domain.Calendars;
import com.vendetech.project.tsms.domain.DdAttendanceData;
import com.vendetech.project.tsms.domain.DdDepartmentTmp;
import com.vendetech.project.tsms.domain.DdHrmDataTmp;
import com.vendetech.project.tsms.domain.DdLog;
import com.vendetech.project.tsms.domain.DdUserTmp;
import com.vendetech.project.tsms.domain.Employee;
import com.vendetech.project.tsms.domain.JobLog;
import com.vendetech.project.tsms.mapper.DdDepartmentTmpMapper;
import com.vendetech.project.tsms.mapper.DdHrmDataTmpMapper;
import com.vendetech.project.tsms.mapper.DdLogMapper;
import com.vendetech.project.tsms.mapper.DdUserTmpMapper;
import com.vendetech.project.tsms.mapper.JobLogMapper;
import com.vendetech.project.tsms.project.mapper.ProjectMapper;
import com.vendetech.project.tsms.service.TsmsService;
import com.vendetech.project.tsms.timesheet.mapper.CalendarsMapper;
import com.vendetech.project.tsms.timesheet.mapper.DdAttendanceDataMapper;
import com.vendetech.project.tsms.timesheet.mapper.EmpTsAttdAsgnRecordMapper;
import com.vendetech.project.tsms.user.mapper.EmployeeMapper;

@Service
public class TsmsServiceImpl implements TsmsService {

	@Autowired
	private JobLogMapper jobLogMapper;
	@Autowired
	private DdLogMapper ddLogMapper;
	@Autowired
	private DdDepartmentTmpMapper ddDepartmentTmpMapper;
	@Autowired
	private DdUserTmpMapper ddUserTmpMapper;
	@Autowired
	private EmployeeMapper employeeMapper;
	@Autowired
	private DdAttendanceDataMapper ddAttendanceDataMapper;
	@Autowired
	private EmpTsAttdAsgnRecordMapper empTsAttdAsgnRecordMapper;
	@Autowired
	private CalendarsMapper calendarsMapper;
	@Autowired
	private DdHrmDataTmpMapper ddHrmDataTmpMapper;
	@Autowired
	private ProjectMapper projectMapper;

	@Override
	public void callDepartmentRefresh() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("msg", new String());
		ddDepartmentTmpMapper.callRefreshDepartments(params);
		String msg = (String) params.get("msg");
		System.out.println(msg);
//		String msg = (String)params.get("msg");
	}

	@Override
	public int insertDdDepartmentTmp(DdDepartmentTmp dpt) {
		return ddDepartmentTmpMapper.insertSelective(dpt);
	}

	@Override
	public void callUserRefresh() {
		ddUserTmpMapper.callRefreshUsers();
	}

	
	@Override
	public void snapshotReportData() {
		projectMapper.snapshotReportData();
	}
	
	@Override
	public int insertDdUserTmp(DdUserTmp duser) {
		return ddUserTmpMapper.insertSelective(duser);
	}

	/**
	 * @param param
	 */
    @Override
	public void createEmployeeAccount(Map<String, Object> param) {
		ddUserTmpMapper.createEmployeeAccount(param);
	}

	@Override
	public int insertJobLog(String type) {
		JobLog jobLog = new JobLog(type);
		jobLog.setJobName(type);
		jobLog.setJobStatus(0);
		jobLog.setJobDate(new Date());
		return jobLogMapper.insertSelective(jobLog);
	}

	@Override
	public int insertDdLog(DdLog log) {
		return ddLogMapper.insertSelective(log);
	}

//	@Override
	public int insertNewJobLog(String type) {
		JobLog jobLog = new JobLog(type);
//		jobLog.setJobName(type);
//		jobLog.setJobStatus(0);
//		jobLog.setJobDate(new Date());
		return jobLogMapper.insertSelective(jobLog);
	}

	@Override
	public int insertJobLog(JobLog jobLog) {
//		jobLog.setJobStatus(0);
//		jobLog.setJobDate(new Date());
		return jobLogMapper.insertSelective(jobLog);
	}

	@Override
	public int finishJobLog(JobLog log) {
		String jobMsg = log.getJobMsg();
		if(null != jobMsg && jobMsg.length() > 2000) {
			log.setJobMsg(jobMsg.substring(0, 2000));
		}
		return jobLogMapper.finishByPrimaryKeySelective(log);
	}

	@Override
	public int updateJobLog(JobLog log) {
		String jobMsg = log.getJobMsg();
		if(null != jobMsg && jobMsg.length() > 2000) {
			log.setJobMsg(jobMsg.substring(0, 2000));
		}
		return jobLogMapper.insertSelective(log);
	}

	@Override
	public Employee selectByEmployeeDdUserId(String ddUserId) {
		return employeeMapper.selectByEmployeeDdUserId(ddUserId);
	}

	@Override
	public int insertUpdateDdAttendanceData(DdAttendanceData attendance) {
		int find = ddAttendanceDataMapper.selectByEmpNumDate(attendance.getEmployeeNum(),
				attendance.getAttendanceDate());
		if (find == 0) {
			return ddAttendanceDataMapper.insert(attendance);
		} else {
			return ddAttendanceDataMapper.updateByEmpNumDatesSelective(attendance);
		}
	}

	@Override
	public int updateDdAttendanceData(DdAttendanceData attendance) {
		return ddAttendanceDataMapper.updateByDdUseridDatesSelective(attendance);
	}

	@Override
	public int insertDdHrmDataTmp(DdHrmDataTmp hrmData) {
		return ddHrmDataTmpMapper.insertSelective(hrmData);
	}

	@Override
	public void callEmpHrmDataRefresh() {
		ddHrmDataTmpMapper.callRefreshEmpHrmData();
	}

	@Override
	public void callEmpTsAttdAsgnRecordRefresh(String startDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("startDate", startDate);
		params.put("msg", new String());
		empTsAttdAsgnRecordMapper.freshAllEmpTsAttdAsgnRecord(params);
		String msg = (String) params.get("msg");
		System.out.println(msg);
	}

	@Override
	public void freshAllEmpTsAttdAsgnRecordByMonth(String fromDate, String toDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fromDate", fromDate);
		params.put("toDate", toDate);
		params.put("msg", new String());
		empTsAttdAsgnRecordMapper.freshAllEmpTsAttdAsgnRecordByMonth(params);
		String msg = (String) params.get("msg");
		System.out.println(msg);
	}

	@Override
	public int insertCalendar(Calendars cal) {
		return calendarsMapper.insertCalendars(cal);
	}
	
	@Override
	public void batchUpdateLastWorkDate(List<EmpDimissionInfoVo> ddUserIdList) {
		empTsAttdAsgnRecordMapper.batchUpdateLastWorkDate(ddUserIdList);
	}

	@Override
	public void truncateDdUserTmp() {
		ddUserTmpMapper.truncateDdUserTmp();
	}
}
