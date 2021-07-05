package com.vendetech.project.tsms.domain;

import java.math.BigDecimal;
import java.util.Date;

public class DdAttendanceData {

	private Long ddAttendanceId;

	private String ddUserid;

	private String employeeNum;

	private Date attendanceDate;

	private Date attendanceIn1;

	private Date attendanceOut1;

	private Integer attendanceIn2;

	private Date attendanceOut2;

	private Integer attendanceMinute;

	private String result;

	private BigDecimal dutyDay;

	private BigDecimal attendDay;

	private Integer allowedOverHours;

	private Integer ifOvertime;

	private Integer ifDuty;

	private Integer ifLeave;
	
	private Integer leaveHours;
	
	private Integer tsExclFlag;

	private Integer status;

	private Date ddCreateTime;

	private Date ddModifyTime;

	private Date createTime;

	private Date modifyTime;

	public Long getDdAttendanceId() {
		return ddAttendanceId;
	}

	public void setDdAttendanceId(Long ddAttendanceId) {
		this.ddAttendanceId = ddAttendanceId;
	}

	public String getDdUserid() {
		return ddUserid;
	}

	public void setDdUserid(String ddUserid) {
		this.ddUserid = ddUserid == null ? null : ddUserid.trim();
	}

	public String getEmployeeNum() {
		return employeeNum;
	}

	public void setEmployeeNum(String employeeNum) {
		this.employeeNum = employeeNum == null ? null : employeeNum.trim();
	}

	public Date getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public Date getAttendanceIn1() {
		return attendanceIn1;
	}

	public void setAttendanceIn1(Date attendanceIn1) {
		this.attendanceIn1 = attendanceIn1;
	}

	public Date getAttendanceOut1() {
		return attendanceOut1;
	}

	public void setAttendanceOut1(Date attendanceOut1) {
		this.attendanceOut1 = attendanceOut1;
	}

	public Integer getAttendanceIn2() {
		return attendanceIn2;
	}

	public void setAttendanceIn2(Integer attendanceIn2) {
		this.attendanceIn2 = attendanceIn2;
	}

	public Date getAttendanceOut2() {
		return attendanceOut2;
	}

	public void setAttendanceOut2(Date attendanceOut2) {
		this.attendanceOut2 = attendanceOut2;
	}

	public Integer getAttendanceMinute() {
		return attendanceMinute;
	}

	public void setAttendanceMinute(Integer attendanceMinute) {
		this.attendanceMinute = attendanceMinute;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result == null ? null : result.trim();
	}

	public BigDecimal getDutyDay() {
		return dutyDay;
	}

	public void setDutyDay(BigDecimal dutyDay) {
		this.dutyDay = dutyDay;
	}

	public BigDecimal getAttendDay() {
		return attendDay;
	}

	public void setAttendDay(BigDecimal attendDay) {
		this.attendDay = attendDay;
	}

	public Integer getAllowedOverHours() {
		return allowedOverHours;
	}

	public void setAllowedOverHours(Integer allowedOverHours) {
		this.allowedOverHours = allowedOverHours;
	}

	public Integer getIfOvertime() {
		return ifOvertime;
	}

	public void setIfOvertime(Integer ifOvertime) {
		this.ifOvertime = ifOvertime;
	}

	public Integer getIfDuty() {
		return ifDuty;
	}

	public void setIfDuty(Integer ifDuty) {
		this.ifDuty = ifDuty;
	}

	public Integer getIfLeave() {
		return ifLeave;
	}

	public void setIfLeave(Integer ifLeave) {
		this.ifLeave = ifLeave;
	}

	public Integer getLeaveHours() {
		return leaveHours;
	}

	public void setLeaveHours(Integer leaveHours) {
		this.leaveHours = leaveHours;
	}

	public Integer getTsExclFlag() {
		return tsExclFlag;
	}

	public void setTsExclFlag(Integer tsExclFlag) {
		this.tsExclFlag = tsExclFlag;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getDdCreateTime() {
		return ddCreateTime;
	}

	public void setDdCreateTime(Date ddCreateTime) {
		this.ddCreateTime = ddCreateTime;
	}

	public Date getDdModifyTime() {
		return ddModifyTime;
	}

	public void setDdModifyTime(Date ddModifyTime) {
		this.ddModifyTime = ddModifyTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

}