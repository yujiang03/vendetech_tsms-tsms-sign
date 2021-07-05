package com.vendetech.project.tsms.project.service;

import com.vendetech.project.tsms.domain.Company;
import com.vendetech.project.tsms.domain.Department;
import com.vendetech.project.tsms.domain.Project;

import com.vendetech.project.tsms.project.controller.dto.CreateProjectDTO;
import com.vendetech.project.tsms.project.controller.dto.ProjectDTO;
import com.vendetech.project.tsms.project.controller.dto.ProjectExcelDTO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;
import java.util.List;

public interface ProjectService {
    List<HashMap<String, Object>> selectByProjectList(String param);
    List<Company> getCompanyNameList();
    List<Department> getDepartMentNameList();

    List<Project> selectByProjectNum(String projectNum);

    int insert(CreateProjectDTO createProjectDTO);

    Project selectByProjectIdList(Long projectId);

    int updateByPrimaryKey(CreateProjectDTO createProjectDTO);
    List<ProjectExcelDTO> selecprojectManager(ProjectExcelDTO projectExcelDTO);

    List<ProjectExcelDTO> getDepartment(ProjectExcelDTO projectExcelDTO);
    List<ProjectExcelDTO> selectByExcelByRoleId(ProjectExcelDTO projectExcelDTO);

    int saveExcelProject(List<ProjectExcelDTO> projectExcelDTO);

    Department getDepartMentId(String departmentNum);

    Company getCompanyId(String companyName);
    List<Project> selectByNum(String projectNum);

    XSSFWorkbook exportExcelInfoBykeWord(String keyword) throws Exception;
}
