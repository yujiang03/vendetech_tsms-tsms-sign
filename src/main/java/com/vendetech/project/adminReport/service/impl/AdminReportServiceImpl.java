package com.vendetech.project.adminReport.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.api.ApiException;
import com.vendetech.project.adminReport.mapper.AdminReportMapper;
import com.vendetech.project.adminReport.service.AdminReportService;
import com.vendetech.project.tsms.utils.ExportExcelUtil;
import com.vendetech.project.tsms.utils.dto.ExcelBean;


@Service
public class AdminReportServiceImpl implements AdminReportService {

	@Autowired
    private AdminReportMapper adminReportMapper;
	
	@Override
	public List<Map<String, Object>> getEmpTsReportList(HashMap<String, Object> params) {
		// TODO Auto-generated method stub
		return adminReportMapper.getEmpTsReportList(params);
	}

	public List<Map<String, Object>> getProjectTsReportList(HashMap<String, Object> params){
		return adminReportMapper.getProjectTsReportList(params);
	}
	
	public List<Map<String, Object>> getEmpUtilizationReportList(HashMap<String, Object> params){
		return adminReportMapper.getEmpUtilizationReportList(params);
	}
	
	public List<Map<String, Object>> getEmpOnboardingOffReportList(HashMap<String, Object> params)throws ApiException{

		/*List<String> queryPreentryList = Methods.queryPreentry(0L);
		if(queryPreentryList.size()>0){
			params.put("queryPreentry",queryPreentryList.toString().replace(" ", "").replace("[", "").replace("]", ""));
		}
		List<String> queryOnjobList = Methods.queryOnjob(0L);
		if(queryOnjobList.size()>0){
			params.put("queryOnjob",queryOnjobList.toString().replace(" ", "").replace("[", "").replace("]", ""));
		}
		List<String> queryDimissionList = Methods.queryPreentry(0L);
		if(queryDimissionList.size()>0){
			params.put("queryDimission",queryDimissionList.toString().replace(" ", "").replace("[", "").replace("]", ""));
		}*/
		return adminReportMapper.getEmpOnboardingOffReportList(params);
	}
	
	public XSSFWorkbook exportEmpTsReport(HashMap<String, Object> params) throws Exception{
		//根据条件查询数据
        List<Map<String,Object>> list = adminReportMapper.getEmpTsReportList(params);
        //System.out.println(list);
        List<ExcelBean> excel = new ArrayList<>();
        Map<Integer,List<ExcelBean>> map = new LinkedHashMap<>();
        //设置标题栏
        excel.add(new ExcelBean("员工号","employee_num",0));
        excel.add(new ExcelBean("员工名","employee_name",0));
        excel.add(new ExcelBean("员工所属公司","emp_company_name",0));
        excel.add(new ExcelBean("员工所属部门编号","emp_department_num",0));
        excel.add(new ExcelBean("员工所属部门名称","emp_department_name",0));
        excel.add(new ExcelBean("项目编码","project_num",0));
        excel.add(new ExcelBean("项目名称", "project_name", 0));
        excel.add(new ExcelBean("项目所属公司", "project_company_name", 0));
        excel.add(new ExcelBean("月份","ts_month",0));
        excel.add(new ExcelBean("状态","emp_status",0));
        excel.add(new ExcelBean("各项目投入工时","emp_project_ts",0));
        excel.add(new ExcelBean("员工当月实际总工时","emp_month_ts", 0));
        excel.add(new ExcelBean("项目投入比例 = 各项目投入工时/员工当月实际总工时","proportion", 0));
        excel.add(new ExcelBean("分配者工号","assigner_emp_id", 0));
        excel.add(new ExcelBean("分配者姓名","assigner_emp_name", 0));
        map.put(0,excel);
        String sheetName = "人员统计表";
        //调用ExcelUtil方法
        XSSFWorkbook xssfWorkbook = null;
        xssfWorkbook = ExportExcelUtil.createExcelFile(HashMap.class, list, map, sheetName);
        System.out.println(xssfWorkbook);
        return xssfWorkbook;
	}
	
	public XSSFWorkbook exportProjectTsReport(HashMap<String, Object> params) throws Exception{
		//根据条件查询数据
        List<Map<String,Object>> list = adminReportMapper.getProjectTsReportList(params);
        //System.out.println(list);
        List<ExcelBean> excel = new ArrayList<>();
        Map<Integer,List<ExcelBean>> map = new LinkedHashMap<>();
        //设置标题栏
        excel.add(new ExcelBean("项目所属部门编号","project_department_num",0));
        excel.add(new ExcelBean("项目所属部门名称","project_department_name",0));
        excel.add(new ExcelBean("状态","emp_status",0));
        excel.add(new ExcelBean("项目编码","project_num",0));
        excel.add(new ExcelBean("项目名称", "project_name", 0));
        excel.add(new ExcelBean("项目所属公司", "project_company_name", 0));
        excel.add(new ExcelBean("员工号","employee_num",0));
        excel.add(new ExcelBean("员工名","employee_name",0));
        excel.add(new ExcelBean("员工所属公司","emp_company_name",0));
        excel.add(new ExcelBean("员工所属部门编号", "emp_department_num", 0));
        excel.add(new ExcelBean("员工所属部门名称", "emp_department_name", 0));
        excel.add(new ExcelBean("月份","ts_month",0));        
        excel.add(new ExcelBean("各项目投入工时","project_ts",0));
        excel.add(new ExcelBean("项目总工时","project_month_ts", 0));
        excel.add(new ExcelBean("项目投入比例 = 各项目投入工时/员工当月实际总工时","proportion", 0));
        excel.add(new ExcelBean("分配者工号","assigner_emp_id", 0));
        excel.add(new ExcelBean("分配者姓名","assigner_emp_name", 0));
        map.put(0,excel);
        String sheetName = "项目统计表";
        //调用ExcelUtil方法
        XSSFWorkbook xssfWorkbook = null;
        xssfWorkbook = ExportExcelUtil.createExcelFile(HashMap.class, list, map, sheetName);
        System.out.println(xssfWorkbook);
        return xssfWorkbook;
	}
	
	public XSSFWorkbook exportEmpUtilizationReport(HashMap<String, Object> params) throws Exception{
		//根据条件查询数据
        List<Map<String,Object>> list = adminReportMapper.getEmpUtilizationReportList(params);
        //System.out.println(list);
        List<ExcelBean> excel = new ArrayList<>();
        Map<Integer,List<ExcelBean>> map = new LinkedHashMap<>();
        //设置标题栏
        excel.add(new ExcelBean("员工所属公司","emp_company_name",0));
        excel.add(new ExcelBean("员工所属部门编号", "emp_department_num", 0));
        excel.add(new ExcelBean("员工所属部门名称","emp_department_name",0));
        excel.add(new ExcelBean("状态","emp_status",0));
        excel.add(new ExcelBean("月份","ts_month",0));
        excel.add(new ExcelBean("此状态人数","depart_emp_count", 0));
        excel.add(new ExcelBean("部门总人数","total_depart_emp_count", 0));
        excel.add(new ExcelBean("门各状态投入总工时","emp_ts_count", 0));
        excel.add(new ExcelBean("部门当月实际总工时","total_emp_ts_count", 0));
        excel.add(new ExcelBean("利用率 =部门各状态投入总工时/部门当月实际总工时","proportion", 0));
        map.put(0,excel);
        String sheetName = "人员利用率表";
        //调用ExcelUtil方法
        XSSFWorkbook xssfWorkbook = null;
        xssfWorkbook = ExportExcelUtil.createExcelFile(HashMap.class, list, map, sheetName);
        System.out.println(xssfWorkbook);
        return xssfWorkbook;
	}
	
	public XSSFWorkbook exportEmpOnboardingOffReport(HashMap<String, Object> params) throws Exception{
		//根据条件查询数据
        List<Map<String,Object>> list = this.getEmpOnboardingOffReportList(params);
        //System.out.println(list);
        List<ExcelBean> excel = new ArrayList<>();
        Map<Integer,List<ExcelBean>> map = new LinkedHashMap<>();
        //设置标题栏
        excel.add(new ExcelBean("员工所属公司","emp_company_name",0));
        excel.add(new ExcelBean("员工所属部门编号", "emp_department_num", 0));
        excel.add(new ExcelBean("员工所属部门名称","emp_department_name",0));
        excel.add(new ExcelBean("状态","emp_status",0));
        excel.add(new ExcelBean("月份","ts_month",0));
        excel.add(new ExcelBean("此状态人数","depart_emp_count", 0));
        excel.add(new ExcelBean("部门总人数","total_depart_emp_count", 0));
        excel.add(new ExcelBean("比率 =此状态人数/部门总人数","proportion", 0));
        map.put(0,excel);
        String sheetName = "人员入离职表";
        //调用ExcelUtil方法
        XSSFWorkbook xssfWorkbook = null;
        xssfWorkbook = ExportExcelUtil.createExcelFile(HashMap.class, list, map, sheetName);
        System.out.println(xssfWorkbook);
        return xssfWorkbook;
	}
}
