package com.vendetech.project.tsms.timesheet.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vendetech.common.utils.DateUtils;
import com.vendetech.framework.web.controller.BaseController;
import com.vendetech.project.system.domain.SysUser;
import com.vendetech.project.tsms.domain.Department;
import com.vendetech.project.tsms.domain.EmpTsAttdAsgnRecord;
import com.vendetech.project.tsms.domain.Employee;
import com.vendetech.project.tsms.domain.Project;
import com.vendetech.project.tsms.domain.R;
import com.vendetech.project.tsms.project.service.ProjectService;
import com.vendetech.project.tsms.timesheet.service.DashboardService;
import com.vendetech.project.tsms.timesheet.service.ReportService;
import com.vendetech.project.tsms.utils.ExcelUtils;

@RestController
@RequestMapping("report")
public class ReportController extends BaseController {

    protected final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Autowired
    private ReportService reportService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private DashboardService dashboardService;

    /**
     * -项目下拉
     *
     * @return
     */
    @GetMapping(value = "/getProjectList")
    public R getProjectList(Project project) {
        List<HashMap<String, Object>> projectList = projectService.selectByProjectList(project.getProjectName());
        return R.data(projectList);
    }

    /**
     * -部门下拉
     */
    // @GetMapping(value = "/getDepartmentList")	// Replaced
    // public R getDepartMentNameList() {
    // 	List<Department> departMentNameList = projectService.getDepartMentNameList();
    // 	return R.data(departMentNameList);
    // }

    /**
     * * 导入 员工考勤信息
     *
     * @param file
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    @PostMapping("importAttendanceRecords")
    public R importAttendanceRecords(@RequestParam MultipartFile file) throws IOException, InvalidFormatException {
        SysUser currentUser = this.getSysUser();
        String mgrNum = currentUser.getUserName();
        Long role = currentUser.getRoles().get(0).getRoleId();

        Workbook workbook = ExcelUtils.getWorkBook(file);
        // List<String[]> excelData = ExcelUtils.getExcelData(file);

        List<String[]> excelData = new ArrayList<String[]>();
        if (null != workbook) {
            Sheet sheet = workbook.getSheetAt(0); // 第一个sheet
            if (null != sheet) {
                // 获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                // 获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                // 循环除了第一行的所有行
                for (int rowNum = firstRowNum + 1; rowNum <= lastRowNum; rowNum++) {
                    // 获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    // 获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    // 获得当前行的列数
                    int lastCellNum = row.getLastCellNum();
                    String[] cells = new String[row.getLastCellNum()];
                    // 循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = ExcelUtils.getCellValue(cell);
                    }
                    excelData.add(cells);
                }
            }
        } else {
            excelData = null;
        }

        List<EmpTsAttdAsgnRecord> recordList = new ArrayList<EmpTsAttdAsgnRecord>();
        List<String> errorList = new ArrayList<String>();
        if (null != excelData && excelData.size() > 0) {
            for (int i = 0; i < excelData.size(); i++) {
                String[] row = excelData.get(i);
                EmpTsAttdAsgnRecord record = null;
                if (row.length >= 6) {
                    String employeeNum = row[0].trim();
                    String employeeName = row[1].trim();
                    String type = row[2];
                    int assignType = 0;
                    String projectNum = (null == row[3] ? "" : row[3].trim());
                    String projectName = (null == row[4] ? "" : row[4].trim());
                    Long projectId = null;
                    String attdDate = row[5];
                    Date tsDate = null;
                    String workHours = row[6];
                    int attendHours = 0;
                    String errorComments = "";
                    try {
                        List<Employee> findEmps = reportService.findEmployeeByNumName(employeeNum, employeeName);
                        if (null == findEmps || findEmps.size() == 0) {
                            errorComments = errorComments + "员工信息不正确。";
                        } else {
                            if (5L == role) {
                                Department bu = dashboardService.getEmpMainBuDepartment(mgrNum);
                                for (Employee emp : findEmps) {
                                    Department dpt = dashboardService.getEmpMainBuDepartment(emp.getEmployeeNum());
                                    if (!bu.getDepartmentNum().equals(dpt.getDepartmentNum())) {
                                        errorComments = errorComments + "员工不属于所属事业部。";
                                        break;
                                    }
                                }
                            }
                        }
                        if ("项目中".equals(type.trim())) {
                            List<Project> findPrj = reportService.findProjectByNumName(projectNum, projectName);
                            if (null == findPrj || findPrj.size() == 0) {
                                errorComments = errorComments + "项目信息不正确。";
                            } else {
                                assignType = 1;
                                projectId = findPrj.get(0).getProjectId();
                            }
                        } else if ("培训".equals(type.trim())) {
                            if (StringUtils.isNotBlank(projectName) || StringUtils.isNotBlank(projectNum)) {
                                errorComments = errorComments + "项目信息应该为空。";
                            } else {
                                assignType = 2;
                                projectId = null;
                            }
                        } else if ("闲置".equals(type.trim())) {
                            if (StringUtils.isNotBlank(projectName) || StringUtils.isNotBlank(projectNum)) {
                                errorComments = errorComments + "项目信息应该为空。";
                            } else {
                                assignType = 3;
                                projectId = null;
                            }
                        } else {
                            errorComments = errorComments + "分配状态不正确。";
                        }
                        try {
                            Number hours = Float.parseFloat(workHours) * 10;
                            attendHours = hours.intValue() / 10;
                        } catch (NumberFormatException nfe) {
                            errorComments = errorComments + "工时数不正确。";
                        }
                        tsDate = DateUtils.parseDate(attdDate);
                        Date today = new Date();
                        if (!(null != tsDate && !today.before(tsDate))) {
                            errorComments = errorComments + "日期不正确或晚于当天。";
                        }
                        if (!"".equals(errorComments)) {
                            errorList.add("第" + (i + 1) + "行导入数据错误：" + errorComments);
                        } else {
                            record = new EmpTsAttdAsgnRecord();
                            Employee emp = findEmps.get(0);
                            record.setEmployeeId(emp.getEmployeeId());
                            record.setEmployeeName(emp.getEmployeeName());
                            record.setEmployeeNum(emp.getEmployeeNum());
                            record.setAssignType(assignType);
                            record.setAssignProjectId(projectId);
                            record.setAssignedStatus(1);
                            record.setAttendanceDayHours(attendHours);
                            record.setIfIssue(0);
                            record.setNeedAssign(1);
                            record.setTsDate(tsDate);
                            record.setSource(1);
                            record.setSourceBy(mgrNum);
                            record.setAssigner(mgrNum);
                        }
                        recordList.add(record);
                    } catch (Exception ex) {
                        errorList.add("第" + (i + 1) + "行导入数据解析报错。");
                        continue;
                    }
                } else {
                    errorList.add("第" + (i + 1) + "行导入数据字段缺失。");
                }
            }
        } else {
            errorList.add("导入数据为空或格式有误。");
        }
        if (errorList.size() > 0) {
            return R.error(423, "导入数据有误。", errorList);
        } else {
            int result = reportService.importUpdateEmpTsAttdAsgnRecord(recordList);
            logger.info("import qty:" + result);
            return R.ok("导入成功。");
        }
    }

    /**
     * * 下载 员工考勤信息模板
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("downloadAttendanceRecordsTemplate")
    public void downloadAttendanceRecordsTemplate(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        XSSFWorkbook wb = null;
        String fileName = "Attendance_Import_template.xlsx";
        try {
            // excel模板路径
            Resource res = new ClassPathResource("doc/" + fileName);
			// File fi = ResourceUtils.getFile("classpath:doc/" + fileName);
            // 读取excel模板
            wb = new XSSFWorkbook(res.getInputStream());

            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            OutputStream out = response.getOutputStream();
            wb.write(out);

            out.close();
        } catch (Exception e) {
            logger.error("下载工时数据模板出现异常：", e);
        }
    }

    /**
     * * 导出 员工考勤信息，分析报告的实时数据
     *
     * @param @return @throws
     */
    @PostMapping("exportAttendanceRecords")
    public void exportAttendanceRecords(HttpServletRequest request, HttpServletResponse response,
                                        @RequestBody HashMap<String, Object> param) throws Exception {
        String dptId = "" + param.get("departmentId");
        Long departmentId = null;
        if (null != param.get("departmentId") && StringUtils.isNotBlank(dptId)) {
            departmentId = 0L + Integer.valueOf(dptId.trim());
        }
        param.put("departmentId", departmentId);
        String prjId = "" + param.get("projectId");
        Long projectId = null;
        if (null != param.get("projectId") && StringUtils.isNotBlank(prjId)) {
            projectId = 0L + Integer.valueOf(prjId.trim());
        }
        param.put("projectId", projectId);

        List<Map<String, Object>> reportRecords = reportService.getNormalTimesheetRecords(param);

        String fileName = "Attendance_Export_template.xlsx";
        XSSFWorkbook wb = null;
        try {
            ClassPathResource cpr = new ClassPathResource("doc/" + fileName);
            // excel模板路径
//			File fi = ResourceUtils.getFile("classpath:doc/" + fileName);
            // 读取excel模板
            wb = new XSSFWorkbook(cpr.getInputStream());

            XSSFSheet sheet = wb.getSheetAt(0);
            // 设置字体
            XSSFFont font = wb.createFont();
//			font.setFontHeightInPoints((short) 30);
            font.setFontName("微软雅黑");
//			font.setItalic(true);

            // 设置样式
            XSSFCellStyle style = wb.createCellStyle();
            style.setFont(font);
            for (int i = 0; i < reportRecords.size(); i++) {
                Map<String, Object> record = reportRecords.get(i);
                XSSFRow row = sheet.getRow(i + 1);
                if (null == row) {
                    row = sheet.createRow(i + 1);
                }
                XSSFCell cell1 = row.getCell(0);
                if (null == cell1) {
                    cell1 = row.createCell(0);
                }
                cell1.setCellValue("" + record.get("employeeNum"));
                cell1.setCellStyle(style);
                XSSFCell cell2 = row.getCell(1);
                if (null == cell2) {
                    cell2 = row.createCell(1);
                }
                cell2.setCellValue("" + record.get("employeeName"));
                cell2.setCellStyle(style);
                XSSFCell cell3 = row.getCell(2);
                if (null == cell3) {
                    cell3 = row.createCell(2);
                }
                cell3.setCellValue("" + record.get("employeeCompany"));
                cell3.setCellStyle(style);
                XSSFCell cell4 = row.getCell(3);
                if (null == cell4) {
                    cell4 = row.createCell(3);
                }
                cell4.setCellValue("" + record.get("projectDepartmentNum"));
                cell4.setCellStyle(style);
                XSSFCell cell5 = row.getCell(4);
                if (null == cell5) {
                    cell5 = row.createCell(4);
                }
                cell5.setCellValue("" + record.get("projectDepartmentName"));
                cell5.setCellStyle(style);
                XSSFCell cell6 = row.getCell(5);
                if (null == cell6) {
                    cell6 = row.createCell(5);
                }
                cell6.setCellValue("" + record.get("assignType"));
                cell6.setCellStyle(style);
                XSSFCell cell7 = row.getCell(6);
                if (null == cell7) {
                    cell7 = row.createCell(6);
                }
                cell7.setCellValue("" + record.get("assignProjectNum"));
                cell7.setCellStyle(style);
                XSSFCell cell8 = row.getCell(7);
                if (null == cell8) {
                    cell8 = row.createCell(7);
                }
                cell8.setCellValue("" + record.get("assignProjectName"));
                cell8.setCellStyle(style);
                XSSFCell cell9 = row.getCell(8);
                if (null == cell9) {
                    cell9 = row.createCell(8);
                }
                cell9.setCellValue("" + record.get("projectCompany"));
                cell9.setCellStyle(style);
                XSSFCell cell10 = row.getCell(9);
                if (null == cell10) {
                    cell10 = row.createCell(9);
                }
                cell10.setCellValue("" + record.get("attdDate"));
                cell10.setCellStyle(style);
                XSSFCell cell11 = row.getCell(10);
                if (null == cell11) {
                    cell11 = row.createCell(10);
                }
                cell11.setCellValue("" + record.get("attendanceDayHours"));
                cell11.setCellStyle(style);
                XSSFCell cell12 = row.getCell(11);
                if (null == cell12) {
                    cell12 = row.createCell(11);
                }
                cell12.setCellValue("" + record.get("assigner"));
                cell12.setCellStyle(style);
                XSSFCell cell13 = row.getCell(12);
                if (null == cell13) {
                    cell13 = row.createCell(12);
                }
                cell13.setCellValue("" + record.get("assignerName"));
                cell13.setCellStyle(style);
            }
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            OutputStream out = response.getOutputStream();
            wb.write(out);

            out.close();
        } catch (Exception e) {
            logger.error("导出Excel出现异常:", e);
        }
    }

    /**
     * 查询分析报表结果-实时
     */
    @RequestMapping(value = "/getAnalysisReport")
    public R getAnalysisReport(HttpServletRequest request, @RequestBody HashMap<String, Object> param) {
        SysUser currentUser = this.getSysUser();
        String mgrNum = currentUser.getUserName();
        Long role = currentUser.getRoles().get(0).getRoleId();
        if (5L == role) {
            String departs = getMgrdDptSet(mgrNum); // 所有管辖部门及其子部门
            param.put("depts", departs);
        }

        String fromDate = null;
        String toDate = null;
        String fromMonth = null;
        String toMonth = null;
        Map<String, Object> results = new HashMap<String, Object>();
        List<Map<String, Object>> realtimeAnalysisReport = new ArrayList<Map<String, Object>>();
        if (null != param.get("fromDate") && null != param.get("toDate")) {
            fromDate = "" + param.get("fromDate");
            toDate = "" + param.get("toDate");
            fromMonth = fromDate.substring(0, 7);
            toMonth = toDate.substring(0, 7);
            if (null != fromMonth && null != toMonth) { // && fromMonth.equals(toMonth)) {
                String dptId = "" + param.get("departmentId");
                Long departmentId = null;
                if (StringUtils.isNotBlank(dptId) && !"null".equals(dptId)) {
                    departmentId = 0L + Integer.valueOf(dptId.trim());
                }
                param.put("departmentId", departmentId);
                String prjDptId = "" + param.get("projectDepartmentId");
                Long projectDepartmentId = null;
                if (StringUtils.isNotBlank(prjDptId) && !"null".equals(prjDptId)) {
                    projectDepartmentId = 0L + Integer.valueOf(prjDptId.trim());
                }
                param.put("projectDepartmentId", projectDepartmentId);
                String prjId = "" + param.get("projectId");
                Long projectId = null;
                if (StringUtils.isNotBlank(prjId) && !"null".equals(prjId)) {
                    projectId = 0L + Integer.valueOf(prjId.trim());
                }
                param.put("projectId", projectId);

                String compId = "" + param.get("companyId");
                Long companyId = null;
                if (StringUtils.isNotBlank(compId) && !"null".equals(compId)) {
                    companyId = 0L + Integer.valueOf(compId.trim());
                }
                param.put("companyId", companyId);
                String prjCompId = "" + param.get("projectCompanyId");
                Long projectCompanyId = null;
                if (StringUtils.isNotBlank(prjCompId) && !"null".equals(prjCompId)) {
                    projectCompanyId = 0L + Integer.valueOf(prjCompId.trim());
                }
                param.put("projectCompanyId", projectCompanyId);
                String empKeyword = "" + param.get("empKeyword");
                if (StringUtils.isNotBlank(empKeyword) && !"null".equals(empKeyword)) {
                    empKeyword = empKeyword.trim();
                } else {
                    empKeyword = null;
                }
                param.put("empKeyword", empKeyword);

                int pageNum = (Integer) param.get("pageNum");
                int pageSize = (Integer) param.get("pageSize");
                param.remove("pageSize");
                param.remove("pageNum");
                List<Map<String, Object>> report = reportService.getRealtimeAnalysisReport(param);
                int total = report.size();
                logger.info("report size:" + total);
                int start = pageSize * (pageNum - 1);
                int end = (pageSize * pageNum) - 1;
                for (int i = start; i <= end && i < report.size(); i++) {
                    Map<String, Object> record = report.get(i);
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("employeeNum", "" + record.get("employeeNum"));
                    item.put("employeeName", "" + record.get("employeeName"));
                    item.put("assignType", "" + record.get("assignType"));
                    item.put("projectName", "" + record.get("assignProjectName"));
                    item.put("projectNum",
                            (null == record.get("assignProjectNum") ? "" : "" + record.get("assignProjectNum")));
                    item.put("employeeDepartment", "" + record.get("employeeDepartmentName"));
                    item.put("tsMonth", "" + record.get("tsMonth"));
                    item.put("attendanceDayHours", "" + record.get("inputHours"));
                    realtimeAnalysisReport.add(item);
                }

                results.put("reportData", realtimeAnalysisReport);
                results.put("total", total);
                return R.data(results);
            } else {
                results.put("reportData", realtimeAnalysisReport);
                results.put("total", 0);
                results.put("msg", "起止日期必须在同一个月内。");
                return R.data(results);
//				return R.error(424, "起止日期必须在同一个月内。", null);
            }
        } else {
            results.put("reportData", realtimeAnalysisReport);
            results.put("total", 0);
            results.put("msg", "起止日期必选。");
            return R.data(results);
//			return R.error(424, "起止日期必选。", null);
        }
    }

    /**
     * * 导出分析报表结果，实时数据
     *
     * @param @return @throws
     */
    @PostMapping("exportAnalysisReport")
    public void exportAnalysisReport(HttpServletRequest request, HttpServletResponse response,
                                     @RequestBody HashMap<String, Object> param) throws Exception {
        SysUser currentUser = this.getSysUser();
        String mgrNum = currentUser.getUserName();
        Long role = currentUser.getRoles().get(0).getRoleId();
        if (5L == role) {
            String departs = getMgrdDptSet(mgrNum); // 所有管辖部门及其子部门
            param.put("depts", departs);
        }

        String dptId = "" + param.get("departmentId");
        Long departmentId = null;
        if (StringUtils.isNotBlank(dptId) && !"null".equals(dptId)) {
            departmentId = 0L + Integer.valueOf(dptId.trim());
        }
        param.put("departmentId", departmentId);
        String prjDptId = "" + param.get("projectDepartmentId");
        Long projectDepartmentId = null;
        if (StringUtils.isNotBlank(prjDptId) && !"null".equals(prjDptId)) {
            projectDepartmentId = 0L + Integer.valueOf(prjDptId.trim());
        }
        param.put("projectDepartmentId", projectDepartmentId);
        String prjId = "" + param.get("projectId");
        Long projectId = null;
        if (StringUtils.isNotBlank(prjId) && !"null".equals(prjId)) {
            projectId = 0L + Integer.valueOf(prjId.trim());
        }
        param.put("projectId", projectId);

        String compId = "" + param.get("companyId");
        Long companyId = null;
        if (StringUtils.isNotBlank(compId) && !"null".equals(compId)) {
            companyId = 0L + Integer.valueOf(compId.trim());
        }
        param.put("companyId", companyId);
        String prjCompId = "" + param.get("projectCompanyId");
        Long projectCompanyId = null;
        if (StringUtils.isNotBlank(prjCompId) && !"null".equals(prjCompId)) {
            projectCompanyId = 0L + Integer.valueOf(prjCompId.trim());
        }
        param.put("projectCompanyId", projectCompanyId);
        String empKeyword = "" + param.get("empKeyword");
        if (StringUtils.isNotBlank(empKeyword) && !"null".equals(empKeyword)) {
            empKeyword = empKeyword.trim();
        } else {
            empKeyword = null;
        }
        param.put("empKeyword", empKeyword);

        List<Map<String, Object>> reportRecords = reportService.getRealtimeAnalysisReport(param);

        String fileName = "ExportAnalysisReport.xlsx";
        XSSFWorkbook wb = null;
        try {
            wb = reportService.exportRealtimeAnalysisReport(reportRecords);
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//
//			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//			response.setHeader("Content-Disposition","attachment;filename="+new String("人员统计表.xls".getBytes(),"UTF-8"));
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
            response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, accept, content-type, xxxx");
            response.setHeader("Access-Control-Allow-Methods", "POST");
            response.setHeader("Access-Control-Allow-Credentials", "true");

            OutputStream out = response.getOutputStream();
            wb.write(out);

            out.close();
        } catch (Exception e) {
            logger.error("导出Excel出现异常:", e);
        }
    }

    /**
     * * 查询工时报表结果
     */
    @RequestMapping(value = "/getTimesheetReport")
    public R getTimesheetReport(HttpServletRequest request, @RequestBody HashMap<String, Object> param) {
        SysUser currentUser = this.getSysUser();
        String mgrNum = currentUser.getUserName();
        Long role = currentUser.getRoles().get(0).getRoleId();
        if (5L == role) {
            String departs = getMgrdDptSet(mgrNum); // 所有管辖部门及其子部门
            param.put("depts", departs);
        }

        String fromDate = null;
        String toDate = null;
        String fromMonth = null;
        String toMonth = null;
        Map<String, Object> results = new HashMap<String, Object>();
        List<Map<String, Object>> historyTimesheetReport = new ArrayList<Map<String, Object>>();
        if (null != param.get("fromDate") && null != param.get("toDate")) {
            fromDate = "" + param.get("fromDate");
            toDate = "" + param.get("toDate");
            fromMonth = fromDate.substring(0, 6);
            toMonth = toDate.substring(0, 6);
            if (null != fromMonth && null != toMonth) { // && fromMonth.equals(toMonth)) {
                String dptId = "" + param.get("departmentId");
                Long departmentId = null;
                if (StringUtils.isNotBlank(dptId) && !"null".equals(dptId)) {
                    departmentId = 0L + Integer.valueOf(dptId.trim());
                }
                param.put("departmentId", departmentId);
                String prjDptId = "" + param.get("projectDepartmentId");
                Long projectDepartmentId = null;
                if (StringUtils.isNotBlank(prjDptId) && !"null".equals(prjDptId)) {
                    projectDepartmentId = 0L + Integer.valueOf(prjDptId.trim());
                }
                param.put("projectDepartmentId", projectDepartmentId);
                String prjId = "" + param.get("projectId");
                Long projectId = null;
                if (StringUtils.isNotBlank(prjId) && !"null".equals(prjId)) {
                    projectId = 0L + Integer.valueOf(prjId.trim());
                }
                param.put("projectId", projectId);

                String compId = "" + param.get("companyId");
                Long companyId = null;
                if (StringUtils.isNotBlank(compId) && !"null".equals(compId)) {
                    companyId = 0L + Integer.valueOf(compId.trim());
                }
                param.put("companyId", companyId);
                String prjCompId = "" + param.get("projectCompanyId");
                Long projectCompanyId = null;
                ;
                if (StringUtils.isNotBlank(prjCompId) && !"null".equals(prjCompId)) {
                    projectCompanyId = 0L + Integer.valueOf(prjCompId.trim());
                }
                param.put("projectCompanyId", projectCompanyId);
                String empKeyword = "" + param.get("empKeyword");
                if (StringUtils.isNotBlank(empKeyword) && !"null".equals(empKeyword)) {
                    empKeyword = empKeyword.trim();
                } else {
                    empKeyword = null;
                }
                param.put("empKeyword", empKeyword);

                int pageNum = (Integer) param.get("pageNum");
                int pageSize = (Integer) param.get("pageSize");
                param.remove("pageSize");
                param.remove("pageNum");
                List<Map<String, Object>> report = reportService.getHistoryTimesheetReport(param);
                int total = report.size();
                logger.info("report size:" + total);
                int start = pageSize * (pageNum - 1);
                int end = (pageSize * pageNum) - 1;
                for (int i = start; i <= end && i < report.size(); i++) {
                    Map<String, Object> record = report.get(i);
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("employeeNum", "" + record.get("employeeNum"));
                    item.put("employeeName", "" + record.get("employeeName"));
                    item.put("assignType", "" + record.get("assignType"));
                    item.put("projectName", "" + record.get("assignProjectName"));
                    item.put("projectNum",
                            (null == record.get("assignProjectNum") ? "" : "" + record.get("assignProjectNum")));
                    item.put("employeeDepartment", "" + record.get("employeeDepartmentName"));
                    item.put("attendanceDayHours", "" + record.get("attendanceDayHours"));
                    historyTimesheetReport.add(item);
                }

                results.put("reportData", historyTimesheetReport);
                results.put("total", total);
                return R.data(results);
            } else {
                results.put("reportData", historyTimesheetReport);
                results.put("total", 0);
                results.put("msg", "起止日期必须在同一个月内。");
                return R.data(results);
//				return R.error(424, "起止日期必须在同一个月内。", null);
            }
        } else {
            results.put("reportData", historyTimesheetReport);
            results.put("total", 0);
            return R.data(results);
//			return R.error(424, "起止日期必选。", null);
        }
    }

    /**
     * * 导出 员工历史工时快照数据报表
     *
     * @param @return @throws
     */
    @PostMapping("exportTimesheetReport")
    public void exportTimesheetReport(HttpServletRequest request, HttpServletResponse response,
                                      @RequestBody HashMap<String, Object> param) throws Exception {
        SysUser currentUser = this.getSysUser();
        String mgrNum = currentUser.getUserName();
        Long role = currentUser.getRoles().get(0).getRoleId();
        if (5L == role) {
            String departs = getMgrdDptSet(mgrNum); // 所有管辖部门及其子部门
            param.put("depts", departs);
        }

        String dptId = "" + param.get("departmentId");
        Long departmentId = null;
        if (StringUtils.isNotBlank(dptId) && !"null".equals(dptId)) {
            departmentId = 0L + Integer.valueOf(dptId.trim());
        }
        param.put("departmentId", departmentId);
        String prjDptId = "" + param.get("projectDepartmentId");
        Long projectDepartmentId = null;
        if (StringUtils.isNotBlank(prjDptId) && !"null".equals(prjDptId)) {
            projectDepartmentId = 0L + Integer.valueOf(prjDptId.trim());
        }
        param.put("projectDepartmentId", projectDepartmentId);
        String prjId = "" + param.get("projectId");
        Long projectId = null;
        if (StringUtils.isNotBlank(prjId) && !"null".equals(prjId)) {
            projectId = 0L + Integer.valueOf(prjId.trim());
        }
        param.put("projectId", projectId);

        String compId = "" + param.get("companyId");
        Long companyId = null;
        if (StringUtils.isNotBlank(compId) && !"null".equals(compId)) {
            companyId = 0L + Integer.valueOf(compId.trim());
        }
        param.put("companyId", companyId);
        String prjCompId = "" + param.get("projectCompanyId");
        Long projectCompanyId = null;
        ;
        if (StringUtils.isNotBlank(prjCompId) && !"null".equals(prjCompId)) {
            projectCompanyId = 0L + Integer.valueOf(prjCompId.trim());
        }
        param.put("projectCompanyId", projectCompanyId);
        String empKeyword = "" + param.get("empKeyword");
        if (StringUtils.isNotBlank(empKeyword) && !"null".equals(empKeyword)) {
            empKeyword = empKeyword.trim();
        } else {
            empKeyword = null;
        }
        param.put("empKeyword", empKeyword);

        List<Map<String, Object>> reportRecords = reportService.getHistoryTimesheetRecords(param);

        String fileName = "Attendance_Export_template.xlsx";
        XSSFWorkbook wb = null;
        try {
            ClassPathResource cpr = new ClassPathResource("doc/" + fileName);
            // excel模板路径
//			File fi = ResourceUtils.getFile("classpath:doc/" + fileName);
            // 读取excel模板
            wb = new XSSFWorkbook(cpr.getInputStream());

            XSSFSheet sheet = wb.getSheetAt(0);
            // 设置字体
            XSSFFont font = wb.createFont();
//			font.setFontHeightInPoints((short) 30);
            font.setFontName("微软雅黑");
//			font.setItalic(true);

            // 设置样式
            XSSFCellStyle style = wb.createCellStyle();
            style.setFont(font);
            for (int i = 0; i < reportRecords.size(); i++) {
                Map<String, Object> record = reportRecords.get(i);
                XSSFRow row = sheet.getRow(i + 1);
                if (null == row) {
                    row = sheet.createRow(i + 1);
                }
                XSSFCell cell1 = row.getCell(0);
                if (null == cell1) {
                    cell1 = row.createCell(0);
                }
                cell1.setCellValue("" + record.get("employeeNum"));
                cell1.setCellStyle(style);
                XSSFCell cell2 = row.getCell(1);
                if (null == cell2) {
                    cell2 = row.createCell(1);
                }
                cell2.setCellValue("" + record.get("employeeName"));
                cell2.setCellStyle(style);
                XSSFCell cell3 = row.getCell(2);
                if (null == cell3) {
                    cell3 = row.createCell(2);
                }
                cell3.setCellValue("" + record.get("employeeCompany"));
                cell3.setCellStyle(style);
                XSSFCell cell4 = row.getCell(3);
                if (null == cell4) {
                    cell4 = row.createCell(3);
                }
                cell4.setCellValue("" + record.get("employeeDepartmentNum"));
                cell4.setCellStyle(style);
                XSSFCell cell5 = row.getCell(4);
                if (null == cell5) {
                    cell5 = row.createCell(4);
                }
                cell5.setCellValue("" + record.get("employeeDepartmentName"));
                cell5.setCellStyle(style);
                XSSFCell cell6 = row.getCell(5);
                if (null == cell6) {
                    cell6 = row.createCell(5);
                }
                cell6.setCellValue("" + record.get("assignType"));
                cell6.setCellStyle(style);
                XSSFCell cell7 = row.getCell(6);
                if (null == cell7) {
                    cell7 = row.createCell(6);
                }
                cell7.setCellValue("" + record.get("assignProjectNum"));
                cell7.setCellStyle(style);
                XSSFCell cell8 = row.getCell(7);
                if (null == cell8) {
                    cell8 = row.createCell(7);
                }
                cell8.setCellValue("" + record.get("assignProjectName"));
                cell8.setCellStyle(style);
                XSSFCell cell9 = row.getCell(8);
                if (null == cell9) {
                    cell9 = row.createCell(8);
                }
                cell9.setCellValue("" + record.get("projectCompany"));
                cell9.setCellStyle(style);
                XSSFCell cell10 = row.getCell(9);
                if (null == cell10) {
                    cell10 = row.createCell(9);
                }
                cell10.setCellValue("" + record.get("projectDepartmentNum"));
                cell10.setCellStyle(style);
                XSSFCell cell11 = row.getCell(10);
                if (null == cell11) {
                    cell11 = row.createCell(10);
                }
                cell11.setCellValue("" + record.get("projectDepartmentName"));
                cell11.setCellStyle(style);
                XSSFCell cell12 = row.getCell(11);
                if (null == cell12) {
                    cell12 = row.createCell(11);
                }
                cell12.setCellValue("" + record.get("attdDate"));
                cell12.setCellStyle(style);
                XSSFCell cell13 = row.getCell(12);
                if (null == cell13) {
                    cell13 = row.createCell(12);
                }
                cell13.setCellValue("" + record.get("attendanceDayHours"));
                cell13.setCellStyle(style);

                XSSFCell cell14 = row.getCell(13);
                if (null == cell14) {
                    cell14 = row.createCell(13);
                }
                cell14.setCellValue("" + record.get("contractStatus"));
                cell14.setCellStyle(style);
                XSSFCell cell15 = row.getCell(14);
                if (null == cell15) {
                    cell15 = row.createCell(14);
                }
                cell15.setCellValue("" + record.get("assigner"));
                cell15.setCellStyle(style);
                XSSFCell cell16 = row.getCell(15);
                if (null == cell16) {
                    cell16 = row.createCell(15);
                }
                cell16.setCellValue("" + record.get("assignerName"));
                cell16.setCellStyle(style);
            }
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//			response.setHeader("Content-Disposition","attachment;filename="+new String("人员统计表.xls".getBytes(),"UTF-8"));
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
            response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, accept, content-type, xxxx");
            response.setHeader("Access-Control-Allow-Methods", "POST");
            response.setHeader("Access-Control-Allow-Credentials", "true");

            OutputStream out = response.getOutputStream();
            wb.write(out);

            out.close();
        } catch (Exception e) {
            logger.error("导出Excel出现异常:", e);
        }
    }

    /**
     * * 查询财务报表结果
     */
    @RequestMapping(value = "/getFinanceReport")
    public R getFinanceReport(HttpServletRequest request, @RequestBody HashMap<String, Object> param) {
        SysUser currentUser = this.getSysUser();
        String mgrNum = currentUser.getUserName();
        Long role = currentUser.getRoles().get(0).getRoleId();
        if (5L == role) {
            String departs = getMgrdDptSet(mgrNum); // 所有管辖部门及其子部门
            param.put("depts", departs);
        }

//		String fromMonth = null;
//		String toMonth = null;
        Map<String, Object> results = new HashMap<String, Object>();
        List<Map<String, Object>> historyFinanceReport = new ArrayList<Map<String, Object>>();
        if (null != param.get("fromMonth") && null != param.get("toMonth")) {
            String dptId = "" + param.get("departmentId");
            Long departmentId = null;
            if (StringUtils.isNotBlank(dptId) && !"null".equals(dptId)) {
                departmentId = 0L + Integer.valueOf(dptId.trim());
            }
            param.put("departmentId", departmentId);
            String prjDptId = "" + param.get("projectDepartmentId");
            Long projectDepartmentId = null;
            if (StringUtils.isNotBlank(prjDptId) && !"null".equals(prjDptId)) {
                projectDepartmentId = 0L + Integer.valueOf(prjDptId.trim());
            }
            param.put("projectDepartmentId", projectDepartmentId);
            String prjId = "" + param.get("projectId");
            Long projectId = null;
            if (StringUtils.isNotBlank(prjId) && !"null".equals(prjId)) {
                projectId = 0L + Integer.valueOf(prjId.trim());
            }
            param.put("projectId", projectId);

            String compId = "" + param.get("companyId");
            Long companyId = null;
            if (StringUtils.isNotBlank(compId) && !"null".equals(compId)) {
                companyId = 0L + Integer.valueOf(compId.trim());
            }
            param.put("companyId", companyId);
            String prjCompId = "" + param.get("projectCompanyId");
            Long projectCompanyId = null;
            if (StringUtils.isNotBlank(prjCompId) && !"null".equals(prjCompId)) {
                projectCompanyId = 0L + Integer.valueOf(prjCompId.trim());
            }
            param.put("projectCompanyId", projectCompanyId);
            String empKeyword = "" + param.get("empKeyword");
            if (StringUtils.isNotBlank(empKeyword) && !"null".equals(empKeyword)) {
                empKeyword = empKeyword.trim();
            } else {
                empKeyword = null;
            }
            param.put("empKeyword", empKeyword);

            int pageNum = (Integer) param.get("pageNum");
            int pageSize = (Integer) param.get("pageSize");
            param.remove("pageSize");
            param.remove("pageNum");
            List<Map<String, Object>> report = reportService.getHistoryFinanceReport(param);
            int total = report.size();
            logger.info("report size:" + total);
            int start = pageSize * (pageNum - 1);
            int end = (pageSize * pageNum) - 1;
            for (int i = start; i <= end && i < report.size(); i++) {
                Map<String, Object> record = report.get(i);
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("employeeNum", "" + record.get("employeeNum"));
                item.put("employeeName", "" + record.get("employeeName"));
                item.put("assignType", "" + record.get("assignType"));
                item.put("projectName", "" + record.get("assignProjectName"));
                item.put("projectNum",
                        (null == record.get("assignProjectNum") ? "" : "" + record.get("assignProjectNum")));
                item.put("employeeDepartment", "" + record.get("employeeDepartmentName"));
                item.put("tsMonth", "" + record.get("tsMonth"));
                item.put("attendanceDayHours", "" + record.get("inputHours"));
                historyFinanceReport.add(item);
            }

            results.put("reportData", historyFinanceReport);
            results.put("total", total);
            return R.data(results);
        } else {
            results.put("reportData", historyFinanceReport);
            results.put("total", 0);
            results.put("msg", "起止月份必选。");
            return R.data(results);
//			return R.error(424, "起止日期必选。", null);
        }
    }

    /**
     * * 导出 员工财务工时快照数据报表
     *
     * @param @return @throws
     */
    @PostMapping("exportFinanceReport")
    public void exportFinanceReport(HttpServletRequest request, HttpServletResponse response,
                                    @RequestBody HashMap<String, Object> param) throws Exception {
        SysUser currentUser = this.getSysUser();
        String mgrNum = currentUser.getUserName();
        Long role = currentUser.getRoles().get(0).getRoleId();
        if (5L == role) {
            String departs = getMgrdDptSet(mgrNum); // 所有管辖部门及其子部门
            param.put("depts", departs);
        }

        String dptId = "" + param.get("departmentId");
        Long departmentId = null;
        if (StringUtils.isNotBlank(dptId) && !"null".equals(dptId)) {
            departmentId = 0L + Integer.valueOf(dptId.trim());
        }
        param.put("departmentId", departmentId);
        String prjDptId = "" + param.get("projectDepartmentId");
        Long projectDepartmentId = null;
        if (StringUtils.isNotBlank(prjDptId) && !"null".equals(prjDptId)) {
            projectDepartmentId = 0L + Integer.valueOf(prjDptId.trim());
        }
        param.put("projectDepartmentId", projectDepartmentId);
        String prjId = "" + param.get("projectId");
        Long projectId = null;
        if (StringUtils.isNotBlank(prjId) && !"null".equals(prjId)) {
            projectId = 0L + Integer.valueOf(prjId.trim());
        }
        param.put("projectId", projectId);

        String compId = "" + param.get("companyId");
        Long companyId = null;
        if (StringUtils.isNotBlank(compId) && !"null".equals(compId)) {
            companyId = 0L + Integer.valueOf(compId.trim());
        }
        param.put("companyId", companyId);
        String prjCompId = "" + param.get("projectCompanyId");
        Long projectCompanyId = null;
        if (StringUtils.isNotBlank(prjCompId) && !"null".equals(prjCompId)) {
            projectCompanyId = 0L + Integer.valueOf(prjCompId.trim());
        }
        param.put("projectCompanyId", projectCompanyId);
        String empKeyword = "" + param.get("empKeyword");
        if (StringUtils.isNotBlank(empKeyword) && !"null".equals(empKeyword)) {
            empKeyword = empKeyword.trim();
        } else {
            empKeyword = null;
        }
        param.put("empKeyword", empKeyword);

        List<Map<String, Object>> reportRecords = reportService.getHistoryFinanceReport(param);

        String fileName = "ExportFinanceReport.xlsx";
        XSSFWorkbook wb = null;
        try {
//			ClassPathResource cpr = new ClassPathResource("doc/" + fileName);
//			wb = new XSSFWorkbook(cpr.getInputStream());

            wb = reportService.exportHistoryFinanceReport(reportRecords);
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//
//			response.setContentType("application/vnd.ms-excel;charset=UTF-8");
//			response.setHeader("Content-Disposition","attachment;filename="+new String("人员统计表.xls".getBytes(),"UTF-8"));
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
            response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, accept, content-type, xxxx");
            response.setHeader("Access-Control-Allow-Methods", "POST");
            response.setHeader("Access-Control-Allow-Credentials", "true");

            OutputStream out = response.getOutputStream();
            wb.write(out);

            out.close();
        } catch (Exception e) {
            logger.error("导出Excel出现异常:", e);
        }
    }

    private String getMgrdDptSet(String mgrNum) {
        List<Department> dpts = dashboardService.getAllMgrDepartments(mgrNum); // 所有管辖部门及其子部门
        String departs = "";
        if (null != dpts && dpts.size() > 0) {
            for (Department dpt : dpts) {
                departs = departs + "," + dpt.getDepartmentId();
            }
            departs = departs.substring(1);
        }
        return departs;
    }
}
