package com.vendetech.hr.service;

import com.alibaba.fastjson.JSONObject;
import com.vendetech.hr.vo.*;
import com.vendetech.project.tsms.domain.Employee;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ISignService {
	List<SignTemplateVO> listSignTemplate(int pageNum, int pageSize);
	List<SignStatisticsVO> listSignStatistics(int pageNum, int pageSize);
	List<SignEmployeeVO> getSignEmployeeListByTplIdAndType(Integer tplId, Integer type);
	boolean checkExistSignTemplateByTplCode(String tplCode);
	boolean publishSignTemplate(String tplCode, Long sysUserId);
	boolean deleteSignTemplate(String tplCode, Long sysUserId);
	List<SignProtocolVO> listSignProtocol(SignProtocolVO signProtocolVO);
	String getEmployeeAuth(Long employeeId);
	String getAuthUrl(Long employeeId, String returnUrl) throws Exception;
	boolean notifyAuth(Long employeeId, String serialNo, String customerId, Integer authStatus, String statusDesc, Integer certStatus);
	String getSignUrl(Long employeeId, String returnUrl, String contractNo) throws Exception;
	boolean notifySign(String contractNo, String downloadUrl);
	String uploadTpl(MultipartFile file) throws IOException;
	String checkTemplateForAdd(SignTemplateVO signTemplateVO, MultipartFile file);
	String checkTemplateForUpdate(SignTemplateVO signTemplateVO);
	boolean addSignTemplate(SignTemplateVO signTemplateVO, String tplPath, Long sysUserId);
	boolean updateSignTemplate(SignTemplateVO signTemplateVO, String tplPath, Long sysUserId);
	List<SignEmployeeRelVO> listSignEmployeeRel(int pageNum, int pageSize);
	List<SignEmployeeVO> getRelEmployeeList();
	List<SignTemplateVO> getRelTemplateList(SignTemplateVO vo);
	/** true:exist	false:not exist*/
	boolean checkExistSignEmployeeRel(Integer templateId, Long employeeId);
	boolean addSignEmployeeRel(SignEmployeeRelVO signEmployeeRelVO, Long sysUserId);

	SignEmployeeVO getEmployeeById(Long loginEmployeeId);
	void updateEmployeeById(Employee vo);
	JSONObject autoSign(String customerId, String contractNo) throws Exception;

    int updateSignRecordStatus(Integer status, String contractNo);

    void cancelRecord(String contractNo) throws Exception;
    // List<SignProtocolVO> signProtocolById(Long id);
}
