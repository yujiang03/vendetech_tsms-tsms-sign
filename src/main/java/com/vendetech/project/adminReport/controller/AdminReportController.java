package com.vendetech.project.adminReport.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vendetech.framework.web.controller.BaseController;
import com.vendetech.framework.web.page.TableDataInfo;
import com.vendetech.project.adminReport.service.AdminReportService;


@RestController
@RequestMapping("adminReport")
public class AdminReportController extends BaseController {
	
	protected final Logger logger = LoggerFactory.getLogger(AdminReportController.class);
	
	@Autowired
	private AdminReportService adminReportService;
	/**
	 * 人员统计表
	 * 
	 * @return
	 */
	@PostMapping(value = "getEmpTsReportList")
	public TableDataInfo getEmpTsReportList(@RequestBody HashMap<String,Object> params) {
		// 开始分页
		startPage();
		// 列表展示
		List<Map<String, Object>> lists = adminReportService.getEmpTsReportList(params);
		return getDataTable(lists);
	}
	/**
	 * 项目统计表
	 * 
	 * @return
	 */
	@PostMapping(value = "getProjectTsReportList")
	public TableDataInfo getProjectTsReportList(@RequestBody HashMap<String,Object> params) {
		// 开始分页
		startPage();
		// 列表展示
		List<Map<String, Object>> lists = adminReportService.getProjectTsReportList(params);
		return getDataTable(lists);
	}
	/**
	 * 人员利用率统计表
	 * 
	 * @return
	 */
	@PostMapping(value = "getEmpUtilizationReportList")
	public TableDataInfo getEmpUtilizationReportList(@RequestBody HashMap<String,Object> params) {
		// 开始分页
		startPage();
		// 列表展示
		List<Map<String, Object>> lists = adminReportService.getEmpUtilizationReportList(params);
		return getDataTable(lists);
	}
	/**
	 * 人员入离职统计表
	 * 
	 * @return
	 */
	@PostMapping(value = "getEmpOnboardingOffReportList")
	public TableDataInfo getEmpOnboardingOffReportList(@RequestBody HashMap<String,Object> params) throws Exception{
		// 开始分页
		startPage();
		// 列表展示
		List<Map<String, Object>> lists = adminReportService.getEmpOnboardingOffReportList(params);
		return getDataTable(lists);
	}
	
	/***
	 * 导出人员统计表
	 * @param request
	 * @param response
	 * @param projectDTO
	 * @throws Exception
	 */
	@PostMapping("exportEmpTsReport")
	public void exportEmpTsReport(HttpServletRequest request, HttpServletResponse response, @RequestBody HashMap<String,Object> params) throws Exception {
		response.reset(); //清除buffer缓存
		//Map<String,Object> map=new HashMap<String,Object>();
		// 指定下载的文件名
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition","attachment;filename="+new String("人员统计表.xls".getBytes(),"UTF-8"));
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, accept, content-type, xxxx");
		response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		//导出Excel对象
		XSSFWorkbook workbook = null;
		try {
			workbook = adminReportService.exportEmpTsReport(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		OutputStream output;
		try {
			output = response.getOutputStream();
			BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
			bufferedOutput.flush();
			workbook.write(bufferedOutput);
			bufferedOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/***
	 * 导出项目统计表
	 * @param request
	 * @param response
	 * @param projectDTO
	 * @throws Exception
	 */
	@PostMapping("exportProjectTsReport")
	public void exportProjectTsReport(HttpServletRequest request, HttpServletResponse response, @RequestBody HashMap<String,Object> params) throws Exception {
		response.reset(); //清除buffer缓存
		//Map<String,Object> map=new HashMap<String,Object>();
		// 指定下载的文件名
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition","attachment;filename="+new String("项目统计表.xls".getBytes(),"UTF-8"));
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, accept, content-type, xxxx");
		response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		//导出Excel对象
		XSSFWorkbook workbook = null;
		try {
			workbook = adminReportService.exportProjectTsReport(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		OutputStream output;
		try {
			output = response.getOutputStream();
			BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
			bufferedOutput.flush();
			workbook.write(bufferedOutput);
			bufferedOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * 导出人员利用表
	 * @param request
	 * @param response
	 * @param projectDTO
	 * @throws Exception
	 */
	@PostMapping("exportEmpUtilizationReport")
	public void exportEmpUtilizationReport(HttpServletRequest request, HttpServletResponse response, @RequestBody HashMap<String,Object> params) throws Exception {
		response.reset(); //清除buffer缓存
		//Map<String,Object> map=new HashMap<String,Object>();
		// 指定下载的文件名
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition","attachment;filename="+new String("人员利用率表.xls".getBytes(),"UTF-8"));
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, accept, content-type, xxxx");
		response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		//导出Excel对象
		XSSFWorkbook workbook = null;
		try {
			workbook = adminReportService.exportEmpUtilizationReport(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		OutputStream output;
		try {
			output = response.getOutputStream();
			BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
			bufferedOutput.flush();
			workbook.write(bufferedOutput);
			bufferedOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/***
	 * 导出人员入离职表
	 * @param request
	 * @param response
	 * @param projectDTO
	 * @throws Exception
	 */
	@PostMapping("exportEmpOnboardingOffReport")
	public void exportEmpOnboardingOffReport(HttpServletRequest request, HttpServletResponse response, @RequestBody HashMap<String,Object> params) throws Exception {
		response.reset(); //清除buffer缓存
		//Map<String,Object> map=new HashMap<String,Object>();
		// 指定下载的文件名
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition","attachment;filename="+new String("人员入离职表.xls".getBytes(),"UTF-8"));
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, accept, content-type, xxxx");
		response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		//导出Excel对象
		XSSFWorkbook workbook = null;
		try {
			workbook = adminReportService.exportEmpOnboardingOffReport(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		OutputStream output;
		try {
			output = response.getOutputStream();
			BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
			bufferedOutput.flush();
			workbook.write(bufferedOutput);
			bufferedOutput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
