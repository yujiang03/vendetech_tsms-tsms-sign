package com.vendetech.hr.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.vendetech.common.utils.StringUtils;
import com.vendetech.common.utils.file.FileUploadUtils;
import com.vendetech.hr.mapper.SignCompanySignatureMapper;
import com.vendetech.hr.service.ICompanyService;
import com.vendetech.hr.service.impl.kit.FddApi;
import com.vendetech.hr.vo.SignCompanySignature;
import com.vendetech.job.service.impl.BaseService;
import com.vendetech.project.tsms.domain.Company;
import com.vendetech.project.tsms.project.mapper.CompanyMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CompanyService extends BaseService implements ICompanyService {
    @Value("${sign.nginxPath}")
    private String nginxPathPrx;
    @Value("${sign.baseFilePath}")
    private String baseFilePath;
    @Value("${sign.companyImgPath}")
    private String companyImgPath;
    @Value("${base.url}")
    private String baseUrl;

    @Value("${fdd.appId}")
    private String appId;
    @Value("${fdd.appSecret}")
    private String appSecret;
    @Value("${fdd.version}")
    private String version;
    @Value("${fdd.host}")
    private String host;

    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private SignCompanySignatureMapper signCompanySignatureMapper;


    @Override
    public List<Company> getCompanyList(Company queryCompany) {
        if(queryCompany.getPageNum() != null) {
            PageHelper.startPage(queryCompany.getPageNum(), queryCompany.getPageSize());
        }
        queryCompany.setProxyPath(companyImgPath.replace(baseFilePath, nginxPathPrx));
        return companyMapper.selectSelective(queryCompany);
    }

    @Override
    public int addCompany(Company company, MultipartFile creditImg, MultipartFile legalImg) throws IOException {
        String creditImgPath = FileUploadUtils.upload(companyImgPath, creditImg);
        String legalImgPath = FileUploadUtils.upload(companyImgPath, legalImg);
        company.setCreateTime(new Date());
        company.setCreditImagePath(creditImgPath);
        company.setLegalIdFrontPath(legalImgPath);
        return companyMapper.insertSelective(company);
    }

    @Override
    public int updateCompanyInfo(Company company, MultipartFile creditImg, MultipartFile legalImg) throws IOException {
        if (creditImg != null) {
            String creditImgPath = FileUploadUtils.upload(companyImgPath, creditImg);
            company.setCreditImagePath(creditImgPath);
        }
        if (legalImg != null) {
            String legalImgPath = FileUploadUtils.upload(companyImgPath, legalImg);
            company.setLegalIdFrontPath(legalImgPath);
        }
        company.setModifyTime(new Date());
        return companyMapper.updateByPrimaryKeySelective(company);
    }

    @Override
    public int removeCompany(Company company) {
        Company updateCompany = new Company();
        updateCompany.setCompanyId(company.getCompanyId());
        updateCompany.setStatus(0);
        return companyMapper.updateByPrimaryKeySelective(updateCompany);
    }

    @Override
    public List<SignCompanySignature> getCompanySignatureList(SignCompanySignature signature) {
        if(signature.getPageNum() != null) {
            PageHelper.startPage(signature.getPageNum(), signature.getPageSize());
        }
        return signCompanySignatureMapper.selectSelective(signature);
    }

    @Override
    public int addSignature(SignCompanySignature signature) throws Exception {
        FddApi api = this.getFddApi(appId, appSecret, version, host);
        JSONObject ret = api.addSignature(signature.getCustomerId(), signature.getFddSignatureImgBase64());
        log.info(ret.toJSONString());
        if ("1".equals(ret.get("code").toString())) {
            signature.setFddSignatureId(ret.getJSONObject("data").getString("signature_id"));
            signature.setFddSignatureScope(1);  // 1默认
            signature.setFddSignatureType(2);   // 2企业
            signature.setCreateTime(new Date());

            SignCompanySignature updateParams = new SignCompanySignature();
            updateParams.setCompanyId(signature.getCompanyId());
            updateParams.setFddSignatureScope(0);
            signCompanySignatureMapper.updateSelective(updateParams);

            return signCompanySignatureMapper.insert(signature);
        } else {
            throw new Exception(ret.toJSONString());
        }
    }

    @Override
    public int updateSignatureInfo(SignCompanySignature signature) throws Exception {

        SignCompanySignature updateRecord = new SignCompanySignature();
        updateRecord.setSignatureId(signature.getSignatureId());
        updateRecord.setSignatureName(signature.getSignatureName());
        updateRecord.setModifyTime(new Date());
        updateRecord.setModifyByUserId(signature.getModifyByUserId());

        if(signature.getFddSignatureImgBase64() == null) {
            return signCompanySignatureMapper.updateByPrimaryKeySelective(updateRecord);
        }

        FddApi api = this.getFddApi(appId, appSecret, version, host);
        JSONObject ret = api.replaceSignature(signature.getCustomerId(),
                signature.getFddSignatureId(), signature.getFddSignatureImgBase64());
        if ("1".equals(ret.get("code").toString())) {
            updateRecord.setFddSignatureImgBase64(signature.getFddSignatureImgBase64());
            return signCompanySignatureMapper.updateByPrimaryKeySelective(signature);
        } else {
            throw new Exception(ret.toJSONString());
        }
    }

    @Override
    public int setDefaultSignature(SignCompanySignature signature) {
        FddApi api = this.getFddApi(appId, appSecret, version, host);
        JSONObject ret = api.defaultSignature(signature.getCustomerId(), signature.getFddSignatureId());
        if ("1".equals(ret.get("code").toString())) {
            SignCompanySignature updateParams = new SignCompanySignature();
            updateParams.setCompanyId(signature.getCompanyId());
            updateParams.setFddSignatureScope(0);
            updateParams.setModifyByUserId(signature.getModifyByUserId());
            updateParams.setModifyTime(new Date());
            signCompanySignatureMapper.updateSelective(updateParams);

            updateParams = new SignCompanySignature();
            updateParams.setSignatureId(signature.getSignatureId());
            updateParams.setFddSignatureScope(1);
            updateParams.setModifyByUserId(signature.getModifyByUserId());
            updateParams.setModifyTime(new Date());
            signCompanySignatureMapper.updateByPrimaryKeySelective(updateParams);
        }

        return 0;
    }

    @Override
    public int removeSignature(SignCompanySignature signature) throws Exception {
        FddApi api = this.getFddApi(appId, appSecret, version, host);
        JSONObject ret = api.removeSignature(signature.getCustomerId(), signature.getFddSignatureId());
        if ("1".equals(ret.get("code").toString())) {
            return signCompanySignatureMapper.deleteByPrimaryKey(signature.getSignatureId());
        } else {
            throw new Exception(ret.toJSONString());
        }
    }

    @Override
    public String getAuthUrl(Company paramsCompany) throws Exception {
        String customerId = paramsCompany.getCustomerId();
        FddApi api = this.getFddApi(appId, appSecret, version, host);

        if (StringUtils.isBlank(customerId)) {
            // 企业唯一标识： companyId_creditNo
            JSONObject ret = api.regAccount(paramsCompany.getCompanyId(), paramsCompany.getCreditNo(), 2);
            if (ret.getInteger("code") != 1) {
                throw new Exception("调用法大大接口错误：" + ret.toJSONString());
            }
            customerId = ret.getString("data");
            Company updateCompany = new Company();
            updateCompany.setCompanyId(paramsCompany.getCompanyId());
            updateCompany.setCustomerId(customerId);
            companyMapper.updateByPrimaryKeySelective(updateCompany);
        }

        // 获取最新记录
        Company company = companyMapper.selectByPrimaryKey(paramsCompany.getCompanyId());
        if(StringUtils.isBlank(company.getVerifyUrl())) {
            company.setProxyPath(companyImgPath.replace(baseFilePath, nginxPathPrx));
            String notifyUrl = baseUrl + "/hr/company/notify/auth?companyId=" + company.getCompanyId();

            JSONObject ret = api.getCompanyAuthUrl(company, paramsCompany.getReturnUrl(), notifyUrl);
            if (ret.getInteger("code") != 1) {
                throw new Exception("调用法大大接口错误：" + ret.toJSONString());
            }

            String url = api.decode(ret.getJSONObject("data").getString("url"));
            String transactionNo = ret.getJSONObject("data").getString("transactionNo");
            Company record = new Company();
            record.setCompanyId(company.getCompanyId());
            record.setVerifyUrl(url);
            record.setTransactionNo(transactionNo);
            record.setModifyTime(new Date());
            companyMapper.updateByPrimaryKeySelective(record);
            return url;
        } else {
            return company.getVerifyUrl();
        }

    }

    @Override
    public int notifyAuth(String companyId, String customerId, String status,
                          String statusDesc, String certStatus, String serialNo) {
        Company company = new Company();
        company.setCompanyId(Long.valueOf(companyId));
        company.setAuthStatus(Integer.valueOf(status));
        company.setStatusDesc(statusDesc);
        company.setCertStatus(Integer.valueOf(certStatus));
        company.setSerialNo(serialNo);
        company.setAuthDate(new Date());
        company.setModifyTime(new Date());
        return companyMapper.updateByPrimaryKeySelective(company);
    }

    @Override
    public String getAuthSignUrl(Company company) throws Exception {
        FddApi api = this.getFddApi(appId, appSecret, version, host);

        String notifyUrl = baseUrl + "/hr/company/notify/authSign?companyId=" + company.getCompanyId();

        String transactionId = company.getCompanyId() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String contractId = company.getCompanyId()  + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String url = api.beforeAuthSign(transactionId, contractId, company.getCustomerId(), company.getReturnUrl(), notifyUrl);

        Company record = new Company();
        record.setCompanyId(company.getCompanyId());
        record.setAuthSignTransactionId(transactionId);
        record.setAuthSignContractNo(contractId);
        record.setModifyTime(new Date());
        companyMapper.updateByPrimaryKeySelective(record);

        return url;
    }

    @Override
    public boolean notifyAuthSign(String companyId, String transaction_id, String contract_id, String result_desc, String viewPdfUrl) {
        Company company = new Company();
        company.setCompanyId(Long.valueOf(companyId));
        company.setAuthSignResultDesc(result_desc);
        company.setAuthSignViewPdf(viewPdfUrl);
        company.setAuthSignStatus(1);
        company.setModifyTime(new Date());
        return companyMapper.updateByPrimaryKeySelective(company) > 0;
    }
}
