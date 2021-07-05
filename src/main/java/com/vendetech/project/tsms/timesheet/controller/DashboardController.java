package com.vendetech.project.tsms.timesheet.controller;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
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
import org.springframework.web.bind.annotation.RestController;

import com.vendetech.framework.web.controller.BaseController;
import com.vendetech.project.system.domain.SysUser;
import com.vendetech.project.system.service.ISysDeptService;
import com.vendetech.project.tsms.domain.Department;
import com.vendetech.project.tsms.domain.EmpTsAttdAsgnRecord;
import com.vendetech.project.tsms.domain.R;
import com.vendetech.project.tsms.timesheet.service.DashboardService;
import com.vendetech.project.tsms.utils.DateUtil;

@RestController
@RequestMapping("timesheet")
public class DashboardController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(DashboardController.class);

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private ISysDeptService deptService;
//
//	@Autowired
//	private EmployeeService empService;

	@GetMapping("/getDeptList")
	public R getTimeSheetDept() {
		SysUser currentUser = this.getSysUser();
		String mgrNum = currentUser.getUserName();
		Long role = currentUser.getRoles().get(0).getRoleId();
		Department buDept = dashboardService.getEmpMainBuDepartment(mgrNum);
		List<Department> depts = null;
		if (5L == role) {
			if (null != buDept) {
				depts = deptService.getDepartmentList(buDept.getDdDepartmentId());
			}
		} else {
			depts = deptService.getDepartmentList("1");
		}
		return toResult(deptService.buildDepts(depts));
	}

	/**
	 * -工时看板
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getDashboardSummary")
	public R getDashboardSummary(HttpServletRequest request, @RequestBody HashMap<String, Object> param) {
		SysUser currentUser = this.getSysUser();
		String mgrNum = currentUser.getUserName(); // "V00000670"; // "V00000397"; //
		Long role = currentUser.getRoles().get(0).getRoleId();
		Map<String, Object> results = new HashMap<String, Object>();

//		List<Department> departments = dashboardService.getLctdDepartments(mgrNum); // 下属所有部门 user.getEmployeeId()
//		List<Employee> emps = empService.getOneByEmployeeNum(mgrNum);
//		String ddUserId = emps.get(0).getDdUserid();
		List<Department> departments = dashboardService.getMgrdDepartments(mgrNum); // 所有管辖部门
		List<Department> dpts = dashboardService.getAllMgrDepartments(mgrNum); // 所有管辖部门及其子部门
		String deptNames = "";
		if (null != departments && departments.size() > 0) {
			for (Department dpt : departments) {
				deptNames = deptNames + "," + dpt.getDepartmentName();
			}
			deptNames = deptNames.substring(1);
		}
		String departs = "";
		if (null != dpts && dpts.size() > 0) {
			for (Department dpt : dpts) {
				departs = departs + "," + dpt.getDepartmentId();
			}
			departs = departs.substring(1);
		}
//		List<Employee> employees = dashboardService.getMgrEmployees(mgrNum); // 下属所有员工

		Department root = null;
		if (4L == role) {
			if (null != param.get("departmentId")) {
				Long departmentId = 0L + Integer.valueOf("" + param.get("departmentId"));
				if (null != departmentId) {
					root = dashboardService.getDepartmentById(departmentId);
				}
			}
		} else if (5L == role) {
			if (null != param.get("departmentId")) {
				Long departmentId = 0L + Integer.valueOf("" + param.get("departmentId"));
				if (null != departmentId) {
					root = dashboardService.getDepartmentById(departmentId);
				}
			}
		} else {
			root = dashboardService.getEmpMainBuDepartment(mgrNum); // 所在主部门的事业部
			if (null == root) {
				root = new Department();
			}
		}
		String departmentName = "";
		String rootDdDepartmentId = null;
		String depts = null;
		if (null != root) {
			if (null != root.getDdDepartmentId()) {
				departmentName = root.getDepartmentNum() + " (" + root.getDepartmentName() + ")";
				rootDdDepartmentId = root.getDdDepartmentId();
				depts = dashboardService.getChildrenDepts(rootDdDepartmentId);
			} else {
//				rootDdDepartmentId = "-1";
				depts = "";
			}
		} else {
			if (4L == role) {
				departmentName = "全体在职";
			} else {
				if (StringUtils.isNotBlank(deptNames)) {
					departmentName = deptNames;
					depts = departs;
				}
			}
		}
		Integer staffQty = dashboardService.countStaff(depts); // 下属所有员工数量
		param.put("depts", depts);
//		param.put("depts", departs);

		List<Map<String, String>> unmatchList = new ArrayList<Map<String, String>>();
//		param.put("mgrNum", mgrNum);
		Integer needAssignHours = dashboardService.getNeedAssignHours(param);
		Integer atcualAssignHours = dashboardService.getAtcualAssignHours(param);
		List<EmpTsAttdAsgnRecord> unmatchRecords = dashboardService.getUnmatchList(param);

		int pageNum = (Integer) param.get("pageNum");
		int pageSize = (Integer) param.get("pageSize");
		int start = pageSize * (pageNum - 1);
		int end = (pageSize * pageNum) - 1;
		for (int i = start; i <= end && i < unmatchRecords.size(); i++) {
			EmpTsAttdAsgnRecord record = unmatchRecords.get(i);
			Map<String, String> unmatch = new HashMap<String, String>();
			unmatch.put("empNum", record.getEmployeeNum());
			unmatch.put("empName", record.getEmployeeName());
			unmatch.put("attdDate", DateUtil.format(record.getTsDate(), DateUtil.PATTERN_DATE));
			unmatch.put("unmatchResult", null == record.getUnmatchResult() ? "未配" : record.getUnmatchResult());
			unmatchList.add(unmatch);
		}
		int total = unmatchRecords.size();

		results.put("departs", departmentName);
		results.put("empQty", staffQty);
		results.put("needAssignHours", needAssignHours);
		results.put("atcualAssignHours", atcualAssignHours);
		System.out.println("needAssignHours:" + needAssignHours);
		System.out.println("atcualAssignHours:" + atcualAssignHours);
		// 开始分页
//		startPage();
		// 列表展示
		results.put("unmatchList", unmatchList);
		System.out.println("unmatchList:" + unmatchList.size());
		results.put("total", total);
		return R.data(results);
	}

	/**
	 * -导出工时看板
	 * 
	 * @return
	 */
	@PostMapping("exportUnmatchedRecords")
	public void exportUnmatchedRecords(HttpServletRequest request, HttpServletResponse response,
			@RequestBody HashMap<String, Object> param) throws Exception {
		SysUser currentUser = this.getSysUser();
		String mgrNum = currentUser.getUserName();
		Long role = currentUser.getRoles().get(0).getRoleId();

//		List<Department> departments = dashboardService.getLctdDepartments(mgrNum); // 下属所有部门(user.getEmployeeId()
//		String departs = "";
//		if (null != departments && departments.size() > 0) {
//			for (Department dpt : departments) {
//				departs = departs + "," + dpt.getDepartmentId();
//			}
//			departs = departs.substring(1);
//		}
		List<Department> dpts = dashboardService.getAllMgrDepartments(mgrNum); // 所有管辖部门及其子部门
		String departs = "";
		if (null != dpts && dpts.size() > 0) {
			for (Department dpt : dpts) {
				departs = departs + "," + dpt.getDepartmentId();
			}
			departs = departs.substring(1);
		}
//		param.put("depts", departs);

		List<Map<String, String>> unmatchList = new ArrayList<Map<String, String>>();
//		param.put("mgrNum", mgrNum);

		Department root = null;
//		if (4L == role || 5L == role) {
//			if (null != param.get("departmentId")) {
//				Long departmentId = 0L + Integer.valueOf("" + param.get("departmentId"));
//				if (null != departmentId) {
//					root = dashboardService.getDepartmentById(departmentId);
//				}
//			}
//		} else {
//			root = dashboardService.getEmpMainBuDepartment(mgrNum); // 所在主部门的事业部
//			if (null == root) {
//				root = new Department();
//			}
//		}
//		String rootDdDepartmentId = null;
//		String depts = null;
//		if (null != root) {
//			if (null != root.getDdDepartmentId()) {
//				rootDdDepartmentId = root.getDdDepartmentId();
//				depts = dashboardService.getChildrenDepts(rootDdDepartmentId);
//			} else {
//				depts = "";
//			}
//		}
		if (4L == role) {
			if (null != param.get("departmentId")) {
				Long departmentId = 0L + Integer.valueOf("" + param.get("departmentId"));
				if (null != departmentId) {
					root = dashboardService.getDepartmentById(departmentId);
				}
			}
		} else if (5L == role) {
			if (null != param.get("departmentId")) {
				Long departmentId = 0L + Integer.valueOf("" + param.get("departmentId"));
				if (null != departmentId) {
					root = dashboardService.getDepartmentById(departmentId);
				}
			}
		} else {
			root = dashboardService.getEmpMainBuDepartment(mgrNum); // 所在主部门的事业部
			if (null == root) {
				root = new Department();
			}
		}
		String rootDdDepartmentId = null;
		String depts = null;
		if (null != root) {
			if (null != root.getDdDepartmentId()) {
				rootDdDepartmentId = root.getDdDepartmentId();
				depts = dashboardService.getChildrenDepts(rootDdDepartmentId);
			} else {
//				rootDdDepartmentId = "-1";
				depts = "";
			}
		} else {
			if (4L == role) {
//				departmentName = "全体在职";
			} else {
				if (StringUtils.isNotBlank(departs)) {
					depts = departs;
				}
			}
		}
		param.put("depts", depts);
		
		List<EmpTsAttdAsgnRecord> unmatchRecords = dashboardService.getUnmatchList(param);
		for (int i = 0; i < unmatchRecords.size(); i++) {
			EmpTsAttdAsgnRecord record = unmatchRecords.get(i);
			Map<String, String> unmatch = new HashMap<String, String>();
			unmatch.put("empNum", record.getEmployeeNum());
			unmatch.put("empName", record.getEmployeeName());
			unmatch.put("attdDate", DateUtil.format(record.getTsDate(), DateUtil.PATTERN_DATE));
			unmatch.put("unmatchResult", null == record.getUnmatchResult() ? "未配" : record.getUnmatchResult());
			unmatchList.add(unmatch);
		}
		String fileName = "UnmatchTS_Export_template.xlsx";
		XSSFWorkbook wb = null;
		try {
			// excel模板路径
			Resource res = new ClassPathResource("doc/" + fileName);
//			File fi = ResourceUtils.getFile("classpath:doc/" + fileName);
			// 读取excel模板
			wb = new XSSFWorkbook(res.getInputStream());

			XSSFSheet sheet = wb.getSheetAt(0);
			for (int i = 0; i < unmatchList.size(); i++) {
				Map<String, String> record = unmatchList.get(i);
				XSSFRow row = sheet.getRow(i + 1);
				if (null == row) {
					row = sheet.createRow(i + 1);
				}
				XSSFCell cell1 = row.getCell(0);
				if (null == cell1) {
					cell1 = row.createCell(0);
				}
				cell1.setCellValue(record.get("empNum"));
				XSSFCell cell2 = row.getCell(1);
				if (null == cell2) {
					cell2 = row.createCell(1);
				}
				cell2.setCellValue(record.get("empName"));
				XSSFCell cell3 = row.getCell(2);
				if (null == cell3) {
					cell3 = row.createCell(2);
				}
				cell3.setCellValue(record.get("attdDate"));
				XSSFCell cell4 = row.getCell(3);
				if (null == cell4) {
					cell4 = row.createCell(3);
				}
				cell4.setCellValue(record.get("unmatchResult"));
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

}
