package com.vendetech.project.tsms.timesheetAssignment.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vendetech.project.tsms.domain.Employee;
import com.vendetech.project.tsms.timesheetAssignment.controller.DTO.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vendetech.framework.web.controller.BaseController;
import com.vendetech.framework.web.page.TableDataInfo;
import com.vendetech.project.system.domain.SysUser;
import com.vendetech.project.tsms.domain.Project;
import com.vendetech.project.tsms.domain.R;
import com.vendetech.project.tsms.domain.TimesheetAssignment;
import com.vendetech.project.tsms.timesheetAssignment.service.EmpTsAttdAsgnRecordService;
import com.vendetech.project.tsms.timesheetAssignment.service.TimesheetAssignmentService;
import com.vendetech.project.tsms.utils.ExcelUtils;

@RestController
@RequestMapping("timesheetAssignment")
public class TimesheetAssignmentController extends BaseController {

	@Autowired
	private TimesheetAssignmentService timesheetAssignmentService;

	@Autowired
	private EmpTsAttdAsgnRecordService empTsAttdAsgnRecordService;

	/**
	 * 工单列表
	 *
	 * @param timesheetAssignment
	 * @return
	 */
	@GetMapping("getTimesheetAssignment")
	public TableDataInfo getTimesheetAssignment(TimesheetAssignment timesheetAssignment) {
		startPage();
		SysUser currentUser = this.getSysUser();
		String userName = currentUser.getUserName();
		timesheetAssignment.setTsCreatedBy(userName);
		List<TimesheetAssignment> lists = timesheetAssignmentService.getTimesheetAssignment(timesheetAssignment);
		return getDataTable(lists);
	}

    /**
     * 项目模糊查询
     *
     * @return
     */
    @GetMapping("getStatusList")
    public R getStatusList(Project project) {
        SysUser currentUser = this.getSysUser();
        String loginName = currentUser.getUserName();
        project.setProjectManager(loginName);
        System.err.println(project);
        List<Project> statusList = timesheetAssignmentService.getStatusList(project);
        return R.data(statusList);
    }

    /**
     * 员工部门 模糊查询 员工信息
     * @param deparmentEmployeeDTO
     * @return
     */
    @GetMapping("getDeparmentEmployeeName")
    public R getDeparmentEmployeeName(DeparmentEmployeeDTO deparmentEmployeeDTO) {
        List<HashMap<String, Object>> hashMaps = timesheetAssignmentService.getDeparmentEmployeeName(
                deparmentEmployeeDTO.getKeyword());
        return R.data(hashMaps);
    }

    /**
     * 导入 查询 员工信息
     * @param file
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    @PostMapping("excelUtil")
	public R getExcelData(@RequestParam MultipartFile file) throws IOException, InvalidFormatException {
		List<String[]> excelData = ExcelUtils.getExcelData(file);
		List<EmployeeDTO> employeeList = new ArrayList<EmployeeDTO>();
		for (int i = 0; i < excelData.size(); i++) {
			String[] employee1 = excelData.get(i);
			EmployeeDTO employeeDTO = null;
			for (int j = 0; j < employee1.length - 1; j++) {
				String employeeName = employee1[1];
				String employeeNum = employee1[0];
				employeeDTO = new EmployeeDTO();
				employeeDTO.setEmployeeNum(employeeNum);
				employeeDTO.setEmployeeName(employeeName);
			}
			employeeList.add(employeeDTO);
		}
		List<EmployeeDTO> lists = new ArrayList<EmployeeDTO>();
		List<EmployeeDTO> lists1 = new ArrayList<EmployeeDTO>();
		for (int i = 0; i < employeeList.size(); i++) {
			EmployeeDTO emp = employeeList.get(i);
			String empNum = emp.getEmployeeNum();
			if (null != empNum && StringUtils.isNotBlank(empNum)) {
				List<Employee> oneByEmployeeNum = timesheetAssignmentService.getOneByEmployeeNum(empNum);
				if (CollectionUtils.isNotEmpty(oneByEmployeeNum)) {
					for (Employee employee : oneByEmployeeNum) {
						EmployeeDTO employeeDTO = new EmployeeDTO();
						Long employeeId = employee.getEmployeeId();
//						System.err.println(employeeId);
						employeeDTO.setEmployeeId(employee.getEmployeeId());
						employeeDTO.setEmployeeName(employee.getEmployeeName());
						employeeDTO.setEmployeeNum(employee.getEmployeeNum());
						lists.add(employeeDTO);
						break;
					}
				} else {
					EmployeeDTO employeeDTO = new EmployeeDTO();
					employeeDTO.setEmployeeNum(empNum);
					employeeDTO.setEmployeeName("该员工信息有误" + (i + 2) + "行");
					lists1.add(employeeDTO);
				}
//				if (CollectionUtils.isEmpty(oneByEmployeeNum)) {
//					EmployeeDTO employeeDTO = new EmployeeDTO();
//					if (employeeDTO1.getEmployeeNum() != null) {
//						employeeDTO.setEmployeeNum(employeeDTO1.getEmployeeNum());
//						employeeDTO.setEmployeeName("该员工信息有误" + (i + 2) + "行");
//						lists1.add(employeeDTO);
//						i++;
//						continue;
//					}
//				}
			}
			
		}
		if (CollectionUtils.isEmpty(lists1)) {
			return R.data(lists);
		} else {
			return R.error(500, "导入错误", lists1);
		}
	}

    /**
     * 根据工单分配名称 模糊查询 员工信息
     * @param employeeDTO
     * @return
     */
    @GetMapping("getAssignmentName")
    public R getAssignmentName(EmployeeDTO employeeDTO){
        SysUser sysUser = this.getSysUser();
        employeeDTO.setProjectManager(sysUser.getUserName());
        List<EmployeeDTO> assignmentName = timesheetAssignmentService.getAssignmentName(employeeDTO);
        return R.data(assignmentName);
    }

    /**
     * 添加工单分配
     * @return
     */
//    @PostMapping("saveAssignment")
    @ResponseBody
    public R saveAssignment(@RequestBody CreateTimesheetAssingnmentDTO createTimesheetAssingnmentDTO){
		SysUser currentUser = this.getSysUser();
		createTimesheetAssingnmentDTO.setTsCreatorUserId(currentUser.getUserId());
		createTimesheetAssingnmentDTO.setTsCreatedBy(currentUser.getUserName());
//        createTimesheetAssingnmentDTO.setTsCreatorUserId((long) 1);
//        createTimesheetAssingnmentDTO.setTsCreatedBy("V00000670");
		if (null != createTimesheetAssingnmentDTO) {
			List<TimesheetAssignment> lists = timesheetAssignmentService.saveAssignment(createTimesheetAssingnmentDTO);
			return R.ok("添加成功");
		}
		System.err.println(System.currentTimeMillis());
		return R.error("添加失败");
	}
    
    /**
     * 添加工单分配
     * @return
     */
    @PostMapping("saveAssignment")
    @ResponseBody
    public R saveAssignmentNew(@RequestBody HashMap<String,Object> params){
        SysUser currentUser = this.getSysUser();
        params.put("currentUserId", currentUser.getUserId());
        params.put("currentUserName", currentUser.getUserName());
        Long tsAssgnId = timesheetAssignmentService.saveAssignmentNew(params);
		if (null != tsAssgnId) {
			Map<String, Object> proc = new HashMap<String, Object>();
			proc.put("msg", new String());
			proc.put("tsAssgnId", tsAssgnId);
			System.err.println("tsAssgnId:" + tsAssgnId);
			empTsAttdAsgnRecordService.freshEmpTsAttdAsgnRecord(proc);
		}
        return R.ok();
    }
    
    /**
     *  修改工单查询
     * @return
     */
    @GetMapping("getByTsAsgnId")
    public TableDataInfo selectByTsAsgnId(TimesheetAssignmentDetailByAssignTypeDTO timesheetAssignmentDetailByAssignTypeDTO){
        startPage();
        List<HashMap<String, Object>> hashMaps = timesheetAssignmentService.selectByTsAsgnId(timesheetAssignmentDetailByAssignTypeDTO);
        return getDataTable(hashMaps);
    }

    /**
     * 修改工时分配单
     * @param createTimesheetAssingnmentDTO
     * @return
     */
    @PutMapping("updateTimesheetAssignment")
    @ResponseBody
	public R updateTimesheetAssignment(@RequestBody CreateTimesheetAssingnmentDTO createTimesheetAssingnmentDTO) {
		if (null != createTimesheetAssingnmentDTO) {
			timesheetAssignmentService
					.updateByPrimaryKey(createTimesheetAssingnmentDTO.getCreateTimesheetAssingnmentDetailDTOList());
			Long tsAssgnId = createTimesheetAssingnmentDTO.getTsAsgnId();
			System.err.println("tsAssgnId:" + tsAssgnId);
			if (null != tsAssgnId) {
				Map<String, Object> proc = new HashMap<String, Object>();
				proc.put("msg", new String());
				proc.put("tsAssgnId", tsAssgnId);
				empTsAttdAsgnRecordService.freshEmpTsAttdAsgnRecordUpdate(proc);
			}
			return R.ok("修改成功");
		}
		return R.error("修改失败");
	}

    /**
     * 修改 部门编号模糊查询
     * @param project
     * @return
     */
    @GetMapping("getByProjectNum")
    public R getByProjectNum(Project project){
        SysUser currentUser = this.getSysUser();
        project.setProjectManager(currentUser.getUserName());
        List<Project> lists = timesheetAssignmentService.getByProjectNum(project);
        return R.data(lists);
    }

    /**
     * 批量删除
     * @return
     */
    @PostMapping("deleteTimesheetAssignmentDetail")
    public R deleteTimesheetAssignmentDetail(@RequestBody TimesheetAssignmentDetailDTO timesheetAssignmentDetailDTO){
        System.err.println(timesheetAssignmentDetailDTO.getId());
        if(timesheetAssignmentDetailDTO.getId() != null) {
            timesheetAssignmentService.deleteTimesheetAssignmentDetail(timesheetAssignmentDetailDTO);
            return R.ok("删除成功");
        }
        return R.error("删除失败");
    }

    /**
     * 根据工单Id 查询
     * @return
     */
    @GetMapping("ByTsAsgnIdGetEmployee")
    public R getByTsAsgnId(GetByTsAsgnIdDTO getByTsAsgnIdDTO){
        List<GetByTsAsgnIdDTO> byTsAsgnId = timesheetAssignmentService.getByTsAsgnId(getByTsAsgnIdDTO.getTsAsgnId());
        return R.data(byTsAsgnId);
    }
}