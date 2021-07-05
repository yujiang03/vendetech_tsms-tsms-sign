package com.vendetech.project.tsms.timesheet.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dingtalk.api.response.OapiSmartworkHrmEmployeeListdimissionResponse.EmpDimissionInfoVo;
import com.vendetech.project.tsms.domain.EmpTsAttdAsgnRecord;

public interface EmpTsAttdAsgnRecordMapper {

	int deleteByPrimaryKey(Long id);

	int insert(EmpTsAttdAsgnRecord record);

	int insertSelective(EmpTsAttdAsgnRecord record);

	EmpTsAttdAsgnRecord selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(EmpTsAttdAsgnRecord record);

	int updateByPrimaryKey(EmpTsAttdAsgnRecord record);

	List<EmpTsAttdAsgnRecord> selectUnmatchList(@Param("depts") String depts, @Param("fromDate") String fromDate,
			@Param("toDate") String toDate);

	List<EmpTsAttdAsgnRecord> selectUnmatchListByMonth(@Param("empNum") String empNum, @Param("year") String year,
			@Param("month") String month);

	Integer getNeedAssignHours(@Param("depts") String depts, @Param("fromDate") String fromDate,
			@Param("toDate") String toDate);

	Integer getAtcualAssignHours(@Param("depts") String depts, @Param("fromDate") String fromDate,
			@Param("toDate") String toDate);

	Integer getEmpNeedAssignHours(@Param("empId") Long empId, @Param("fromDate") String fromDate,
			@Param("toDate") String toDate);

	Integer getEmpAtcualAssignHours(@Param("empId") Long empId, @Param("fromDate") String fromDate,
			@Param("toDate") String toDate);

	List<EmpTsAttdAsgnRecord> selectUnmatchListByDeptsMonth(@Param("depts") String depts, @Param("year") String year,
			@Param("month") String month);

	List<Map<String, Object>> sumNormalTimesheetList(Map<String, Object> params);

	List<Map<String, Object>> selectNormalTimesheetList(Map<String, Object> params);

	List<Map<String, Object>> sumHistoryTimesheetList(Map<String, Object> params);

	List<Map<String, Object>> selectHistoryTimesheetList(Map<String, Object> params);

	int countNormalTimesheetList(Map<String, Object> params);

	int clearEmpTsAttdAsgnRecords(@Param("dtlIds") String[] ids);

	void freshAllEmpTsAttdAsgnRecord(Map<String, Object> params);

	void freshAllEmpTsAttdAsgnRecordByMonth(Map<String, Object> params);

	void freshEmpTsAttdAsgnRecord(Map<String, Object> params);

	void freshEmpTsAttdAsgnRecordUpdate(Map<String, Object> params);

	List<EmpTsAttdAsgnRecord> findEmpTsAttdAsgnRecord(EmpTsAttdAsgnRecord record);

	/**
	 * @param proc 
	 */
	void freshEmpTsAttdAsgnRecordByEmp(Map<String, Object> proc);

	void batchUpdateLastWorkDate(List<EmpDimissionInfoVo> ddUserIdList);
	
	Integer countStaff(@Param("depts") String depts);
	
	List<Map<String, Object>> getRealtimeAnalysisReport(Map<String, Object> params);

	List<Map<String, Object>> getHistoryFinanceReport(Map<String, Object> params);

}