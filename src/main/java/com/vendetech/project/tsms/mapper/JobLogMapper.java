package com.vendetech.project.tsms.mapper;

import com.vendetech.project.tsms.domain.JobLog;

public interface JobLogMapper {
	
    int deleteByPrimaryKey(Long jobLogId);

    int insert(JobLog record);

    int insertSelective(JobLog record);

    JobLog selectByPrimaryKey(Long jobLogId);

    int updateByPrimaryKeySelective(JobLog record);
    
    int finishByPrimaryKeySelective(JobLog record);

    int updateByPrimaryKey(JobLog record);
    
    JobLog getLatestJobLog(String jobName);
    
}