package com.vendetech.project.tsms.timesheet.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vendetech.project.tsms.domain.Department;
import com.vendetech.project.tsms.domain.EmpTsAttdAsgnRecord;
import com.vendetech.project.tsms.domain.Employee;
import com.vendetech.project.tsms.project.mapper.DepartmentMapper;
import com.vendetech.project.tsms.timesheet.mapper.EmpTsAttdAsgnRecordMapper;
import com.vendetech.project.tsms.timesheet.service.DashboardService;
import com.vendetech.project.tsms.user.mapper.EmployeeMapper;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	EmpTsAttdAsgnRecordMapper empTsAttdAsgnRecordMapper;

	@Autowired
	EmployeeMapper employeeMapper;

	@Autowired
	DepartmentMapper departmentMapper;

	@Override
	public String getChildrenDepts(String mainDdDepartmentId) {
		return departmentMapper.getChildrenDepts(mainDdDepartmentId);
	}

	@Override
	public Integer getNeedAssignHours(Map<String, Object> params) {
		Integer totalHours = 0;
		String fromDate = (String) params.get("fromDate");
		String toDate = (String) params.get("toDate");
//		String mainBuDdDepartmentId = (String) params.get("mainBuDdDepartmentId");
		String depts = (String) params.get("depts");
		// 
		totalHours = empTsAttdAsgnRecordMapper.getNeedAssignHours(depts, fromDate, toDate);
		return totalHours;
	}

	@Override
	public Integer getAtcualAssignHours(Map<String, Object> params) {
		Integer totalHours = 0;
		String fromDate = (String) params.get("fromDate");
		String toDate = (String) params.get("toDate");
//		String mainBuDdDepartmentId = (String) params.get("mainBuDdDepartmentId");
		String depts = (String) params.get("depts");
		//
		totalHours = empTsAttdAsgnRecordMapper.getAtcualAssignHours(depts, fromDate, toDate);
		return totalHours;
	}

	@Override
	public List<Department> getLctdDepartments(String mgrNum) {
		List<Department> departments = departmentMapper.getLctdDepartments(mgrNum); // 下属所有部门
		return departments;
	}

	@Override
	public List<Department> getMgrdDepartments(String mgrNum) {
		List<Department> departments = departmentMapper.getMgrdDepartments(mgrNum); // 所有管理部门
		return departments;
	}

	@Override
	public List<Department> getAllMgrDepartments(String mgrNum) {
//		List<Department> departments = departmentMapper.getLctdDepartments(mgrNum); // 下属所有部门
		List<Department> departments = departmentMapper.getMgrdDepartments(mgrNum); // 所有管理部门
		Map<String, Department> uniqueDepartments = new HashMap<String, Department>();
		for (Department dpt : departments) {
			String dptNum = dpt.getDepartmentNum();
			if (null == uniqueDepartments.get(dptNum)) {
				uniqueDepartments.put(dptNum, dpt);
			}
			List<Department> dpts = departmentMapper.getChildrenDeparments(dpt.getDdDepartmentId());
			for (Department subdpt : dpts) {
				String subdptNum = subdpt.getDepartmentNum();
				if (null == uniqueDepartments.get(subdptNum)) {
					uniqueDepartments.put(subdptNum, subdpt);
				}
			}
		}
		return new ArrayList<Department>(uniqueDepartments.values());
	}

	@Override
	public List<Employee> getMgrEmployees(String mgrNum) {
		Map<String, Employee> employees = new HashMap<String, Employee>();
		// employeeMapper.getMgrEmployees(empNum); // 下属所有员工
		List<Department> departments = getAllMgrDepartments(mgrNum);
		for (Department dpt : departments) {
			String dptNum = dpt.getDepartmentNum();
			if (null != dptNum && StringUtils.isNotEmpty(dptNum.trim())) {
				List<Employee> emps = employeeMapper.getDepartmentEmployees(dptNum);
				for (Employee emp : emps) {
					String empNum = emp.getEmployeeNum();
					if (null == employees.get(empNum)) {
						employees.put(empNum, emp);
					}
				}
			}
		}
		return new ArrayList<Employee>(employees.values());
	}

	@Override
	public Department getEmpMainDepartment(String empNum) {
		List<Employee> emps = employeeMapper.selectByEmpNum(empNum);
		Long departmentId = null;
		Department department = null;
		if (null != emps & emps.size() > 0) {
			departmentId = emps.get(0).getMainDeptId();
		}
		if (null != departmentId) {
			department = departmentMapper.selectByPrimaryKey(departmentId); // 主部门
		}
		return department;
	}

	@Override
	public Department getEmpMainBuDepartment(String empNum) {
		Department buDept = null; 
		Department mainDept = getEmpMainDepartment(empNum);
		if (null != mainDept) {
			String mainDdDepartmentId = mainDept.getDdDepartmentId();
			List<Department> departments = departmentMapper.getEmpMainBuDepartment(mainDdDepartmentId); // 主部门所在事业部
			if(null != departments & departments.size() > 0) {
				buDept = departments.get(0);
			}
		}
		return buDept;
	}

	@Override
	public List<Employee> getMainBuDeptEmployees(String mainBuDdDepartmentId) {
		Map<String, Employee> employees = new HashMap<String, Employee>();
		// 主部门所在事业部下属所有员工
//		List<Department> departments = departmentMapper.getChildrenDeparments(mainBuDdDepartmentId);
//		for (Department dpt : departments) {
//			String dptNum = dpt.getDepartmentNum();
//			if (null != dptNum && StringUtils.isNotEmpty(dptNum.trim())) {
//				List<Employee> emps = employeeMapper.getDepartmentEmployees(dptNum);
//				for (Employee emp : emps) {
//					String empNum = emp.getEmployeeNum();
//					if (null == employees.get(empNum)) {
//						employees.put(empNum, emp);
//					}
//				}
//			}
//		}
		return new ArrayList<Employee>(employees.values());
	}
		
	@Override
	public List<EmpTsAttdAsgnRecord> getUnmatchList(Map<String, Object> params) {
		List<EmpTsAttdAsgnRecord> list = new ArrayList<EmpTsAttdAsgnRecord>();
		String fromDate = (String) params.get("fromDate");
		String toDate = (String) params.get("toDate");
//		String mainBuDdDepartmentId = (String) params.get("mainBuDdDepartmentId");
		String depts = (String) params.get("depts");
//		List<Employee> emps = getMainBuDeptEmployees(mainBuDdDepartmentId);
		list = empTsAttdAsgnRecordMapper.selectUnmatchList(depts, fromDate, toDate);
		return list;
	}

	@Override
	public List<EmpTsAttdAsgnRecord> getUnmatchListByMonth(Map<String, Object> params) {
		List<EmpTsAttdAsgnRecord> list = new ArrayList<EmpTsAttdAsgnRecord>();
		Integer year = (Integer) params.get("year");
		Integer month = (Integer) params.get("month");
		String mgrNum = (String) params.get("mgrNum");
		Department buDept = getEmpMainBuDepartment(mgrNum);
		String depts = null;
		if (null != buDept) {
			if (null != buDept.getDdDepartmentId()) {
				depts = getChildrenDepts(buDept.getDdDepartmentId());
			}
		}
		list = empTsAttdAsgnRecordMapper.selectUnmatchListByDeptsMonth(depts,
				year + "", (month < 10 ? "0" + (month - 1) : "" + (month - 1)));
		return list;
	}

	@Override
	public Department getDepartmentById(Long departmentId) {
		return departmentMapper.selectByPrimaryKey(departmentId);
	}

	@Override
	public Integer countStaff(String depts) {
		Integer qty = 0;
		qty = empTsAttdAsgnRecordMapper.countStaff(depts);
		return qty;
	}

}
