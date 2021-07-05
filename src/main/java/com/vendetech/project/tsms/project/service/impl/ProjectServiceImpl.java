package com.vendetech.project.tsms.project.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vendetech.project.tsms.domain.Company;
import com.vendetech.project.tsms.domain.Department;
import com.vendetech.project.tsms.domain.Project;
import com.vendetech.project.tsms.project.controller.dto.CreateProjectDTO;
import com.vendetech.project.tsms.project.controller.dto.ProjectExcelDTO;
import com.vendetech.project.tsms.project.mapper.CompanyMapper;
import com.vendetech.project.tsms.project.mapper.DepartmentMapper;
import com.vendetech.project.tsms.project.mapper.ProjectMapper;
import com.vendetech.project.tsms.project.service.ProjectService;
import com.vendetech.project.tsms.utils.ExportExcelUtil;
import com.vendetech.project.tsms.utils.dto.ExcelBean;

@Service
@Transactional
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectMapper projectMapper;

	@Autowired
	private CompanyMapper companyMapper;

	@Autowired
	private DepartmentMapper departmentMapper;

	/**
	 * 列表
	 * 
	 * @return
	 */
	@Override
	public List<HashMap<String, Object>> selectByProjectList(String param) {
		List<HashMap<String, Object>> maps = projectMapper.selectByProjectList(param);
		return maps;
	}

	/**
	 * 公司下拉
	 * 
	 * @return
	 */
	@Override
	public List<Company> getCompanyNameList() {
		return companyMapper.getCompanyNameList();
	}

	/**
	 * 部门下拉
	 * 
	 * @return
	 */
	@Override
	public List<Department> getDepartMentNameList() {
		return departmentMapper.getDepartMentNameList();
	}

	/**
	 *
	 * 根据项目编码 查询是否已经存在
	 * 
	 * @param projectNum
	 * @return
	 */
	@Override
	public List<Project> selectByProjectNum(String projectNum) {
		return projectMapper.selectByProjectNum(projectNum);
	}

	/**
	 * 添加项目
	 * 
	 * @param createProjectDTO
	 * @return
	 */
	@Override
	public int insert(CreateProjectDTO createProjectDTO) {
		Project project = new Project();
		project.setProjectName(createProjectDTO.getProjectName());
		project.setProjectNum(createProjectDTO.getProjectNum());
		project.setProjectManager(createProjectDTO.getProjectManager());
		project.setProjectLeader(createProjectDTO.getProjectLeader());
		project.setCustomerName(createProjectDTO.getCustomerName());
		project.setCustomerNum(createProjectDTO.getCustomerNum());
		project.setProjectCompanyId(createProjectDTO.getProjectCompanyId());
		project.setProjectDepartmentId(createProjectDTO.getProjectDepartmentId());
		project.setStatus(createProjectDTO.getStatus());
		project.setContractStatus(createProjectDTO.getContractStatus());
		projectMapper.insert(project);
		return 0;
	}

	/**
	 * 修改项目
	 * 
	 * @param createProjectDTO
	 * @return
	 */
	@Override
	public int updateByPrimaryKey(CreateProjectDTO createProjectDTO) {
		Long projectId = createProjectDTO.getProjectId();
		System.err.println(projectId);
		Project project = new Project();
		project.setProjectId(createProjectDTO.getProjectId());
		project.setProjectLeader(createProjectDTO.getProjectLeader());
		project.setProjectManager(createProjectDTO.getProjectManager());
		project.setCustomerName(createProjectDTO.getCustomerName());
		project.setCustomerNum(createProjectDTO.getCustomerNum());
		project.setProjectCompanyId(createProjectDTO.getProjectCompanyId());
		project.setProjectDepartmentId(createProjectDTO.getProjectDepartmentId());
		project.setStatus(createProjectDTO.getStatus());
		project.setContractStatus(createProjectDTO.getContractStatus());
		projectMapper.updateByPrimaryKey(project);
		return 0;
	}

	@Override
	public List<ProjectExcelDTO> selecprojectManager(ProjectExcelDTO projectExcelDTO) {
		return projectMapper.selectByExcelNickeName(projectExcelDTO);
	}

	@Override
	public List<ProjectExcelDTO> getDepartment(ProjectExcelDTO projectExcelDTO) {
		return projectMapper.getDepartment(projectExcelDTO);
	}

	@Override
	public List<ProjectExcelDTO> selectByExcelByRoleId(ProjectExcelDTO projectExcelDTO) {
		return projectMapper.selectByExcelByRoleId(projectExcelDTO);
	}

	@Override
	public int saveExcelProject(List<ProjectExcelDTO> projectExcelDTOS) {
		for (ProjectExcelDTO projectExcelDTO : projectExcelDTOS) {
			Project project = new Project();
			project.setProjectName(projectExcelDTO.getProjectName());
			project.setProjectNum(projectExcelDTO.getProjectNum());
			project.setProjectManager(projectExcelDTO.getEmployeeNum());
			project.setProjectLeader(projectExcelDTO.getProjectLeader());
			project.setCustomerName(projectExcelDTO.getCustomerName());
			project.setCustomerNum(projectExcelDTO.getCustomerNum());
			project.setProjectCompanyId(projectExcelDTO.getProjectCompanyId());
			project.setProjectDepartmentId(projectExcelDTO.getProjectDepartmentId());
			project.setStatus(projectExcelDTO.getStatus());
			project.setContractStatus(projectExcelDTO.getContractStatus());
			projectMapper.insert(project);
		}
		return 0;
	}

	@Override
	public Department getDepartMentId(String departmentNum) {
		return departmentMapper.getDepartMentId(departmentNum);
	}

	@Override
	public Company getCompanyId(String companyName) {
		return companyMapper.getCompanyId(companyName);
	}

	@Override
	public List<Project> selectByNum(String projectNum) {
		return projectMapper.selectByNum(projectNum);
	}

	@Override
	public XSSFWorkbook exportExcelInfoBykeWord(String keyword) throws Exception {
		// 根据条件查询数据
		List<Map<String, Object>> list = projectMapper.exportExcelInfoBykeWord(keyword);
		// System.out.println(list);
		List<ExcelBean> excel = new ArrayList<>();
		Map<Integer, List<ExcelBean>> map = new LinkedHashMap<>();
		// 设置标题栏
		excel.add(new ExcelBean("项目编码", "projectNum", 0));
		excel.add(new ExcelBean("项目名称", "projectName", 0));
		excel.add(new ExcelBean("项目经理工号", "projectManager", 0));
		excel.add(new ExcelBean("项目经理", "projectManagerName", 0));
		excel.add(new ExcelBean("负责人", "projectLeader", 0));
		excel.add(new ExcelBean("项目状态", "status", 0));
		excel.add(new ExcelBean("合同状态", "contractStatus", 0));
		excel.add(new ExcelBean("项目所属部门编号", "departmentNum", 0));
		excel.add(new ExcelBean("项目所属部门", "departmentName", 0));
		excel.add(new ExcelBean("项目所属公司", "companyName", 0));
		excel.add(new ExcelBean("客户名称", "customerName", 0));
		excel.add(new ExcelBean("客户编码", "customerNum", 0));
		map.put(0, excel);
		String sheetName = "项目信息";
		// 调用ExcelUtil方法
		XSSFWorkbook xssfWorkbook = null;
		xssfWorkbook = ExportExcelUtil.createExcelFile(Project.class, list, map, sheetName);
		System.out.println(xssfWorkbook);
		return xssfWorkbook;
	}

	/**
	 * 根据项目ID 查询列表
	 * 
	 * @param projectId
	 * @return
	 */
	@Override
	public Project selectByProjectIdList(Long projectId) {
		return projectMapper.selectByProjectIdList(projectId);
	}

}
