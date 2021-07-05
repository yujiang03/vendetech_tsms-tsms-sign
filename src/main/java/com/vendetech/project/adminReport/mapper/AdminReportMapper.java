package com.vendetech.project.adminReport.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface AdminReportMapper {

	List<Map<String, Object>> getEmpTsReportList(HashMap<String, Object> params);

	List<Map<String, Object>> getProjectTsReportList(HashMap<String, Object> params);

	List<Map<String, Object>> getEmpUtilizationReportList(HashMap<String, Object> params);

	List<Map<String, Object>> getEmpOnboardingOffReportList(HashMap<String, Object> params);

}
