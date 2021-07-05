package com.vendetech.job.mapper;

import com.vendetech.hr.vo.SignEmployeeVO;
import com.vendetech.hr.vo.SignRecordVO;
import com.vendetech.hr.vo.SignTemplateVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignScheduleMapper {

    SignEmployeeVO getEmployeeById(@Param("employeeId") Long employeeId);

	List<SignEmployeeVO> getEmployeeWithoutCustomerId();

	@Update("UPDATE employee SET customer_id = #{customerId}, modify_time = NOW() WHERE employee_id = #{employeeId}")
	int updateEmployeeCustomerId(@Param("customerId") String customerId, @Param("employeeId") Long employeeId);

	List<SignTemplateVO> getSignTemplateNotUploaded();

	@Update("UPDATE sign_template SET is_upload = #{isUpload} WHERE id = #{employeeId}")
	int updateSignTemplateIsUpload(@Param("isUpload") String isUpload, @Param("employeeId") Integer employeeId);

	List<SignRecordVO> getSignRecordNotSynchronized();

	int batchAddSignRecord(@Param("list") List<SignRecordVO> recordList);

	List<SignRecordVO> getSignRecordPlatformNotSign();

	@Update("UPDATE sign_record SET contract_file_path = #{path}, modify_time = NOW() WHERE id = #{id}")
	int updateSignRecordContractFilePathById(@Param("path") String contractFilePath, @Param("id") Long id);

	List<SignRecordVO> listSignRecord();
}

