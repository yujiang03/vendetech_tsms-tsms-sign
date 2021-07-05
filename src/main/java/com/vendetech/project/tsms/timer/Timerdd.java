package com.vendetech.project.tsms.timer;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.vendetech.project.tsms.domain.*;
import com.vendetech.project.tsms.service.IDdHrmDataSignService;
import com.vendetech.project.tsms.user.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiDepartmentGetRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeListdimissionRequest;
import com.dingtalk.api.request.OapiSmartworkHrmEmployeeQuerydimissionRequest;
import com.dingtalk.api.response.OapiDepartmentGetResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeListdimissionResponse;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeListdimissionResponse.EmpDimissionInfoVo;
import com.dingtalk.api.response.OapiSmartworkHrmEmployeeQuerydimissionResponse;
import com.taobao.api.ApiException;
import com.vendetech.project.dingtalk.ali.AuthHelper;
import com.vendetech.project.dingtalk.ali.Methods;
import com.vendetech.project.dingtalk.config.Constant;
import com.vendetech.project.dingtalk.utils.DdSentMsgThead;
import com.vendetech.project.tsms.service.TsmsService;
import com.vendetech.project.tsms.timesheet.service.DashboardService;
import com.vendetech.project.tsms.user.service.UserService;
import com.vendetech.project.tsms.utils.DateUtil;
import com.vendetech.project.tsms.utils.JobConstant;
import com.vendetech.project.tsms.utils.MailSentThread;

/**
 * 
 * 
 * @author vendetech
 */
@Component
@EnableScheduling
@Controller
@EnableAsync
public class Timerdd {

	protected final Logger logger = LoggerFactory.getLogger(Timerdd.class);

	@Autowired
	private TsmsService tsmsService;
	@Autowired
	private UserService userService;
	@Autowired
	private DashboardService dashboardService;
    @Autowired
    private EmployeeService employeeService;
	@Autowired
    private IDdHrmDataSignService ddHrmDataSignService;

	/*
	 * @Async //异动定时任务取消
	 * 
	 * @Scheduled(cron = "0 0 10 13 * ?") // 每月13日上午10:00触发
	 * 
	 * @Scheduled(cron = "0 0 10 10 * ?") // 每月10日上午10:00触发
	 * 
	 * @Scheduled(cron = "0 0 10 5 * ?") // 每月5日上午10:00触发
	 */


	/**
	 * Daily 从钉钉全量更新部门数据到TSMS系统。如果发现新部门则插入部门表，如果发现有部门编号没了则逻辑删除，其余的更新。
	 */
	@Async
	@Scheduled(cron = "0 0 22 * * ?") // 每天22点0分
	public void updateDepartment() throws ParseException {
		JobLog log = new JobLog(JobConstant.JOB_DEPARTMENT_UPDATE);
		tsmsService.insertJobLog(log);
//		Long id = log.getJobLogId();
//		System.out.println(id+"");
		try {
			List<String> departments = Methods.getDepartments();
			if (null != departments && departments.size() > 0) {
				for (String dptId : departments) {
					DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/department/get");
					OapiDepartmentGetRequest request = new OapiDepartmentGetRequest();
					request.setId(dptId);
					request.setHttpMethod("GET");
					OapiDepartmentGetResponse response = client.execute(request, AuthHelper.getAccessToken());
					if (0 == response.getErrcode()) {
						try {
							DdDepartmentTmp dpt = new DdDepartmentTmp();
							dpt.setDdDepartmentId(response.getId());
							String fullname = response.getName();
							boolean hasBlank = fullname.contains(" ");
							if (hasBlank) {
								dpt.setDdDepartmentFullname(fullname.substring(0, fullname.lastIndexOf(" ")));
							} else {
								dpt.setDdDepartmentFullname(fullname);
							}
							if (!"".equals(fullname)) {
								dpt.setDdDepartmentCode(fullname.substring(fullname.lastIndexOf(" ") + 1));
								Long parentid = response.getParentid();
								if (null != parentid) {
									dpt.setDdDepartmentParentId(
											null == parentid || "".equals(parentid) ? null : parentid);
									String[] mgrIds = response.getDeptManagerUseridList().split("\\|");
									String mgrIdJoin = String.join(",", mgrIds);
									dpt.setDdDepartmentOwnerId(
											null == mgrIds || "".equals(response.getDeptManagerUseridList()) ? null
													: mgrIdJoin);

									tsmsService.insertDdDepartmentTmp(dpt);
								}
							}
						} catch (Exception e) {
							logger.error(e.getMessage());
							DdLog ddLog = new DdLog();
							ddLog.setStatus(1);
							ddLog.setPostName("部门表每日job");
							ddLog.setParameter(response.getBody());
							ddLog.setSendTime(new Date());
							ddLog.setErrorCode(response.getErrorCode());
							ddLog.setErrorMessage(e.getMessage());
							tsmsService.insertDdLog(ddLog);
							log.setJobStatus(3);
						}
					} else {
						log.setJobStatus(3);
						log.setJobMsg(response.getErrmsg());
						tsmsService.finishJobLog(log);
					}
				}
				tsmsService.callDepartmentRefresh();
			}
		} catch (ApiException ae) {
			handelApiException(log, ae);
		} catch (Exception e) {
			handelException(log, e);
		}
		if(null == log.getJobStatus() || 0 == log.getJobStatus()) {
			log.setJobStatus(1);
		}
		tsmsService.finishJobLog(log);
	}

	/**
	 * Daily 从钉钉全量更新员工数据到TSMS系统。如果发现新员工则插入员工表，如果发现有员工在钉钉数据里不见了则逻辑删除，其余的更新。
	 */
	@Async
	@Scheduled(cron = "0 30 7,9,11,13,15,17,19 * * ? ") // 每天12,23点10分
	public void updateEmployee() throws ApiException {
		JobLog log = new JobLog(JobConstant.JOB_EMPLOYEE_UPDATE);
		tsmsService.insertJobLog(log);
		try {
			List<Map<String, Object>> userList = Methods.getUsers();
			// tsmsService.truncateDdUserTmp();
			for (Map<String, Object> dUser : userList) {
			    if (dUser.get("empId") == null) {
			        continue;
                }
				DdUserTmp tUser = new DdUserTmp();
				tUser.setDdActiveStatus(dataAction(dUser.get("status")));
				tUser.setDdUserId(dataAction(dUser.get("userId")));
				tUser.setDdUserName(dataAction(dUser.get("name")));
				tUser.setDdUserNum(dataAction(dUser.get("empId")));
				tUser.setDdUserOrgemail(dataAction(dUser.get("email")));
				tUser.setDdUserAvatar(dataAction(dUser.get("avatar")));
				tUser.setDdDepartmentList(dataAction(dUser.get("depts")));
				tUser.setDdUserMobile(dataAction(dUser.get("mobile")));
				tUser.setDdUserCompany(dataAction(dUser.get("company")));
				tsmsService.insertDdUserTmp(tUser);
            }
			tsmsService.callUserRefresh();
		} catch (ApiException ae) {
			handelApiException(log, ae);
		} catch (Exception e) {
			handelException(log, e);
		}
		log.setJobStatus(1);
		tsmsService.finishJobLog(log);
	}

	/**
	 * 项目匹配工时数据按月归档
	 */
//	@Async
//	@Scheduled(cron = "0 0 0 2 * ?") // 每月2号 0时跑
	public void snapshotReportData() {
		JobLog log = new JobLog(JobConstant.JOB_MONTH_REPORT_SNAPSHOT);
		tsmsService.insertJobLog(log);
		try {
			tsmsService.snapshotReportData();
		} catch (Exception e) {
			handelException(log, e);

		}
		log.setJobStatus(1);
		tsmsService.finishJobLog(log);
	}

	private String dataAction(Object ob) {
		if (null == ob || "".equals(ob.toString().trim()) || "null".equals(ob.toString().trim().toLowerCase())) {
			return null;
		}
		return ob.toString();
	}

	/**
	 * Daily 从钉钉全量更新员工考勤数据到TSMS系统。如果是15之前，更新上月和本月的考勤记录，如果是15日或15日之后，只更新本月考勤记录。
	 * 根据员工号和日期检查，发现新员工考勤则插入，其余的更新。
	 */
//	@Async
//	@Scheduled(cron = "0 0 3 * * ?") // 每天3点0分
	public void updateAttendance() throws ApiException {
		JobLog log = new JobLog(JobConstant.JOB_ATTENDANCE_UPDATE);
		tsmsService.insertJobLog(log);
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DATE);
		String fromDate = "";
		String toDate = "";
		if (day <= 14) {
			int fromMonth = (month == 1 ? 12 : month - 1);
			int fromYear = (month == 1 ? year - 1 : year);
			fromDate = fromDate + fromYear + "-" + fromMonth + "-01 00:00:00";
		} else {
			fromDate = fromDate + year + "-" + month + "-01 00:00:00";
		}
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			toDate = toDate + year + "-" + month + "-31 00:00:00";
		} else if (month == 2) {
			if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
				toDate = toDate + year + "-" + month + "-29 00:00:00";
			} else {
				toDate = toDate + year + "-" + month + "-28 00:00:00";
			}
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			toDate = toDate + year + "-" + month + "-30 00:00:00";
		}
		try {
			List<String> users = Methods.getUserids();
			List<Map<String, Object>> attendances = Methods.getAttendances(users, fromDate, toDate);
//			List<Map<String, Object>> attendances = Methods.getAttendances(users, "2020-03-01 00:00:00", "2020-03-15 00:00:00");
			System.out.println("users qty" + users.size());
			for (Map<String, Object> attd : attendances) {
				DdAttendanceData ddAttd = new DdAttendanceData();
				String dduserid = "" + attd.get("userid");
				ddAttd.setDdUserid(dduserid);
				Employee emp = tsmsService.selectByEmployeeDdUserId(dduserid);
				if (null != emp && StringUtils.isNotBlank(emp.getEmployeeNum())) {	// 找不到员工号的话，不同步过来
//				System.out.println(attd.get("attdDate"));
					ddAttd.setEmployeeNum(emp.getEmployeeNum());
					String result = "" + attd.get("attdResult");
					ddAttd.setAttendanceDate((Date) attd.get("attdDate"));
					ddAttd.setResult(result);
					ddAttd.setDutyDay(null == attd.get("dutyDay") ? new BigDecimal(0.0)
							: BigDecimal.valueOf(Double.valueOf("" + attd.get("dutyDay"))));
					ddAttd.setAttendDay(null == attd.get("attdDay") ? new BigDecimal(0.0)
							: BigDecimal.valueOf(Double.valueOf("" + attd.get("attdDay"))));
					Number allowedOtHours = Float.parseFloat("" + attd.get("allowedOtHours")) * 10;
					ddAttd.setAllowedOverHours(allowedOtHours.intValue() / 10);
				}
				tsmsService.insertUpdateDdAttendanceData(ddAttd);
			}
			
//			List<String> users = new ArrayList<String>();
//			users.add("0331092735471898");
//			List<Map<String, Object>> leaves = Methods.getLeaves(users, "2020-02-18 00:00:00", "2020-03-01 00:00:00");
			List<Map<String, Object>> leaves = Methods.getLeaves(users, fromDate, toDate);
			for (Map<String, Object> leave : leaves) {
				DdAttendanceData ddAttd = new DdAttendanceData();
				String dduserid = "" + leave.get("userid");
				ddAttd.setDdUserid(dduserid);
				ddAttd.setAttendanceDate((Date) leave.get("attdDate"));
				Integer exchangeOffHours = (null == leave.get("exchangeOffHours") ? 0
						: (Integer) leave.get("exchangeOffHours"));
				Integer annualLeaveHours = (null == leave.get("annualLeaveHours") ? 0
						: (Integer) leave.get("annualLeaveHours"));
				Integer absenceLeaveHours = (null == leave.get("absenceLeaveHours") ? 0
						: (Integer) leave.get("absenceLeaveHours"));
				Integer sickLeaveHours = (null == leave.get("sickLeaveHours") ? 0
						: (Integer) leave.get("sickLeaveHours"));
				ddAttd.setLeaveHours(exchangeOffHours + annualLeaveHours + absenceLeaveHours + sickLeaveHours);
				tsmsService.updateDdAttendanceData(ddAttd);
			}
		} catch (ApiException ae) {
			handelApiException(log, ae);
		} catch (Exception e) {
			handelException(log, e);
		}
		log.setJobStatus(1);
		tsmsService.finishJobLog(log);
	}

	private void handelApiException(JobLog log, ApiException ae) {
		logger.error(ae.getMessage());
		ae.printStackTrace();
		log.setJobStatus(2);
		log.setJobMsg(ae.getErrMsg());
		tsmsService.finishJobLog(log);
	}

	private void handelException(JobLog log, Exception e) {
		logger.error(e.getMessage(), e);
		e.printStackTrace();
		log.setJobStatus(2);
		log.setJobMsg(e.getMessage());
		tsmsService.finishJobLog(log);
	}

	/**
	 * Daily 从钉钉全量更新员工花名册数据到TSMS系统。
	 * 根据员工号检查，发现新员工则插入，其余的更新。
	 */
	@Async
	@Scheduled(cron = "0 30 8,10,12,14,16,18,20 * * ?") // 每天23点30分
	// @Scheduled(fixedRate = 60000000) // 每天23点30分
	public void updateHrmData() throws ApiException {
		JobLog log = new JobLog(JobConstant.JOB_HRM_DATA_UPDATE);
		tsmsService.insertJobLog(log);
		try {
			List<String> users = Methods.getUserids();
			System.out.println("users qty" + users.size());
			List<DdHrmDataSign> ddHrmData = Methods.getHrmData(users);
			System.out.println("ddHrmData qty" + ddHrmData.size());
			for (DdHrmDataSign hrm : ddHrmData) {
				DdHrmDataTmp data = new DdHrmDataTmp();
				String ddUserId = "" + hrm.getDdUserId();
				Employee emp = tsmsService.selectByEmployeeDdUserId(ddUserId);
				if (null != emp && StringUtils.isNotBlank(emp.getEmployeeNum())) {
					data.setDdUserId(ddUserId);
					data.setDdEmpNum(emp.getEmployeeNum());
					data.setDdTsExclFlag(hrm.getDdTsExclFlag()); // tsExclFlg 项目分配排除标识
					data.setDdMainDeptId(String.valueOf(hrm.getDdMainDeptId()));
					data.setDdMainDeptNum(hrm.getDdMainDept());
					String joinDate = hrm.getDdConfirmJoinTime();
					Date entryDate = null;
					if (null != joinDate) {
						entryDate = DateUtil.toDate(joinDate, DateUtil.PATTERN_DATE);
					}
					data.setDdJoinDate(entryDate);
					data.setDdCompanyName(hrm.getDdCompanyName());
					tsmsService.insertDdHrmDataTmp(data);
				}
			}

			// 批量更新花名册信息，签约字段
            int r = ddHrmDataSignService.truncateHrmDataSign();
            logger.info("truncateHrmDataSign：成功更新" + r + "条记录");
            int n = ddHrmDataSignService.batchInsertHrmDataSign(ddHrmData);
			logger.info("batchInsertHrmDataSign：成功更新" + n + "条记录");

			// 批量更新员工信息
			tsmsService.callEmpHrmDataRefresh();

			// 根据员工表批量更新系统用户
            userService.updateSysUserByTask();

		} catch (ApiException ae) {
			handelApiException(log, ae);
		} catch (Exception e) {
			handelException(log, e);
		}
		log.setJobStatus(1);
		tsmsService.finishJobLog(log);
	}

	// @Scheduled(fixedRate = 600000000)
	public void updateSysUserByTask() {
		userService.updateSysUserByTask();
	}

	/**
	 * Daily 从钉钉全量更新员工离职日期
	 */
//	@Async
//	@Scheduled(cron = "0 0 4 * * ?") // 每天4点
	public void freshEmpOffboardingDate() throws ApiException {
		JobLog log = new JobLog(JobConstant.JOB_FRESH_EMP_OFFBOARDING_DATE);
		tsmsService.insertJobLog(log);
		try {
			freshEmpOffboardingDateJob(0L);
		} catch (Exception e) {
			handelException(log, e);
		}
		log.setJobStatus(1);
		tsmsService.finishJobLog(log);
	}
	
	
	/**
	 * 递归刷新离职时间
	 */
	public void freshEmpOffboardingDateJob(Long offset) throws ApiException {
		DingTalkClient client = new DefaultDingTalkClient(
				"https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/querydimission");
		OapiSmartworkHrmEmployeeQuerydimissionRequest req = new OapiSmartworkHrmEmployeeQuerydimissionRequest();
		req.setOffset(offset);
		req.setSize(50L);
		OapiSmartworkHrmEmployeeQuerydimissionResponse response = client.execute(req, AuthHelper.getAccessToken());
//		System.out.println(response.getBody());
		if (0 == response.getErrcode()) {
			List<String> ddUserIdList = response.getResult().getDataList();
			if (ddUserIdList.size() > 0) {
				System.out.println(ddUserIdList.size());
				DingTalkClient client2 = new DefaultDingTalkClient(
						"https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/listdimission");
				OapiSmartworkHrmEmployeeListdimissionRequest req2 = new OapiSmartworkHrmEmployeeListdimissionRequest();
				req2.setUseridList(ddUserIdList.toString().replace(" ", "").replace("[", "").replace("]", ""));
				OapiSmartworkHrmEmployeeListdimissionResponse response2 = client2.execute(req2,
						AuthHelper.getAccessToken());
				if (0 == response2.getErrcode()) {
					List<EmpDimissionInfoVo> ddUserIdList2 = response2.getResult();
					tsmsService.batchUpdateLastWorkDate(ddUserIdList2);
				} else {
					throw new ApiException(response2.getErrmsg());
				}
			}
			Long nextOffset = response.getResult().getNextCursor();
			if (null != nextOffset && nextOffset != 0) {
				freshEmpOffboardingDateJob(nextOffset);
			}
		} else {
			throw new ApiException(response.getErrmsg());
		}
	}
	
	/**
	 * Daily 从钉钉全量更新员工考勤数据到TSMS系统。如果是15之前，更新上月和本月的考勤记录，如果是15日或15日之后，只更新本月考勤记录。
	 * 根据员工号和日期检查，发现新员工考勤则插入，其余的更新。
	 */
//	@Async
//	@Scheduled(cron = "0 30 5 * * ?") // 每天5点30分
	public void freshAttendanceMatch() throws ApiException {
		JobLog log = new JobLog(JobConstant.JOB_ATTENDANCE_MATCH_REFRESH);
		tsmsService.insertJobLog(log);
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DATE);
		String fromYear = "" + year;
		String fromMonth = (month < 10 ? "0" + month : "" + month);
		String toYear = "" + (month == 12 ? year + 1 : year);
		String toMonth = (month == 12 ? "01" : (month < 9 ? "0" + (month + 1) : "" + (month + 1)));
		try {
			if (day <= 14) {
				if (month == 1) {
					fromYear = "" + (year - 1);
					fromMonth = "12";
				} else {
					fromMonth = (month < 11 ? "0" + (month - 1) : "" + (month - 1));
				}
			}
			System.out.println(fromYear + "-" + fromMonth + "-01");
			System.out.println(toYear + "-" + toMonth + "-01");
			tsmsService.freshAllEmpTsAttdAsgnRecordByMonth(fromYear + "-" + fromMonth + "-01",
					toYear + "-" + toMonth + "-01");
//			tsmsService.freshAllEmpTsAttdAsgnRecordByMonth(year, month);
			System.out.println("END:" + new Date());
		} catch (Exception e) {
			handelException(log, e);
		}
		log.setJobStatus(1);
		tsmsService.finishJobLog(log);
	}

//	@Scheduled(cron = "0 0 9 ? * MON")
//	@Scheduled(cron = "0 0 9 23-31 * ?")
	public void sendNotification() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH) + 1;
		int day = now.get(Calendar.DATE);
		List<Map<String, Object>> users = userService.selectUserListByRole(3L);	// 项目经理PM
		List<Map<String, Object>> bmUsers = userService.selectUserListByRole(5L); // 事业部经理BM
		users.addAll(bmUsers);
		for (Map<String, Object> user : users) {
			String ddUserid = ""+user.get("ddUserid");
			String email = ""+user.get("email");
//			ddUserid = "160635502121412782";
//			email = "zhouzhiyu@vendetech.com";
			if (day < 23) {
				if (null != ddUserid) {
					DdSentMsgThead ddSentMsgThead = new DdSentMsgThead(ddUserid, Constant.TIME_SHEET_NOTIFICATION);
					ddSentMsgThead.start();
				}
				if (null != email) {
					MailSentThread mailThread = new MailSentThread(email, "工时分配提醒", Constant.TIME_SHEET_NOTIFICATION);
			        mailThread.start();
				}
			} else {
				Map<String, Object> param = new HashMap<String, Object>();
				param.put("mgrNum", ""+user.get("empNum"));
				param.put("year", ""+year);
				param.put("month", ""+month);
				List<EmpTsAttdAsgnRecord> unmatchRecords = dashboardService.getUnmatchListByMonth(param);
				if (null != unmatchRecords && unmatchRecords.size() > 0) {
					if (null != ddUserid) {
						DdSentMsgThead ddSentMsgThead = new DdSentMsgThead(ddUserid, Constant.ASSIGN_NOTIFICATION);
						ddSentMsgThead.start();
					}
					if (null != email) {
						MailSentThread mailThread = new MailSentThread(email, "工时分配提醒", Constant.ASSIGN_NOTIFICATION);
				        mailThread.start();
					}
				}
			}
		}
	}

	@Scheduled(fixedRate = 600000) // 10分钟跑一次
    public void resetUserErrorCount() {
		userService.resetUserErrorCount();
	}

	/**
	 * Yearly 日历获取
	 * 
	 * @throws ParseException
	 */
	@Scheduled(cron = "0 11 0 1 1 ?") // 每年1月1日 零点 因为网络限制 不能直接获取数据量过大。
	public void getCalendar() throws ParseException {
		Calendar calendar = Calendar.getInstance();// 当前日期
		int year = calendar.get(Calendar.YEAR);
		Calendars calendars = new Calendars();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 1; i < 13; i++) {

			if (i == 1 || i == 3 || i == 5 || i == 7 || i == 8 || i == 10 || i == 12) {
				for (int j = 1; j < 32; j++) {
					String day = "", type = "", month = "", days = "";

					if (i < 10) {
						month = "0" + i;
					} else {
						month = "" + i;
					}
					if (j < 10) {
						days = "0" + j;
					} else {
						days = "" + j;
					}
					day = year + "-" + month + "-" + days;
					type = Methods.getHolidays(year + "" + month + "" + days);
					calendars.setWorkday(sdf.parse(day));
					calendars.setType(Integer.parseInt(type.substring(type.length() - 5, type.length() - 4)));
					tsmsService.insertCalendar(calendars);
				}
			} else if (i == 2) {
				if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
					for (int j = 1; j < 30; j++) {
						String day = "", type = "", month = "", days = "";

						if (i < 10) {
							month = "0" + i;
						} else {
							month = "" + i;
						}
						if (j < 10) {
							days = "0" + j;
						} else {
							days = "" + j;
						}
						day = year + "-" + month + "-" + days;
						type = Methods.getHolidays(year + "" + month + "" + days);

						calendars.setWorkday(sdf.parse(day));
						calendars.setType(Integer.parseInt(type.substring(type.length() - 5, type.length() - 4)));
						tsmsService.insertCalendar(calendars);
					}
				} else {
					for (int j = 1; j < 29; j++) {
						String day = "", type = "", month = "", days = "";

						if (i < 10) {
							month = "0" + i;
						} else {
							month = "" + i;
						}
						if (j < 10) {
							days = "0" + j;
						} else {
							days = "" + j;
						}
						day = year + "-" + month + "-" + days;
						type = Methods.getHolidays(year + "" + month + "" + days);

						calendars.setWorkday(sdf.parse(day));
						calendars.setType(Integer.parseInt(type.substring(type.length() - 5, type.length() - 4)));
						tsmsService.insertCalendar(calendars);
					}
				}
			} else if (i == 4 || i == 6 || i == 9 || i == 11) {
				for (int j = 1; j < 31; j++) {
					String day = "", type = "", month = "", days = "";
					if (i < 10) {
						month = "0" + i;
					} else {
						month = "" + i;
					}
					if (j < 10) {
						days = "0" + j;
					} else {
						days = "" + j;
					}
					day = year + "-" + month + "-" + days;
					type = Methods.getHolidays(year + "" + month + "" + days);

					calendars.setWorkday(sdf.parse(day));
					calendars.setType(Integer.parseInt(type.substring(type.length() - 5, type.length() - 4)));
					tsmsService.insertCalendar(calendars);
				}
			}

		}
	}

	public static void main(String[] args) throws ApiException {

        // List<Employee> notInSysUsersEmpl = new ArrayList<>();
        // List<SysUser> notInSysUsers = new ArrayList<>();
        // List<SysUserRole> notInSysRoles = new ArrayList<>();
        // List<Employee> employees = new ArrayList<>();
        // List<SysUser> sysUsers = new ArrayList<>();
        // for (long i = 0; i < 10; i++) {
        //     Employee e = new Employee();
        //     e.setEmployeeId(i);
        //     SysUser u = new SysUser();
        //     u.setEmployeeId(i + 3);
        //
        //     employees.add(e);
        //     sysUsers.add(u);
        // }
        //
        // for(SysUser sysUser : sysUsers) {
        //     employees = employees.stream().filter(employee ->
        //             !(sysUser.getEmployeeId().equals(employee.getEmployeeId()))).collect(Collectors.toList());
        // }
        // System.out.println(notInSysUsersEmpl);

		// try {
		// 	List<String> queryDimissionList = Methods.queryDimission(0L);
		// 	if(queryDimissionList.size()>0){
		// 		System.out.println(queryDimissionList.size());
		// 		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/smartwork/hrm/employee/listdimission");
		// 		OapiSmartworkHrmEmployeeListdimissionRequest req = new OapiSmartworkHrmEmployeeListdimissionRequest();
		// 		req.setUseridList(queryDimissionList.toString().replace(" ", "").replace("[", "").replace("]", ""));
		// 		OapiSmartworkHrmEmployeeListdimissionResponse response = client.execute(req , AuthHelper.getAccessToken());
		// 		if(0==response.getErrcode()){
		// 			List<EmpDimissionInfoVo> ddUserIdList = response.getResult();
		// 			Date date =new Date(1575043200000L);
		// 			tsmsService.batchUpdateLastWorkDate(ddUserIdList);
		// 		}else{
		// 			throw new ApiException(response.getErrmsg());
		// 		}
		// 	}
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		// System.out.println("0:"+new Date());
		// Timerdd dd  = new Timerdd();
		// dd.updateAttendance();
		// System.out.println("1:"+new Date());
		// List<Map<String, Object>> attds = getAttendances(users);
		// System.out.println("2:"+new Date());
		// int size = attds.size();
		// System.out.println(size);
	}
}
