package com.vendetech.project.tsms.project.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vendetech.framework.web.controller.BaseController;
import com.vendetech.framework.web.page.TableDataInfo;
import com.vendetech.project.tsms.domain.Company;
import com.vendetech.project.tsms.domain.Department;
import com.vendetech.project.tsms.domain.Project;
import com.vendetech.project.tsms.domain.R;
import com.vendetech.project.tsms.project.controller.dto.CreateProjectDTO;
import com.vendetech.project.tsms.project.controller.dto.ProjectDTO;
import com.vendetech.project.tsms.project.controller.dto.ProjectExcelDTO;
import com.vendetech.project.tsms.project.service.ProjectService;
import com.vendetech.project.tsms.utils.ExcelUtils;

@RestController
@RequestMapping("project")
public class ProjectController extends BaseController {
	protected final Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private ProjectService projectService;

	/**
	 * 项目列表
	 * 
	 * @return
	 */
	@GetMapping(value = "getProjectList")
	public TableDataInfo list(ProjectDTO projectDTO) {
		// 开始分页
		startPage();
		// 列表展示
		List<HashMap<String, Object>> lists = projectService.selectByProjectList(projectDTO.getKeyword());
		return getDataTable(lists);
	}

	/**
	 * 公司下拉
	 * 
	 * @return
	 */
	@GetMapping(value = "getCompanyNameList")
	public R getCompanyNameList() {
		List<Company> companyNameList = projectService.getCompanyNameList();
		return R.data(companyNameList);
	}

	/**
	 * 部门下拉
	 * 
	 * @return
	 */
	@GetMapping(value = "getDepartMentNameList")
	public R getDepartMentNameList() {
		List<Department> departMentNameList = projectService.getDepartMentNameList();
		return R.data(departMentNameList);
	}

	/**
	 * 添加项目
	 * 
	 * @param createProjectDTO
	 * @return
	 */
	@PostMapping("saveProject")
	@ResponseBody
	public R saveProject(@RequestBody CreateProjectDTO createProjectDTO) {
		// 查询项目编码 判断是否已存在
		List<Project> projects = projectService.selectByProjectNum(createProjectDTO.getProjectNum());
		if (null == projects || projects.size() == 0) {
			projectService.insert(createProjectDTO);
			return R.ok("添加成功");
		} else {
			return R.error("该项目编码重复");
		}
	}

	/**
	 * 修改项目
	 * 
	 * @param createProjectDTO
	 * @return
	 */
	@PutMapping(value = "updateProjectList")
	@ResponseBody
	public R updateProjectList(@RequestBody CreateProjectDTO createProjectDTO) {
		if (createProjectDTO != null) {
			projectService.updateByPrimaryKey(createProjectDTO);
			return R.ok("修改成功");
		}
		return R.error("修改失败");
	}

	/**
	 * 修改项目回显列表
	 * 
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "modifyProjectList", method = RequestMethod.GET)
	public R modifyProjectList(Long projectId) {
		if (projectId != 0) {
			Project project = projectService.selectByProjectIdList(projectId);
			return R.data(project);
		}
		return R.error("查询失败");
	}

	/**
	 ** 下载 员工考勤信息
	 *
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@PostMapping("downloadProjectTemplate")
	public void downloadAttendanceRecordsTemplate(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		XSSFWorkbook wb = null;
		String fileName = "Project_Import_template.xlsx";
		try {
			// excel模板路径
			Resource res = new ClassPathResource("doc/" + fileName);
//			File fi = ResourceUtils.getFile("classpath:doc/" + fileName);
			// 读取excel模板
			wb = new XSSFWorkbook(res.getInputStream());

			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

			OutputStream out = response.getOutputStream();
			wb.write(out);

			out.close();
		} catch (Exception e) {
			logger.error("下载项目模板出现异常：", e);
		}
	}

	/**
	 * 导入 查询 员工信息
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	@PostMapping("excelProject")
	public R getExcelData(@RequestParam MultipartFile file) throws IOException, InvalidFormatException {
		List<String[]> excelData = ExcelUtils.getExcel(file);
		List<ProjectExcelDTO> projectExcelDTOS = new ArrayList<ProjectExcelDTO>();
		for (int i = 0; i < excelData.size(); i++) {
			String[] projectExcel = excelData.get(i);
			ProjectExcelDTO projectExcelDTO = null;
            try {
                for (int j = 0; j < projectExcel.length - 1; j++) {
                    projectExcelDTO = new ProjectExcelDTO();
                    projectExcelDTO.setProjectNum(projectExcel[0].trim());
                    projectExcelDTO.setProjectName(projectExcel[1].trim());
                    projectExcelDTO.setProjectManager(projectExcel[3].trim());
                    projectExcelDTO.setProjectLeader(projectExcel[4].trim());
                    projectExcelDTO.setStatus(projectExcel[5].trim());
                    projectExcelDTO.setContractStatus(projectExcel[6].trim());
                    projectExcelDTO.setDepartmentNum(projectExcel[7].trim());
                    projectExcelDTO.setDepartmentName(projectExcel[8].trim());
                    projectExcelDTO.setCompanyName(projectExcel[9].trim());
                    projectExcelDTO.setEmployeeNum(projectExcel[2].trim());
                    projectExcelDTO.setCustomerName(projectExcel[10].trim());
                    projectExcelDTO.setCustomerNum(projectExcel[11].trim());
                }
            } catch (Exception e) {
                return R.error("请按照模板的规则导入");
            }
            projectExcelDTOS.add(projectExcelDTO);
		}
		List<String> projectCode = new ArrayList<>();
		List<ProjectExcelDTO> dtoList = new ArrayList<ProjectExcelDTO>();
		for (int i = 0; i < projectExcelDTOS.size(); i++) {
			ProjectExcelDTO projectExcelDTO = projectExcelDTOS.get(i);
			String projectNum = projectExcelDTO.getProjectNum();
			projectCode.add(projectNum);
			if (projectExcelDTO.getProjectManager() != null && !projectExcelDTO.getProjectManager().equals("")
					&& projectExcelDTO.getEmployeeNum() != null && !projectExcelDTO.getEmployeeNum().equals("")
					&& projectExcelDTO.getDepartmentName() != null && !projectExcelDTO.getDepartmentName().equals("")
					&& projectExcelDTO.getDepartmentNum() != null && !projectExcelDTO.getDepartmentNum().equals("")
					&& projectExcelDTO.getCompanyName() != null && !projectExcelDTO.getCompanyName().equals("")
					&& projectExcelDTO.getProjectNum() != null && !projectExcelDTO.getProjectNum().equals("")
					&& projectExcelDTO.getProjectName() != null && !projectExcelDTO.getProjectName().equals("")) {
				List<ProjectExcelDTO> projectExcelDTOS1 = projectService.selecprojectManager(projectExcelDTO);
				if (CollectionUtils.isEmpty(projectExcelDTOS1)) {
					projectExcelDTO.setProjectManagerRemark("第"+(i + 2) + "行："+"项目经理与工号不匹配");
				} else {
					List<ProjectExcelDTO> projectExcelDTOS3 = projectService.selectByExcelByRoleId(projectExcelDTO);
					if (CollectionUtils.isEmpty(projectExcelDTOS3)) {
						projectExcelDTO.setProjectManagerRemark("第"+(i + 2) + "行："+"该员工不是项目经理");
					}
				}
				List<ProjectExcelDTO> projectExcelDTOS2 = projectService.getDepartment(projectExcelDTO);
				if (CollectionUtils.isEmpty(projectExcelDTOS2)) {
					projectExcelDTO.setDepartmentNameRemark("第"+(i + 2) + "行："+"部门编号与部门名称不匹配");
				} else {
					Department department = projectService.getDepartMentId(projectExcelDTO.getDepartmentNum());
					if (department.getDepartmentId() != null) {
						projectExcelDTO.setProjectDepartmentId(department.getDepartmentId());
					} else {
						projectExcelDTO.setDepartmentNameRemark("第"+(i + 2) + "行："+"项目所属部门不符合规则");
					}
				}
				Company company = projectService.getCompanyId(projectExcelDTO.getCompanyName());
				if (company != null) {
					projectExcelDTO.setProjectCompanyId(company.getCompanyId());
				} else {
					projectExcelDTO.setCompanyNameRemark("第"+(i + 2) + "行："+"项目所属公司不符合规则");
				}
				if (projectExcelDTO.getStatus().equals("正常")) {
					projectExcelDTO.setStatus("1");
				} else if (projectExcelDTO.getStatus().equals("关闭")) {
					projectExcelDTO.setStatus("0");
				} else {
					projectExcelDTO.setStatusRemark("第"+(i + 2) + "行："+"状态不符合规则");
				}
				
				if (projectExcelDTO.getContractStatus().equals("已签")) {
					projectExcelDTO.setContractStatus("1");
				} else if (projectExcelDTO.getContractStatus().equals("待签")) {
					projectExcelDTO.setContractStatus("0");
				} else {
					projectExcelDTO.setContractStatusRemark("第"+(i + 2) + "行："+"合同状态不符合规则");
				}
				
			} else {
				projectExcelDTO.setProjectNumRemark("第"+(i + 2) + "行："+"*号为必填选项");
			}		
		}
		for(ProjectExcelDTO projectExcelDTO1:projectExcelDTOS){
			if(projectExcelDTO1.getProjectNumRemark()!=null){
				dtoList.add(projectExcelDTO1);
			}
		}
		if(CollectionUtils.isNotEmpty(dtoList)){
			return R.error(500,"导入错误",dtoList);
		}
		HashSet<String> set = new HashSet<>(projectCode);
		Boolean result = set.size() == projectCode.size() ? true : false;
		if (result) {
			List<ProjectExcelDTO> lists = new ArrayList<ProjectExcelDTO>();
			for (ProjectExcelDTO projectExcelDTO : projectExcelDTOS) {
				List<Project> projects = projectService.selectByNum(projectExcelDTO.getProjectNum());
				if (projects.size() != 0) {
					projectExcelDTO.setProjectNumRemark("项目编码已经存在");
				}
				if (projectExcelDTO.getCompanyNameRemark() != null || projectExcelDTO.getDepartmentNameRemark() != null
						|| projectExcelDTO.getStatusRemark() != null || projectExcelDTO.getProjectManagerRemark() != null
						|| projectExcelDTO.getProjectNumRemark() != null || projectExcelDTO.getContractStatusRemark() != null
				) {
					lists.add(projectExcelDTO);
				}
			}
			if (CollectionUtils.isNotEmpty(lists)) {
				return R.error(500, "上传失败", lists);
			}
			if (CollectionUtils.isEmpty(lists)) {
				projectService.saveExcelProject(projectExcelDTOS);
				return R.ok("上传成功");
			}
			return R.error("上传失败");
		} else {
			return R.error("项目编码不唯一");
		}
	}

	/***
	 * 导出
	 * @param request
	 * @param response
	 * @param projectDTO
	 * @throws Exception
	 */
	@PostMapping("exportByKeyWord")
	public void exportById(HttpServletRequest request, HttpServletResponse response, @RequestBody ProjectDTO projectDTO) throws Exception {
		response.reset(); //清除buffer缓存
		//Map<String,Object> map=new HashMap<String,Object>();
		// 指定下载的文件名
		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition","attachment;filename="+new String("项目导出模板.xls".getBytes(),"UTF-8"));
		response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
		response.setHeader("Access-Control-Allow-Headers", "X-Requested-With, accept, content-type, xxxx");
		response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS, PATCH");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		//导出Excel对象
		XSSFWorkbook workbook = null;
		try {
			workbook = projectService.exportExcelInfoBykeWord(projectDTO.getKeyword());
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
