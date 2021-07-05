package com.vendetech.hr.service;

import com.vendetech.hr.vo.SignCompanySignature;
import com.vendetech.project.tsms.domain.Company;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ICompanyService {
    List<Company> getCompanyList(Company queryCompany);

    int addCompany(Company company, MultipartFile creditImg, MultipartFile legalImg) throws IOException;

    int updateCompanyInfo(Company company, MultipartFile creditImg, MultipartFile legalImg) throws IOException;

    int removeCompany(Company company);

    List<SignCompanySignature> getCompanySignatureList(SignCompanySignature querySignature);

    int addSignature(SignCompanySignature signature) throws Exception;

    int updateSignatureInfo(SignCompanySignature signature) throws Exception;

    int removeSignature(SignCompanySignature signature) throws Exception;

    String getAuthUrl(Company company) throws Exception;

    int notifyAuth(String companyId, String customerId, String status, String statusDesc, String certStatus, String serialNo);

    int setDefaultSignature(SignCompanySignature signature);

    String getAuthSignUrl(Company company) throws Exception;

    boolean notifyAuthSign(String companyId, String transaction_id, String contract_id, String result_desc, String viewPdfUrl);
}
