package com.vendetech.job.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.vendetech.common.utils.DateUtils;
import com.vendetech.common.utils.StringUtils;
import com.vendetech.hr.mapper.SignMapper;
import com.vendetech.hr.service.impl.kit.DownloadPdf;
import com.vendetech.hr.service.impl.kit.FddApi;
import com.vendetech.hr.service.impl.kit.SignKit;
import com.vendetech.job.mapper.SignScheduleMapper;
import com.vendetech.job.service.ISignScheduleTaskService;
import com.vendetech.job.service.impl.kit.SignScheduleTaskKit;
import com.vendetech.hr.vo.SignEmployeeVO;
import com.vendetech.hr.vo.SignRecordVO;
import com.vendetech.hr.vo.SignTemplateVO;
import com.vendetech.project.tsms.domain.DdHrmDataSign;
import com.vendetech.project.tsms.service.IDdHrmDataSignService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SignScheduleTaskService extends BaseService implements ISignScheduleTaskService {
	@Autowired
	private SignScheduleMapper signScheduleMapper;
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
	public void registerFddAccount() {
		try {
			List<SignEmployeeVO> employee = signScheduleMapper.getEmployeeWithoutCustomerId();
			for (SignEmployeeVO e : employee) {
				Long employeeId = e.getEmployeeId();
                FddApi api = this.getFddApi(appId, appSecret, version, host);
				JSONObject ret = api.regAccount(employeeId, e.getCardNum(), 1);
				if (ret.getInteger("code") != 1) {
					throw new Exception("调用法大大接口错误：" + ret.toJSONString());
				}
				String customerId = ret.getString("data");
				signScheduleMapper.updateEmployeeCustomerId(customerId, e.getEmployeeId());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void uploadContractTpl() {
		try {
			List<SignTemplateVO> tplList = signScheduleMapper.getSignTemplateNotUploaded();
			for (SignTemplateVO tpl : tplList) {
				log.info("tplCode: " + tpl.getTplCode() + ", tplName: " + tpl.getTplName());
				FddApi api = this.getFddApi(appId, appSecret, version, host);
				JSONObject ret = api.uploadContractTpl(tpl.getId() + "_" + tpl.getTplCode(),
						tplPath + tpl.getTplPath());
				log.info("法大大返回：" + ret.toString());
				if ("1".equals(ret.get("code"))) {
					signScheduleMapper.updateSignTemplateIsUpload(SignScheduleTaskKit.TPL_IS_UPLOAD_YES, tpl.getId());
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public void createSignRecords() {
		try {
			List<SignRecordVO> recordList = signScheduleMapper.getSignRecordNotSynchronized();
			if (CollectionUtils.isEmpty(recordList)) {
				return;
			}
			for (SignRecordVO r : recordList) {
				// contractNo: smr.template_id, '_', st.tpl_code, '_', smr.member_id
				Date today = new Date();
				r.setNotifyDate(DateUtils.format(today));
				r.setNotifyExpireDate(DateUtils.format(DateUtils.addDays(today, r.getTplExpireDay())));
				r.setStatus(SignScheduleTaskKit.RECORD_STATUS_NOT_SIGN);
				r.setCreateTime(DateUtils.format(today));
				r.setModifyTime(DateUtils.format(today));

				// 填充模板
				String download_url = this.fillContract(r);
                String fileName = UUID.randomUUID() + ".pdf";
                DownloadPdf.download(download_url, pdfPath + "/" + fileName);
                r.setContractFilePath("/" + fileName);

                // 发送短信
                FddApi api = this.getFddApi(appId, appSecret, version, host);
				String ret = api.sendSmsContent(r.getMobile().substring(r.getMobile().indexOf("-") + 1),
                        2, sendMsg, "99999");
				log.info(ret);
			}
			signScheduleMapper.batchAddSignRecord(recordList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void downloadPDFNotSync() {
	    List<SignRecordVO> records = signScheduleMapper.listSignRecord();
        FddApi api = this.getFddApi(appId, appSecret, version, host);
        for (SignRecordVO record : records) {
            String filePath = pdfPath + record.getContractFilePath();
            File file = new File(filePath);
            if (!file.exists()) {
                String downloadUrl = api.getDownLoadUrl(record.getContractNo());
                DownloadPdf.download(downloadUrl, filePath);
            }
        }
	}

	private String fillContract(SignRecordVO signRecord) throws Exception {
        // if(StringUtils.isEmpty(contractNo)) {
        //     throw new Exception("查询不到该协议");
        // }

        // List<SignRecordVO> srList = signMapper.getSignRecordPlatformNotSign(contractNo, SignKit.RECORD_STATUS_NOT_SIGN);
        // if(srList.isEmpty()) {
        //     throw new Exception("查询不到该协议");
        // }

        SignTemplateVO template = signMapper.getSignTemplateByTplCode(signRecord.getTplCode(), tplPath);
        SignEmployeeVO employee = signMapper.getEmployeeById(signRecord.getEmployeeId());
        DdHrmDataSign ddHrmData = ddHrmDataSignService.getHrmDataByJobNumber(employee.getEmployeeNum());
        if(ddHrmData == null) {
            throw new Exception("该员工花名册信息缺失");
        }

        FddApi api = this.getFddApi(appId, appSecret, version, host);
        JSONObject fillRet = api.fillContract(signRecord, ddHrmData, template.getTplPath());
        if (fillRet == null) {
            throw new Exception("合同模板填充失败");
        }

        return fillRet.getString("download_url");
    }
}
