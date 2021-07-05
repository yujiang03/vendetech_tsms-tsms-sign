package com.vendetech.hr.controller;

import com.vendetech.hr.service.ICompanyService;
import com.vendetech.hr.vo.SignCompanySignature;
import com.vendetech.project.tsms.domain.Company;
import com.vendetech.project.tsms.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("hr/company")
public class CompanyManagementController extends BaseController {
    @Autowired
    private ICompanyService companyService;
    @Value("${base.url}")
    private String baseUrl;

    @RequestMapping("/list")
    public R getCompanyList(@RequestBody Company queryCompany) {
        List<Company> companies = companyService.getCompanyList(queryCompany);
        return result(companies);
    }

    @RequestMapping("/add")
    public R addCompany(Company companyParams, MultipartFile creditImage, MultipartFile legalFrontImage) {
        Long sysUserId = this.getLoginUserId();
        companyParams.setCreateUser(sysUserId);

        try {
            int i = companyService.addCompany(companyParams, creditImage, legalFrontImage);
            return R.ok("新增成功");
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("图片上传失败");
        }
    }

    @RequestMapping("/edit")
    public R updateCompanyInfo(Company companyParams, MultipartFile creditImage, MultipartFile legalFrontImage) {
        Long sysUserId = this.getLoginUserId();
        companyParams.setModifyUser(sysUserId);
        try {
            int i = companyService.updateCompanyInfo(companyParams, creditImage, legalFrontImage);
            return R.ok("更新成功");
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("图片上传失败");
        }
    }

    @RequestMapping("/remove")
    public R removeCompany(@RequestBody Company companyParams) {
        Long sysUserId = this.getLoginUserId();
        companyParams.setModifyUser(sysUserId);
        int i = companyService.removeCompany(companyParams);
        return R.ok("删除成功");
    }


    @RequestMapping("/signature/list")
    public R getCompanyList(@RequestBody SignCompanySignature querySignature) {
        List<SignCompanySignature> signatures = companyService.getCompanySignatureList(querySignature);
        return result(signatures);
    }

    @RequestMapping("/signature/add")
    public R addSignature(@RequestBody SignCompanySignature querySignature) {
        Long sysUserId = this.getLoginUserId();
        querySignature.setCreateByUserId(sysUserId);
        try {
            int i = companyService.addSignature(querySignature);
            return R.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return R.error(e.getMessage());
        }
    }

    @RequestMapping("/signature/edit")
    public R updateSignatureInfo(@RequestBody SignCompanySignature signature) {
        Long sysUserId = this.getLoginUserId();
        signature.setModifyByUserId(sysUserId);
        try {
            int i = companyService.updateSignatureInfo(signature);
            return R.ok("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return R.error(e.getMessage());
        }
    }

    @RequestMapping("/signature/default")
    public R setDefaultSignature(@RequestBody SignCompanySignature signature) {
        Long sysUserId = this.getLoginUserId();
        signature.setModifyByUserId(sysUserId);
        try {
            int i = companyService.setDefaultSignature(signature);
            return R.ok("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return R.error(e.getMessage());
        }
    }

    @RequestMapping("/signature/remove")
    public R removeSignature(@RequestBody SignCompanySignature signature) {
        try {
            int i = companyService.removeSignature(signature);
            return R.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return R.error(e.getMessage());
        }
    }

    @RequestMapping("/authUrl")
    public R authUrl(@RequestBody Company company) {
        String url = null;
        try {
            url = companyService.getAuthUrl(company);
            return R.ok(url);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(e.getMessage());
        }
    }

    /**
     * 企业认证的回调，前端无需调用
     */
    @RequestMapping("/notify/auth")
    public R notifyAuth(String companyId, String serialNo, String customerId, String status, String statusDesc, String certStatus) {
        logger.debug("===================== callBackUrl : " + baseUrl + "/hr/company/notify/authSign" + " =====================");
        int i = companyService.notifyAuth(companyId, customerId, status, statusDesc, certStatus, serialNo);
        return R.ok();
    }


    @RequestMapping("/authSignUrl")
    public R authSignUrl(@RequestBody Company company) {
        String url = null;
        try {
            url = companyService.getAuthSignUrl(company);
            return R.ok(url);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return R.error(e.getMessage());
        }
    }


    /**
     * 签署合同的回调，前端无需调用
     */
    @RequestMapping("/notify/authSign")
    public R authSign(String companyId, String transaction_id, String contract_id,
                      String result_code, String result_desc, String viewpdf_url) {
        logger.debug("===================== callBackUrl : " + baseUrl + "/hr/sign/notify/authSign" + " =====================");
        logger.debug("===================== downloadUrl : " + viewpdf_url + " =====================");
        if ("3000".equals(result_code) && companyService.notifyAuthSign(companyId, transaction_id, contract_id, result_desc, viewpdf_url)) {
            return R.ok(result_desc);
        }
        return R.error(result_desc);
    }


}
