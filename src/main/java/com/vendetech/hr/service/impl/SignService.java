package com.vendetech.hr.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.vendetech.common.utils.DateUtils;
import com.vendetech.common.utils.StringUtils;
import com.vendetech.common.utils.file.FileUploadUtils;
import com.vendetech.hr.mapper.SignMapper;
import com.vendetech.hr.service.ISignService;
import com.vendetech.hr.service.impl.kit.DownloadPdf;
import com.vendetech.hr.service.impl.kit.FddApi;
import com.vendetech.hr.service.impl.kit.SignKit;
import com.vendetech.hr.vo.*;
import com.vendetech.job.service.impl.BaseService;
import com.vendetech.project.tsms.domain.DdHrmDataSign;
import com.vendetech.project.tsms.domain.Employee;
import com.vendetech.project.tsms.service.IDdHrmDataSignService;
import com.vendetech.project.tsms.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SignService extends BaseService implements ISignService {
    @Autowired
    private SignMapper signMapper;
    @Autowired
    private IDdHrmDataSignService ddHrmDataSignService;

    @Value("${base.url}")
    private String baseUrl;
    @Value("${sign.nginxPath}")
    private String nginxPathPrx;
    @Value("${sign.baseFilePath}")
    private String baseFilePath;
    @Value("${sign.tplPath}")
    private String tplPath;
    @Value("${sign.pdfPath}")
    private String pdfPath;
    @Value("${sign.sendMsg}")
    private String sendMsg;

    @Value("${fdd.appId}")
    private String appId;
    @Value("${fdd.appSecret}")
    private String appSecret;
    @Value("${fdd.version}")
    private String version;
    @Value("${fdd.host}")
    private String host;

    @Override
    public List<SignTemplateVO> listSignTemplate(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return signMapper.getSignTemplate(tplPath.replace(baseFilePath, nginxPathPrx));
    }

    @Override
    public List<SignStatisticsVO> listSignStatistics(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return signMapper.getSignStatistics(tplPath.replace(baseFilePath, nginxPathPrx));
    }

    @Override
    public List<SignEmployeeVO> getSignEmployeeListByTplIdAndType(Integer tplId, Integer type) {
        if (type == 1) {
            // 已签署人
            return signMapper.getTotalAlreadySignEmployee(tplId);
        } else if (type == 2) {
            // 未签署人
            return signMapper.getSignEmployeeByTplIdAndStatus(tplId, SignKit.RECORD_STATUS_NOT_SIGN);
        } else {
            // 应签署人
            return signMapper.getTotalShouldSignEmployee(tplId);
        }
    }

    @Override
    public boolean checkExistSignTemplateByTplCode(String tplCode) {
        SignTemplateVO tpl = signMapper.getSignTemplateByTplCode(tplCode, tplPath.replace(baseFilePath, nginxPathPrx));
        return tpl != null;
    }

    @Override
    public boolean publishSignTemplate(String tplCode, Long userId) {
        return signMapper.updateSignTemplateStatusByTplCode(tplCode, SignKit.TPL_STATUS_PUBLISHED, userId) > 0;
    }

    @Override
    public boolean deleteSignTemplate(String tplCode, Long userId) {
        return signMapper.updateSignTemplateStatusByTplCode(tplCode, SignKit.TPL_STATUS_INVALID, userId) > 0;
    }

    @Override
    public List<SignProtocolVO> listSignProtocol(SignProtocolVO vo) {
        PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        vo.setProxyPath(nginxPathPrx);
        return signMapper.getSignProtocolVO(vo);
    }

    @Override
    public String getEmployeeAuth(Long employeeId) {
        return signMapper.getAuthStatusByEmployeeId(employeeId);
    }

    @Override
    public String getAuthUrl(Long employeeId, String returnUrl) throws Exception {
        SignEmployeeVO employee = signMapper.getEmployeeById(employeeId);
        String notifyUrl = baseUrl + "/hr/sign/notify/auth?employeeId=" + employeeId;
        log.info("customerId: " + employee.getCustomerId());
        log.info("name: " + employee.getEmployeeName());
        log.info("cardNum: " + employee.getCardNum());
        log.info("mobile: " + employee.getMobile());
        log.info("returnUrl: " + returnUrl);
        log.info("notifyUrl: " + notifyUrl);
        FddApi api = this.getFddApi(appId, appSecret, version, host);

        JSONObject authRet = api.getAuthPersonUrl(employee.getCustomerId(), employee.getEmployeeName(),
                employee.getCardNum(), employee.getMobile().substring(employee.getMobile().indexOf("-") + 1), returnUrl, notifyUrl);
        log.info("法大大返回：" + authRet.toString());
        if ("1".equals(authRet.get("code").toString())) {
            JSONObject data = (JSONObject) authRet.get("data");
            return api.decode(data.get("url").toString());
        } else {
            throw new Exception(authRet.get("msg").toString());
        }
    }

    @Override
    public boolean notifyAuth(Long employeeId, String serialNo, String customerId, Integer authStatus, String statusDesc, Integer certStatus) {
        Employee e = new Employee();
        e.setEmployeeId(employeeId);
        e.setSerialNo(serialNo);
        e.setAuthStatus(authStatus);
        e.setStatusDesc(statusDesc);
        e.setCertStatus(certStatus);
        e.setAuthDate(new Date());
        return signMapper.updateEmployeeById(e) > 0;
    }

    @Override
    public String getSignUrl(Long employeeId, String returnUrl, String contractNo) throws Exception {
        if(StringUtils.isEmpty(contractNo)) {
            throw new Exception("查询不到该协议");
        }

        List<SignRecordVO> srList = signMapper.getSignRecordPlatformNotSign(contractNo, SignKit.RECORD_STATUS_NOT_SIGN);
        if(srList.isEmpty()) {
            throw new Exception("查询不到该协议");
        }

        SignEmployeeVO employee = signMapper.getEmployeeById(employeeId);
        // SignTemplateVO template = signMapper.getSignTemplateByTplCode(srList.get(0).getTplCode(), tplPath);
        // DdHrmDataSign ddHrmData = ddHrmDataSignService.getHrmDataByJobNumber(employee.getEmployeeNum());
        // if(ddHrmData == null) {
        //     throw new Exception("该员工花名册信息缺失");
        // }
        //
        FddApi api = this.getFddApi(appId, appSecret, version, host);
        // JSONObject fillRet = api.fillContract(srList.get(0), ddHrmData, template.getTplPath());
        // if (fillRet == null) {
        //     throw new Exception("合同模板填充失败");
        // }

        String notifyUrl = baseUrl + "/hr/sign/notify/sign";
        log.info("customerId: " + employee.getCustomerId());
        log.info("title: " + employee.getEmployeeName() + " 的合同");
        log.info("contractNo: " + contractNo);
        log.info("returnUrl: " + returnUrl);
        log.info("notifyUrl: " + notifyUrl);
        String url = api.employeeSign(employee.getCustomerId(), employee.getEmployeeName() + " 的合同", contractNo, returnUrl,
                notifyUrl, "sign2");
        log.info("法大大返回：" + url);
        return url;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public JSONObject autoSign(String customerId, String contractNo) throws Exception {
        if(StringUtils.isEmpty(contractNo)) {
            throw new Exception("查询不到该协议");
        }
        List<SignRecordVO> srList = signMapper.getSignRecordPlatformNotSign(contractNo, SignKit.RECORD_STATUS_VERIFY_PASS);
        if(srList.isEmpty()) {
            throw new Exception("查询不到该协议");
        }
        SignRecordVO sr = srList.get(0);
        String title = sr.getContractNo() + " 的用工合同";

        // 自动签章
        FddApi api = this.getFddApi(appId, appSecret, version, host);
        JSONObject ret = api.autoSign(customerId, title, contractNo, "sign1");
        log.info("法大大返回：" + ret.toString());
        if (!"1000".equals(ret.get("code"))) {
            throw new Exception("自动签章失败： " + ret.get("msg").toString() + ", 请重新用章！");
        }
        String fddPdfUrl = api.getDownLoadUrl(contractNo);
        String fileName = UUID.randomUUID() + ".pdf";
        DownloadPdf.download(fddPdfUrl, pdfPath + "/" + fileName);
        signMapper.updateSignRecordContractFilePathById("/" + fileName, sr.getId());

        // 合同归档
        JSONObject fillingRet = JSONObject.parseObject(api.contractFiling(contractNo));
        if (!"1000".equals(fillingRet.get("code"))) {
            throw new Exception("合同归档失败，失败信息：" + ret.get("msg") + ", 请重新用章！");
        }

        // 记录更新
        Date signDate = new Date();
        int tplExpireYear = signMapper.getExpireYearByContractNo(contractNo);
        Date expireDate = DateUtils.addYears(signDate, tplExpireYear);
        signMapper.updateSignRecordByContractNoForNotifySign(contractNo, signDate,
                expireDate, SignKit.RECORD_STATUS_SIGNED, "/" + fileName);
        return ret;
    }

    @Override
    public int updateSignRecordStatus(Integer status, String contractNo) {
        return signMapper.updateSignRecordStatus(status, contractNo);
    }

    @Override
    public boolean notifySign(String contractNo, String downloadUrl) {
        String fileName = UUID.randomUUID() + ".pdf";
        DownloadPdf.download(downloadUrl, pdfPath + "/" + fileName);

        Date signDate = new Date();
        int tplExpireYear = signMapper.getExpireYearByContractNo(contractNo);
        Date expireDate = DateUtils.addYears(signDate, tplExpireYear);
        return signMapper.updateSignRecordByContractNoForNotifySign(contractNo, signDate, expireDate,
                SignKit.RECORD_STATUS_EMPL_SIGNED, "/" + fileName) > 0;
    }

    @Override
    public String uploadTpl(MultipartFile file) throws IOException {
        String fileNamePath = FileUploadUtils.upload(tplPath, file);
        log.info("文件上传成功，tplPath: " + tplPath);
        return fileNamePath;
    }

    @Override
    public String checkTemplateForAdd(SignTemplateVO signTemplateVO, MultipartFile file) {
        if (StringUtils.isBlank(signTemplateVO.getTplCode())) {
            return "协议编号（tplCode）不能为空";
        }
        if (StringUtils.isBlank(signTemplateVO.getTplName())) {
            return "协议名称（tplName）不能为空";
        }
        if (file == null) {
            return "必须上传协议模板pdf";
        }
        if (signTemplateVO.getTplExpireDay() == null) {
            return "电签有效天数（tplExpireDay）不能为空";
        }
        if (signTemplateVO.getTplExpireDay() <= 0) {
            return "电签有效天数（tplExpireDay）必须大于0";
        }
        if (signTemplateVO.getTplExpireYear() == null) {
            return "电签协议年份（tplExpireYear）不能为空";
        }
        if (signTemplateVO.getTplExpireYear() <= 0) {
            return "电签协议年份（tplExpireYear）必须大于0";
        }
        SignTemplateVO st = signMapper.getSignTemplateByTplCode(signTemplateVO.getTplCode(), tplPath.replace(baseFilePath, nginxPathPrx));
        if (st != null) {
            return "已存在相同协议编号（tplCode）的电签模板";
        }
        return null;
    }

    @Override
    public String checkTemplateForUpdate(SignTemplateVO tpl) {
        if (StringUtils.isBlank(tpl.getTplCode())) {
            return "协议编号（tplCode）不能为空";
        }
        if (tpl.getTplExpireDay() != null && tpl.getTplExpireDay() <= 0) {
            return "电签有效天数（tplExpireday）必须大于0";
        }
        if (tpl.getTplExpireYear() != null && tpl.getTplExpireYear() <= 0) {
            return "电签协议年份（tplExpireyear）必须大于0";
        }
        SignTemplateVO st = signMapper.getSignTemplateByTplCode(tpl.getTplCode(), tplPath.replace(baseFilePath, nginxPathPrx));
        if (st == null) {
            return "电签模板不存在";
        }
        return null;
    }

    @Override
    public boolean addSignTemplate(SignTemplateVO signTemplateVO, String tplPath, Long sysUserId) {
        signTemplateVO.setTplPath(tplPath);
        signTemplateVO.setIsUpload(SignKit.TPL_IS_UPLOAD_NO);
        signTemplateVO.setStatus(SignKit.TPL_STATUS_DRAFT);
        signTemplateVO.setCreateTime(DateUtil.getFormatCurrentTime());
        signTemplateVO.setCreateByUserId(sysUserId);
        signTemplateVO.setModifyTime(DateUtil.getFormatCurrentTime());
        signTemplateVO.setModifyByUserId(sysUserId);
        return signMapper.addSignTemplate(signTemplateVO) > 0;
    }

    @Override
    public boolean updateSignTemplate(SignTemplateVO signTemplateVO, String tplPath, Long sysUserId) {
        signTemplateVO.setTplPath(tplPath);
        signTemplateVO.setModifyTime(DateUtil.getFormatCurrentTime());
        signTemplateVO.setModifyByUserId(sysUserId);
        return signMapper.updateSignTemplateForEdit(signTemplateVO) > 0;
    }

    @Override
    public List<SignEmployeeRelVO> listSignEmployeeRel(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return signMapper.getSignEmployeeRel();
    }

    @Override
    public List<SignEmployeeVO> getRelEmployeeList() {
        return signMapper.getEmployeeValid();
    }

    @Override
    public List<SignTemplateVO> getRelTemplateList(SignTemplateVO vo) {
        if(vo.getPageNum() != null) {
            PageHelper.startPage(vo.getPageNum(), vo.getPageSize());
        }
        vo.setProxyPath(tplPath.replace(baseFilePath, nginxPathPrx));
        List<SignTemplateVO> list = signMapper.getSignTemplateByStatus(vo);
        return list;
    }

    @Override
    public boolean checkExistSignEmployeeRel(Integer templateId, Long employeeId) {
        String createDate = new SimpleDateFormat("yyyyMMdd").format(new Date());
        return signMapper.getSignEmployeeRelByTplIdEmployeeId(templateId, employeeId, createDate) != null;
    }

    @Override
    public boolean addSignEmployeeRel(SignEmployeeRelVO signEmployeeRelVO, Long sysUserId) {
        signEmployeeRelVO.setCreateByUserId(sysUserId);
        signEmployeeRelVO.setCreateTime(DateUtil.getFormatCurrentTime());
        signEmployeeRelVO.setModifyByUserId(sysUserId);
        signEmployeeRelVO.setModifyTime(DateUtil.getFormatCurrentTime());
        return signMapper.addSignEmployeeRel(signEmployeeRelVO) > 0;
    }

    @Override
    public SignEmployeeVO getEmployeeById(Long loginEmployeeId) {
        return signMapper.getEmployeeById(loginEmployeeId);
    }

    @Override
    public void updateEmployeeById(Employee e) {
        if (!StringUtils.isBlank(e.getBankCard())) {
            e.setBankCard(e.getBankCard().replace(" ", ""));
        }
        signMapper.updateEmployeeById(e);
    }

    @Override
    public void cancelRecord(String contractNo) throws Exception {
        FddApi api = this.getFddApi(appId, appSecret, version, host);
        String ret = api.cancellationOfContract(contractNo);
        JSONObject retJson = JSONObject.parseObject(ret);

        // 合同未上传的业务类型错误的情况下也可以撤销合同
        if (!"1000".equals(retJson.getString("code")) && !retJson.getString("msg").contains("合同未上传")) {
            throw new Exception("撤销合同操作失败： " + retJson.getString("msg") + ", 请稍后重试！");
        }

        signMapper.updateSignRecordStatus(SignKit.RECORD_STATUS_CANCELED, contractNo);
    }

}
