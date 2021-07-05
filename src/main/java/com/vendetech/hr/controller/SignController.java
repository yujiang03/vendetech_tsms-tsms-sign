package com.vendetech.hr.controller;

import com.alibaba.fastjson.JSONObject;
import com.vendetech.common.utils.SecurityUtils;
import com.vendetech.framework.security.LoginUser;
import com.vendetech.framework.security.service.TokenService;
import com.vendetech.hr.service.ISignService;
import com.vendetech.hr.service.impl.kit.BankCardKit;
import com.vendetech.hr.service.impl.kit.SignKit;
import com.vendetech.hr.vo.*;
import com.vendetech.project.tsms.domain.Employee;
import com.vendetech.project.tsms.domain.R;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("hr/sign")
public class SignController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${base.url}")
    private String baseUrl;
    @Autowired
    private ISignService signService;
    @Autowired
    private TokenService utilUserService;

    /**
     * 协议配置：新增
     */
    @RequestMapping("/rel/add")
    public R addRel(@RequestBody SignEmployeeRelVO signEmployeeRelVO) {
        if (signEmployeeRelVO.getTemplateId() == null || signEmployeeRelVO.getEmployeeId() == null) {
            return R.errorForMissingNecessaryParam();
        }
        if (signService.checkExistSignEmployeeRel(signEmployeeRelVO.getTemplateId(), signEmployeeRelVO.getEmployeeId())) {
            return R.error("已存在相同的成员-协议模板-当日配置");
        }
        Long sysUserId = this.getLoginUserId();
        return signService.addSignEmployeeRel(signEmployeeRelVO, sysUserId) ? R.ok() : R.error();
    }

    /**
     * 协议配置：新增-获取协议模板列表
     */
    @RequestMapping("/rel/getTplList")
    public R getRelTemplateList(@RequestBody SignTemplateVO vo) {
        return result(signService.getRelTemplateList(vo));
    }

    /**
     * 协议配置：新增-获取成员列表
     */
    @RequestMapping("/rel/getEmployeeList")
    public R getRelEmployeeList() {
        return R.data(signService.getRelEmployeeList());
    }

    /**
     * 分页查询协议配置
     */
    @RequestMapping("/rel/list")
    public R listSignEmployeeRel(@RequestBody BaseVO baseVO) {
        return result(signService.listSignEmployeeRel(baseVO.getPageNum(), baseVO.getPageSize()));
    }

    /**
     * 获取银行卡信息
     */
    @RequestMapping("/getCiBankCardInfo")
    public R getCiBankCardInfo(@RequestBody SignParamVO param) {
        return R.data(JSONObject.parse(BankCardKit.getCiBankCard(param.getCiNumber())));
    }

    /**
     * 获取签署合同的url
     */
    @RequestMapping("/getSignUrl")
    public R getSignUrl(@RequestBody SignParamVO param) {
        Long employeeId = this.getLoginEmployeeId();
        if (employeeId == null) {
            return R.error("当前登录用户信息不存在!");
        }
        try {
            String returnUrl = param.getReturnUrl() == null ? "" : param.getReturnUrl();
            String url = signService.getSignUrl(employeeId, returnUrl, param.getContractNo());
            Map<String, Object> map = new HashMap<>();
            map.put("url", url);
            return R.ok(map);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return R.error("调用法大大接口异常：" + e.getMessage());
        }
    }

    /**
     * 签署合同的回调，前端无需调用
     */
    @RequestMapping("/notify/sign")
    public R notifySign(String contract_id, String result_code, String result_desc, String download_url) {
        logger.debug("===================== callBackUrl : " + baseUrl + "/hr/sign/notify/sign" + " =====================");
        logger.debug("===================== downloadUrl : " + download_url + " =====================");
        if ("3000".equals(result_code) && signService.notifySign(contract_id, download_url)) {
            return R.ok(result_desc);
        }
        return R.error(result_desc);
    }

    /**
     * 公司自动签章
     */
    @RequestMapping("/companySign/autoSign")
    public R autoSign(@RequestBody SignParamVO param) {
        Long employeeId = this.getLoginEmployeeId();
        if (employeeId == null) {
            return R.error("当前登录用户没有信息不完整，请咨询管理员！");
        }
        try {
            JSONObject ret = signService.autoSign(param.getCompanyCustomerId(), param.getContractNo());
            return R.ok(ret.get("msg").toString());
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return R.error("调用法大大接口异常：" + e.getMessage());
        }
    }

    /**
     * 获取身份认证URL
     */
    @RequestMapping("/getAuthUrl")
    public R getAuthUrl(@RequestBody SignParamVO param) {
        Long employeeId = this.getLoginEmployeeId();
        if (employeeId == null) {
            return R.error("当前登录用户没有军团归属");
        }
        try {
            String url = signService.getAuthUrl(employeeId, param.getReturnUrl());
            Map<String, Object> map = new HashMap<>();
            map.put("url", url);
            return R.ok(map);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return R.error("调用法大大接口异常：" + e.getMessage());
        }
    }

    /**
     * 身份认证的回调，前端无需调用
     *
     * @param employeeId 员工id
     */
    @RequestMapping("/notify/auth")
    public R notifyAuth(Long employeeId, String serialNo, String customerId, Integer status, String statusDesc, Integer certStatus) {
        logger.debug("===================== callBackUrl : " + baseUrl + "/hr/sign/notify/auth" + " =====================");
        return signService.notifyAuth(employeeId, serialNo, customerId, status, statusDesc, certStatus) ? R.ok() : R.error();
    }

    /**
     * 查询该成员的认证状态
     */
    @RequestMapping("/getEmployeeAuth")
    public R getEmployeeAuth() {
        Long employeeId = this.getLoginEmployeeId();
        String authStatus = signService.getEmployeeAuth(employeeId);
        Map<String, Object> map = new HashMap<>();
        map.put("authStatus", authStatus);
        return R.ok(map);
    }

    /**
     * 分页查询协议记录
     */
    @RequestMapping("/record/list")
    public R listSignRecord(@RequestBody SignProtocolVO vo) {
        return result(signService.listSignProtocol(vo));
    }

    /**
     * 员工分页查询协议记录
     */
    @RequestMapping("/record/employee/list")
    public R listEmployeeSignRecord(@RequestBody SignProtocolVO vo) {
        vo.setEmployeeId(getLoginEmployeeId());
        return result(signService.listSignProtocol(vo));
    }

    /**
     * 分页查询协议记录
     */
    @RequestMapping("/record/myList")
    public R myListSignRecord(@RequestBody SignProtocolVO vo) {
        Long employeeId = this.getLoginEmployeeId();
        vo.setEmployeeId(employeeId);
        return result(signService.listSignProtocol(vo));
    }

    /**
     * 查询当前协议(byId)
     */
    @RequestMapping("/record")
    public R querySignRecordById(@RequestBody SignProtocolVO vo) {
        List<SignProtocolVO> signProtocolVOList = signService.listSignProtocol(vo);
        if (!signProtocolVOList.isEmpty()) {
            return toResult(signProtocolVOList.get(0));
        } else {
            return R.error("未查询到当前协议");
        }
    }

    /**
     * 查询当前协议
     */
    @RequestMapping("/companySign/list")
    public R querySignRecordPrepareSignByCompany(@RequestBody SignProtocolVO vo) {
        List<SignProtocolVO> signProtocolVOList = signService.listSignProtocol(vo);
        return result(signProtocolVOList);
    }

    /**
     * 废除协议模板
     */
    @RequestMapping("/template/delete")
    public R deleteSignTemplate(@RequestBody SignParamVO param) {
        if (!signService.checkExistSignTemplateByTplCode(param.getTplCode())) {
            return R.error(203, "该协议模板不存在");
        }
        Long sysUserId = this.getLoginUserId();
        return signService.deleteSignTemplate(param.getTplCode(), sysUserId) ? R.ok() : R.error();
    }

    /**
     * 发布协议模板
     */
    @RequestMapping("/template/publish")
    public R publishSignTemplate(@RequestBody SignParamVO param) {
        if (!signService.checkExistSignTemplateByTplCode(param.getTplCode())) {
            return R.error(203, "该协议模板不存在");
        }
        Long sysUserId = this.getLoginUserId();
        return signService.publishSignTemplate(param.getTplCode(), sysUserId) ? R.ok() : R.error();
    }

    /**
     * 新增协议模板
     */
    @RequestMapping("/template/add")
    public R addSignTemplate(SignTemplateVO vo, MultipartFile file) {
        Long sysUserId = this.getLoginUserId();
        // String msg = signService.checkTemplateForAdd(vo, file);
        // if (msg != null) {
        //     return R.error(msg);
        // }
        try {
            String tplPath = signService.uploadTpl(file);
            return signService.addSignTemplate(vo, tplPath, sysUserId) ? R.ok() : R.error();
        } catch (IOException e) {
            e.printStackTrace();
            return R.error("协议模板文件上传异常");
        }
    }

    /**
     * 更新协议模板
     */
    @RequestMapping("/template/update")
    public R updateSignTemplate(SignTemplateVO vo, MultipartFile file) {
        Long sysUserId = this.getLoginUserId();
        // String msg = signService.checkTemplateForUpdate(vo);
        // if (msg != null) {
        //     return R.error(msg);
        // }
        try {
            String tplPath = null;
            if (file != null) {
                tplPath = signService.uploadTpl(file);
            }
            return signService.updateSignTemplate(vo, tplPath, sysUserId) ? R.ok() : R.error();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return R.error("协议模板文件上传异常");
        }
    }

    /**
     * 协议统计：获取签署人列表
     */
    @RequestMapping("/statistics/getSignEmployeeList")
    public R getSignEmployeeList(@RequestBody SignParamVO param) {
        return R.data(signService.getSignEmployeeListByTplIdAndType(param.getTplId(), param.getType()));
    }

    /**
     * 分页查询协议统计
     */
    @RequestMapping("/statistics/list")
    public R listSignCount(@RequestBody BaseVO baseVO) {
        return result(signService.listSignStatistics(baseVO.getPageNum(), baseVO.getPageSize()));
    }

    /**
     * 分页查询协议模板
     */
    @RequestMapping("/template/list")
    public R listSignTemplate(@RequestBody SignTemplateVO baseVO) {
        return result(signService.listSignTemplate(baseVO.getPageNum(), baseVO.getPageSize()));
    }

    /**
     * 分页查询协议模板
     */
    @RequestMapping("/employee/info")
    public R getEmployeeInfo() {
        return toResult(signService.getEmployeeById(this.getLoginEmployeeId()));
    }


    /**
     * 更新职员信息
     */
    @RequestMapping("/employee/update")
    public R updateEmployee(@RequestBody Employee e) {
        e.setEmployeeId(this.getLoginEmployeeId());
        signService.updateEmployeeById(e);
        return toResult(e);
    }

    /**
     * 二次审核通过
     */
    @RequestMapping("/verify/pass")
    public R verifyPass(@RequestBody SignRecordVO vo) {
        signService.updateSignRecordStatus(SignKit.RECORD_STATUS_VERIFY_PASS, vo.getContractNo());
        return R.ok();
    }

    /**
     * 二次审核不通过
     */
    @RequestMapping("/verify/fail")
    public R verifyFail(@RequestBody SignRecordVO vo) {
        signService.updateSignRecordStatus(SignKit.RECORD_STATUS_VERIFY_FAIL, vo.getContractNo());
        return R.ok();
    }

    /**
     * 撤销合同
     */
    @RequestMapping("/record/cancel")
    public R cancellationOfContract(@RequestBody SignRecordVO vo) {
        try {
            signService.cancelRecord(vo.getContractNo());
            return R.ok();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return R.error("调用法大大接口异常：" + e.getMessage());
        }
    }


}
