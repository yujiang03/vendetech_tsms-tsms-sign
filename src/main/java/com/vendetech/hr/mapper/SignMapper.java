package com.vendetech.hr.mapper;

import com.vendetech.hr.vo.*;
import com.vendetech.project.tsms.domain.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface SignMapper {
	List<SignProtocolVO> getSignProtocolVO(SignProtocolVO vo);

	List<SignTemplateVO> getSignTemplate(@Param("proxyPath") String tplPathPrx);

	List<SignStatisticsVO> getSignStatistics(@Param("proxyPath") String tplPathPrx);
	List<SignEmployeeVO> getTotalShouldSignEmployee(@Param("tplId") Integer tplId);
    List<SignEmployeeVO> getTotalAlreadySignEmployee(@Param("tplId") Integer tplId);
	List<SignEmployeeVO> getSignEmployeeByTplIdAndStatus(@Param("tplId") Integer tplId, @Param("status") int status);

	int updateSignTemplateForEdit(SignTemplateVO signTemplateVO);
	int addSignTemplate(SignTemplateVO signTemplateVO);

	@Update("UPDATE sign_template SET status = #{status}, modify_time = NOW(), modify_by_user_id = #{userId} WHERE tpl_code = #{tplCode}")
	int updateSignTemplateStatusByTplCode(@Param("tplCode") String tplCode, @Param("status") int status, @Param("userId") Long userId);

	@Select("SELECT auth_status FROM employeeWHERE id = #{employeeId}")
	String getAuthStatusByEmployeeId(@Param("employeeId") Long employeeId);

	SignEmployeeVO getEmployeeById(@Param("id") Long employeeId);

	@Update("UPDATE employee SET auth_status = #{authStatus},  auth_date = NOW(), modify_time = NOW() WHERE employee_id = #{id}")
	int updateEmployeeAuthStatus(@Param("id") Long employeeId, @Param("authStatus") String y);

	@Update("UPDATE sign_record SET sign_date = #{signDate}, expire_date = #{expireDate}" +
			", status = #{status}, contract_file_path = #{filePath}, modify_time = NOW() " +
			"WHERE contract_no = #{contractNo}")
	int updateSignRecordByContractNoForNotifySign(@Param("contractNo") String contractNo, @Param("signDate") Date signDate,
                                                  @Param("expireDate") Date expireDate, @Param("status") int status, @Param("filePath") String contractFilePath);

	int getExpireYearByContractNo(@Param("contractNo") String contractNo);

	SignTemplateVO getSignTemplateByTplCode(@Param("tplCode") String tplCode, @Param("proxyPath") String tplPathPrx);

	List<SignEmployeeRelVO> getSignEmployeeRel();

	List<SignEmployeeVO> getEmployeeValid();

	List<SignTemplateVO> getSignTemplateByStatus(SignTemplateVO vo);

	int addSignEmployeeRel(SignEmployeeRelVO signEmployeeRelVO);

	SignEmployeeRelVO getSignEmployeeRelByTplIdEmployeeId(@Param("tplId") Integer tplId,
														  @Param("employeeId") Long employeeId,
														  @Param("createDate") String createDate);

    int updateEmployeeById(Employee vo);

    List<SignRecordVO> getSignRecordPlatformNotSign(@Param("contractNo") String contractNo, @Param("status") int status);

    @Update("UPDATE sign_record SET contract_file_path = #{path}, modify_time = NOW() WHERE id = #{id}")
    int updateSignRecordContractFilePathById(@Param("path") String contractFilePath, @Param("id") Long id);

    @Update("UPDATE sign_record SET status = #{status}, modify_time = NOW() WHERE contract_no = #{contractNo}")
    int updateSignRecordStatus(@Param("status") Integer status, @Param("contractNo") String contractNo);

}
