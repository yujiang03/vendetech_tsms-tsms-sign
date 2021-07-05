package com.vendetech.project.tsms.timesheet.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vendetech.framework.web.controller.BaseController;
import com.vendetech.project.tsms.domain.R;
import com.vendetech.project.tsms.project.controller.ProjectController;
import com.vendetech.project.tsms.timesheet.service.ReportService;

@RestController
@RequestMapping("basicData")
public class BasicDataController extends BaseController {

	protected final Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	private ReportService reportService;

	/**
	 * -项目下拉
	 * 
	 * @return
	 */
	@GetMapping(value = "/getFreshStatus")
	public R getProjectList() {
//		List<HashMap<String, Object>> projectList = projectService.selectByProjectList(null);
		List<String> status = reportService.getFreshStatus();
//		String freshStatus = "";
		return R.data(status);
	}

	
}
