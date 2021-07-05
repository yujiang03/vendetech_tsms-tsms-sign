package com.vendetech.project.adminReport.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.taobao.api.ApiException;

public interface AdminReportService {

	List<Map<String, Object>> getEmpTsReportList(HashMap<String, Object> params);

	XSSFWorkbook exportEmpTsReport(HashMap<String, Object> params) throws Exception;

	List<Map<String, Object>> getProjectTsReportList(HashMap<String, Object> params);

	XSSFWorkbook exportProjectTsReport(HashMap<String, Object> params) throws Exception;

	List<Map<String, Object>> getEmpUtilizationReportList(HashMap<String, Object> params);

	XSSFWorkbook exportEmpUtilizationReport(HashMap<String, Object> params) throws Exception;

	List<Map<String, Object>> getEmpOnboardingOffReportList(HashMap<String, Object> params) throws ApiException;

	XSSFWorkbook exportEmpOnboardingOffReport(HashMap<String, Object> params) throws Exception;

}
