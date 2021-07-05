package com.vendetech.project.tsms.timesheetAssignment.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vendetech.project.tsms.timesheet.mapper.EmpTsAttdAsgnRecordMapper;
import com.vendetech.project.tsms.timesheetAssignment.service.EmpTsAttdAsgnRecordService;

@Service
public class EmpTsAttdAsgnRecordServiceImpl implements EmpTsAttdAsgnRecordService {

	protected final Logger logger = LoggerFactory.getLogger(EmpTsAttdAsgnRecordServiceImpl.class);

	@Autowired
	EmpTsAttdAsgnRecordMapper empTsAttdAsgnRecordMapper;

	@Async
	@Override
	@Transactional
	public void freshEmpTsAttdAsgnRecord(Map<String, Object> params) {
        empTsAttdAsgnRecordMapper.freshEmpTsAttdAsgnRecord(params);
        logger.info("freshEmpTsAttdAsgnRecord done......");
	}

	@Async
	@Override
	@Transactional
	public void freshEmpTsAttdAsgnRecordUpdate(Map<String, Object> params) {
		empTsAttdAsgnRecordMapper.freshEmpTsAttdAsgnRecordUpdate(params);
        logger.info("freshEmpTsAttdAsgnRecordUpdate done......");
	}

	/**
	 * @param proc
	 */
	@Async
	@Override
	@Transactional
	public void freshEmpTsAttdAsgnRecordByEmp(Map<String, Object> proc) {
		logger.info("freshEmpTsAttdAsgnRecordByEmp----------------" + proc.toString());
		empTsAttdAsgnRecordMapper.freshEmpTsAttdAsgnRecordByEmp(proc);
	}
}
