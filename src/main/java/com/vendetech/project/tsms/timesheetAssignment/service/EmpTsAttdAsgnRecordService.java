package com.vendetech.project.tsms.timesheetAssignment.service;

import java.util.Map;

public interface EmpTsAttdAsgnRecordService {
	void freshEmpTsAttdAsgnRecord(Map<String, Object> params);

	void freshEmpTsAttdAsgnRecordUpdate(Map<String, Object> params);

	/**
	 * @param proc
	 */
	void freshEmpTsAttdAsgnRecordByEmp(Map<String, Object> proc);
}
