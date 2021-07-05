package com.vendetech.project.tsms.project.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.vendetech.project.tsms.domain.Project;
import com.vendetech.project.tsms.project.controller.dto.ProjectExcelDTO;

public interface ProjectMapper {

	List<HashMap<String, Object>> selectByProjectList(@Param("keyword") String param);

	int deleteByPrimaryKey(Long projectId);

	int insert(Project record);

	int insertSelective(Project record);

	Project selectByPrimaryKey(Long projectId);

	int updateByPrimaryKeySelective(Project record);

	int updateByPrimaryKey(Project record);

	List<Project> selectByProjectNum(String projectNum);

	Project selectByProjectIdList(Long projectId);

	List<Project> getByProjectNum(Project project);

	List<Project> selectByNum(String projectNum);

	Project getProjectDepartment(Long projectId);

	List<Project> findProjectByNumName(@Param("projectNum") String projectNum,
			@Param("projectName") String projectName);

	List<ProjectExcelDTO> selectByExcelNickeName(ProjectExcelDTO projectExcelDTO);

	List<ProjectExcelDTO> selectByExcelByRoleId(ProjectExcelDTO projectExcelDTO);

	List<ProjectExcelDTO> getDepartment(ProjectExcelDTO projectExcelDTO);

	List<Map<String, Object>> exportExcelInfoBykeWord(@Param("keyword") String keyword);

	void snapshotReportData();
}
