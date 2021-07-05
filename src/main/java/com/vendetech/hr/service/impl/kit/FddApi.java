package com.vendetech.hr.service.impl.kit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fadada.sdk.client.FddClientBase;
import com.fadada.sdk.client.FddClientExtra;
import com.fadada.sdk.client.authForfadada.GetCompanyVerifyUrl;
import com.fadada.sdk.client.authForfadada.GetPersonVerifyUrl;
import com.fadada.sdk.client.authForfadada.model.AgentInfoINO;
import com.fadada.sdk.client.authForfadada.model.BankInfoINO;
import com.fadada.sdk.client.authForfadada.model.CompanyInfoINO;
import com.fadada.sdk.client.authForfadada.model.LegalInfoINO;
import com.fadada.sdk.client.common.FddClient;
import com.fadada.sdk.client.request.ExtsignReq;
import com.fadada.sdk.util.crypt.FddEncryptTool;
import com.fadada.sdk.util.crypt.MsgDigestUtil;
import com.fadada.sdk.util.http.ClientMultipartFormPost;
import com.fadada.sdk.util.http.HttpsUtil;
import com.itextpdf.text.pdf.AcroFields.Item;
import com.vendetech.common.utils.DateUtils;
import com.vendetech.hr.vo.SignRecordVO;
import com.vendetech.project.tsms.domain.Company;
import com.vendetech.project.tsms.domain.DdHrmDataSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class FddApi {

    private String appId;
    private String appSecret;
    private String version;
    private String host;

    private FddClientBase base;

    private String getURLOfSmsText() {
        return this.host + "sms_text.api";
    }
    private String getURLOfQuerySignature() {
        return this.host + "query_signature.api";
    }
    private String getURLOfReplaceSignature() {
        return this.host + "replace_signature.api";
    }
    private String getURLOfRemoveSignature() {
        return this.host + "remove_signature.api";
    }
    private String getURLOfDefaultSignature() {
        return this.host + "default_signature.api";
    }
    private String getURLBeforeAuthSign() {
        return this.host + "before_authsign.api";
    }
    private String getURLCancelAuthSign() {
        return this.host + "cancel_extsign_auto_page.api";
    }
    private String getURLCancellationOfContract() {
        return this.host + "cancellation_of_contract.api";
    }



    public FddApi(String appId, String appSecret, String version, String host) {
        this.base = new FddClientBase(appId, appSecret, version, host);
        this.appId = appId;
        this.appSecret = appSecret;
        this.version = version;
        this.host = host;
    }

    /**
     * 授权页面接口
     * @param transaction_id 交易号
     * @param contract_id 合同编号
     * @param customer_id 客户编号
     * @param return_url 页面跳转URL（签署结果同步跳转页面）
     * @param notify_url 签署结果异步通知URL
     */
    public String beforeAuthSign(String transaction_id, String contract_id, String customer_id,
                                 String return_url, String notify_url) {
        StringBuilder sb = new StringBuilder();
        try {
            //系统时间戳
            String timeStamp = HttpsUtil.getTimeStamp();
            //摘要计算
            String sha1 = FddEncryptTool.sha1(this.appId + FddEncryptTool.md5Digest(transaction_id + timeStamp) +
                    FddEncryptTool.sha1(this.appSecret + customer_id));
            String msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));

            //接口地址
            sb.append(this.getURLBeforeAuthSign());
            //公共参数
            sb.append("?app_id=").append(this.appId);
            sb.append("&v=").append(this.version);
            sb.append("&timestamp=").append(timeStamp);
            sb.append("&msg_digest=").append(msgDigest);

            //业务参数
            sb.append("&transaction_id=").append(transaction_id);
            sb.append("&auth_type=").append("1"); // 1 授权自动签(目前只有1)
            sb.append("&contract_id=").append(contract_id);
            sb.append("&customer_id=").append(customer_id);
            sb.append("&return_url=").append(URLEncoder.encode(return_url, "UTF-8"));
            sb.append("&notify_url=").append(URLEncoder.encode(notify_url, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    /**
     * 取消授权页面接口
     * @param customer_id 客户编号
     * @param return_url 页面跳转URL（签署结果同步跳转页面）
     * @param notify_url 签署结果异步通知URL
     */
    public String cancelAuthSign(String customer_id, String return_url, String notify_url) {
        StringBuilder sb = new StringBuilder();
        HashMap<String, String> params = new HashMap<>();
        try {
            String timeStamp = HttpsUtil.getTimeStamp();
            params.put("Customer_id", customer_id);
            params.put("Notify_url", notify_url);
            params.put("Return_url", return_url);
            String[] sortForParameters = MsgDigestUtil.sortforParameters(params);
            String msgDigest = MsgDigestUtil.getCheckMsgDigest(this.appId, this.appSecret, timeStamp, sortForParameters);

            //接口地址
            sb.append(this.getURLCancelAuthSign());
            //公共参数
            sb.append("?app_id=").append(this.appId);
            sb.append("&v=").append(this.version);
            sb.append("&timestamp=").append(timeStamp);
            sb.append("&msg_digest=").append(msgDigest);

            //业务参数
            sb.append("&customer_id=").append(customer_id);
            sb.append("&return_url=").append(URLEncoder.encode(return_url, "UTF-8"));
            sb.append("&notify_url=").append(URLEncoder.encode(notify_url, "UTF-8"));
        } catch (Exception var17) {
            var17.printStackTrace();
            throw new RuntimeException(var17);
        }

        return sb.toString();
    }

    /**
     * 注册一个账号
     * @param id  ID (个人为employeeId，企业为companyId)
     * @param num 证件号(个人为身份证号，企业为信用代码)
     * @param accountType 1.个人 2.企业
     * @return String 员工在法大大平台的customer_id，需要存储到员工表中
     */
    public JSONObject regAccount(Long id, String num, Integer accountType) {
        String sourceId = id + "_" + num;
        String result = base.invokeregisterAccount(sourceId, String.valueOf(accountType));
        log.info("regAccount: " + result);
        return JSONObject.parseObject(result);
    }

    /**
     * 获取个人实名认证url地址
     * @param customer_id  员工在法大大上的账号ID
     * @param employeeName 员工姓名
     * @param idCardNo     身份证号码
     * @param mobile       手机号码
     * @param returnUrl    法大大认证操作完成，返回到华彩众享的页面，即为 合同列表 上的查看合同详情页面
     * @param notifyUrl    回调函数url
     */
    public JSONObject getAuthPersonUrl(String customer_id, String employeeName, String idCardNo,
                                       String mobile, String returnUrl, String notifyUrl) {
        GetPersonVerifyUrl personVerify = new GetPersonVerifyUrl(this.appId, this.appSecret, this.version, this.host);
        String verified_way = "2"; // 0:身份证正面照片+人脸验证+短信验证码 1：身份正面照片+短信验证码 2：比0多了银行卡 3: 2和0加起来
        String page_modify = "2";
        String customer_ident_type = "0"; // 0：身份证 1：其他
        String ident_front_path = "";
        String result = personVerify.invokePersonVerifyUrl(customer_id, verified_way, page_modify, notifyUrl, returnUrl,
                employeeName, customer_ident_type, idCardNo, mobile, ident_front_path, "", "1");
        JSONObject jsonRet = JSONObject.parseObject(result);
        log.info("getAuthPersonUrl: " + result);

        return jsonRet;
    }

    /**
     * 上传合同模板，每个企业，每个模板只上传一次
     * @param template_id 自己定义，在华彩众享平台唯一即可，可以用用工类型表的合同号+企业ID
     */
    public JSONObject uploadContractTpl(String template_id, String tplFilePath) {
        String doc_url = "";
        File file = new File(tplFilePath);
        String result = base.invokeUploadTemplate(template_id, file, doc_url);
        log.info("uploadContractTpl: " + result);
        return JSONObject.parseObject(result);
    }

    /**
     * 自动签章
     * @param title      法大大上的id
     * @param contractNo 填充合同时设置的合同号
     */
    public JSONObject autoSign(String customerId, String title, String contractNo, String signKey) {
        ExtsignReq req = new ExtsignReq();
        req.setCustomer_id(customerId); // 甲方：深圳市文德数慧科技开发有限责任公司
        String transaction_id = "TRANS_" + HttpsUtil.getTimeStamp();
        req.setTransaction_id(transaction_id);
        req.setContract_id(contractNo);
        req.setClient_role("1");
        req.setSign_keyword(signKey);
        req.setKeyword_strategy("0");  // 关键字签章策略：0：所有关键字签章；1：第一个关键字签章；2：最后一个关键字签章
        req.setDoc_title(title);
        // req.setNotify_url(notifyUrl); // 通知回调地址
        String result = base.invokeExtSignAuto(req);
        return JSON.parseObject(result);
    }

    /**
     * 根据合同号，获取发大大pdf下载地址
     */
    public String getDownLoadUrl(String contractNum) {
        FddClientExtra extra = new FddClientExtra(this.appId, this.appSecret, this.version, this.host);
        return extra.invokeDownloadPdf(contractNum);
    }

    /**
     * @param customer_id 员工在法大大上的账号ID
     * @param title       合同标题
     * @param contractNo  合同号
     * @param returnUrl   在法大大平台签署后，返回到华彩众享的页面web地址，要求全路径Http地址
     * @param notifyUrl   异步通知员工手签合同结果，华彩众享要开发这个接口
     * @return 签署页面地址
     */
    public String employeeSign(String customer_id, String title, String contractNo,
                               String returnUrl, String notifyUrl, String signKey) {
        ExtsignReq req = new ExtsignReq();
        req.setCustomer_id(customer_id);
        String transaction_id = "TRANS_" + HttpsUtil.getTimeStamp();
        req.setTransaction_id(transaction_id);
        req.setContract_id(contractNo);
        req.setDoc_title(title);
        req.setReturn_url(returnUrl);
        req.setNotify_url(notifyUrl);
        req.setKeyword_strategy("0");  //最后一个关键字处签章
        req.setSign_keyword(signKey);

        // mobile_sign_type: 0:全部；1：标准；2：手写
        // sign_verify_way: 1 短信 3 刷脸
        return base.invokeExtSign(req) + "&mobile_sign_type=2&sign_verify_way=3";
    }

    /**
     * 发送自定义短信
     * @param mobile         手机号码
     * @param messageType    默认2，1 填充模板：调用方传 code，云端将其 填充模板生成短信内容并发送 2 自定义内容：调用方传 message_content，云端将其发送给用
     * @param messageContent 内容
     * @param code           验证码 随便填，不能为null
     */
    public String sendSmsContent(String mobile, Integer messageType, String messageContent, String code) throws RuntimeException {
        FddClient client = new FddClient(this.appId, this.appSecret, this.version, this.host);
        ArrayList<NameValuePair> params = new ArrayList<>();

        mobile = FddEncryptTool.encrypt(mobile, client.getSecret());

        try {
            String timeStamp = HttpsUtil.getTimeStamp();
            String sha1 = FddEncryptTool.sha1(client.getAppId()
                    + FddEncryptTool.md5Digest(timeStamp + mobile + messageType + messageContent + code)
                    + FddEncryptTool.sha1(client.getSecret()));
            String msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));

            params.add(new BasicNameValuePair("mobile", mobile));
            params.add(new BasicNameValuePair("message_type", messageType.toString()));
            params.add(new BasicNameValuePair("message_content", messageContent));
            params.add(new BasicNameValuePair("code", code));
            // if(!StringUtils.isBlank(code)){
            // }
            params.add(new BasicNameValuePair("app_id", client.getAppId()));
            params.add(new BasicNameValuePair("timestamp", timeStamp));
            params.add(new BasicNameValuePair("v", client.getVersion()));
            params.add(new BasicNameValuePair("msg_digest", msgDigest));
        } catch (Exception var6) {
            var6.printStackTrace();
            throw new RuntimeException(var6);
        }
        return HttpsUtil.doPost(this.getURLOfSmsText(), params);
    }

    /**
     * 归档合同，归档后就不能操作了，该操作，在异步通知员工手签合同结果接口中调用
     */
    public String contractFiling(String contractNo) {
        return base.invokeContractFilling(contractNo);
    }

    /**
     * 数据填充到合同模板，并上传合同
     * @param sr        待签署协议详情
     * @param ddHrmData 待签署协议乙方详情
     * @param pdfPath   待签署协议合同模板本地路径
     */
    public JSONObject fillContract(SignRecordVO sr, DdHrmDataSign ddHrmData, String pdfPath) throws ParseException {

        String templateId = sr.getTemplateId() + "_" + sr.getTplCode();
        String contractNo = sr.getContractNo();
        String title = sr.getContractNo() + " 的用工合同";
        JSONObject parameter = createTplParam(contractNo, ddHrmData, pdfPath);

        if (parameter == null) {
            return null;
        }

        String font_size = "10.5";  //用默认即可，不用填
        String font_type = "4";     //0-宋体；1-仿宋；2-黑体；3-楷体；4-微软雅黑
        // String dynamic_tables = null;  //用于表格的，目前用不到，不要启用
        String result = base.invokeGenerateContract(templateId, contractNo, title,
                font_size, font_type, parameter.toJSONString(), null);
        log.info("fillContract: " + result);

        return JSONObject.parseObject(result);
    }

    /**
     * 组装填充合同模板参数
     */
    private JSONObject createTplParam(String contractNo, DdHrmDataSign dataSign, String pdfPath) throws ParseException {
        Map<String, Item> map = PDFUtil.readAcrobatFields(pdfPath);
        if (map == null) {
            return null;
        }

        JSONObject json = new JSONObject();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            if ("empName".equals(key)) {
                json.put("empName", dataSign.getDdName());
            }
            if ("sex".equals(key)) {
                json.put("sex", dataSign.getDdSexType() == 0 ? "男" : "女");
            }
            if ("birthTime".equals(key)) {
                json.put("birthTime", dataSign.getDdBirthTime());
            }
            if ("certAddress".equals(key)) {
                json.put("certAddress", dataSign.getDdCertAddress());
            }
            if ("empAddress".equals(key)) {
                json.put("empAddress", dataSign.getDdAddress());
            }
            if ("certNo".equals(key)) {
                json.put("certNo", dataSign.getDdCertNo());
            }
            if ("mobile".equals(key)) {
                json.put("mobile", dataSign.getDdMobile());
            }
            if ("workCity".equals(key)) {
                json.put("workCity", dataSign.getDdWorkCity());
            }
            if ("nowContractStartTme".equals(key)) {
                json.put("nowContractStartTme", dataSign.getDdNowContractStartTime());
            }
            if ("nowContractStartTime".equals(key)) {
                json.put("nowContractStartTime", dataSign.getDdNowContractStartTime());
            }
            if ("nowContractEndTime".equals(key)) {
                json.put("nowContractEndTime", dataSign.getDdNowContractEndTime());
            }
            if ("probationPeriodType".equals(key)) {
                json.put("probationPeriodType", dataSign.getDdProbationPeriodType());
            }
            if ("regularTime".equals(key)) {
                json.put("regularTime", dataSign.getDdRegularTime());
            }
            if ("confirmJoinTime".equals(key)) {
                json.put("confirmJoinTime", dataSign.getDdConfirmJoinTime());
            }
            if ("trainingContractEnd".equals(key)) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date confirmJoinDate = sdf.parse(dataSign.getDdConfirmJoinTime());
                Date trainingContractEnd = DateUtils.addDays(confirmJoinDate, 15);
                json.put("trainingContractEnd", sdf.format(trainingContractEnd));

                Date trainingContractWorkEnd = DateUtils.addMonths(trainingContractEnd, 3);
                json.put("trainingContractWorkEnd", sdf.format(trainingContractWorkEnd));
            }
            if ("position".equals(key)) {
                json.put("position", dataSign.getDdPosition());
            }

            if ("graduateSchool".equals(key)) {
                json.put("graduateSchool", dataSign.getDdGraduateSchool());
            }
            // 非花名册字段
            if ("contractNo".equals(key)) {
                json.put("contractNo", contractNo);
            }
        }

        return json;
    }

    /**
     * 获取企业实名认证地址
     * @param company 企业信息实体
     */
    public JSONObject getCompanyAuthUrl(Company company, String returnUrl, String notifyUrl) {
        GetCompanyVerifyUrl companyVerify = new GetCompanyVerifyUrl(this.appId, this.appSecret, this.version, this.host);

        CompanyInfoINO companyInfo = new CompanyInfoINO();      // 企业信息
        companyInfo.setCompany_name(company.getCompanyName());  // 企业名称
        companyInfo.setCredit_no(company.getCreditNo());        // 统一社会信用代码
        companyInfo.setCredit_image_path(company.getProxyPath() + company.getCreditImagePath());   // 统一社会信用代码证件照路径

        BankInfoINO bankInfo = new BankInfoINO();       // 对公账号信息
        bankInfo.setBank_name(company.getBankName());   // 银行名称
        bankInfo.setBank_id(company.getBankId());       // 银行帐号
        bankInfo.setSubbranch_name(company.getSubBranchName()); // 开户支行名称

        LegalInfoINO legalInfo = new LegalInfoINO();            // 法人信息
        legalInfo.setLegal_name(company.getLegalName());        // 法人姓名
        legalInfo.setLegal_id(company.getLegalId());            // 法人证件号(身份证)
        legalInfo.setLegal_mobile(company.getLegalMobile());    // 法人手机号(仅支持国内运营商)
        legalInfo.setLegal_id_front_path(company.getProxyPath() + company.getLegalIdFrontPath());   // 法人证件正面照下载地址

        String customer_id = company.getCustomerId();   // 客户编号 必填
        String verified_way = "0";           // 实名认证套餐类(默认0): 0.标准方案(对公打款+纸质审核) 1.对公打款 2.纸质审核
        String m_verified_way = "2";         // 管理员实名认证套餐类型
        String page_modify = "1";            // 是否允许用户页面修改
        String company_principal_type = "2"; // 1.法人，2 代理人
        String result_type = "2";   // 刷脸是否显示结果页面，参数值为“1”：直接跳转到 return_url 或法大大指定页面，参数值为“2”：需要用户点击确认后跳转到return_url或法大大指定页面
        String cert_flag = "1"; // 是否认证成功后自动申请实名证书：参数值为“0”：不申请，参数值为“1”：自动申请

        String ret = companyVerify.invokeCompanyVerifyUrl(companyInfo, bankInfo, legalInfo,
                new AgentInfoINO(), customer_id, verified_way, m_verified_way, page_modify,
                company_principal_type, returnUrl, notifyUrl, result_type, cert_flag);
        log.info("法大大返回：" + ret);
        return JSONObject.parseObject(ret);
    }

    /**
     * 新增签章
     * @param customerId 公司customerId
     * @param base64Str 印章base64
     */
    public JSONObject addSignature(String customerId, String base64Str) {
        String ret = base.invokeaddSignature(customerId, base64Str);
        log.info("法大大返回：" + ret);
        return JSONObject.parseObject(ret);
    }

    /**
     * 获取所有公章
     * @param customerId 客户账号
     * @param fddSignatureId 不传则查全部
     */
    public JSONObject querySignature(String customerId, String fddSignatureId) {
        ArrayList<NameValuePair> params = new ArrayList<>();

        try {
            String timeStamp = HttpsUtil.getTimeStamp();
            String sha1 = FddEncryptTool.sha1(this.appId + FddEncryptTool.md5Digest(timeStamp)
                    + FddEncryptTool.sha1(this.appSecret + customerId + fddSignatureId));
            String msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));
            params.add(new BasicNameValuePair("customer_id", customerId));
            if(fddSignatureId != null) {
                params.add(new BasicNameValuePair("signature_id", fddSignatureId));
            }
            params.add(new BasicNameValuePair("app_id", this.appId));
            params.add(new BasicNameValuePair("timestamp", timeStamp));
            params.add(new BasicNameValuePair("v", this.version));
            params.add(new BasicNameValuePair("msg_digest", msgDigest));
        } catch (Exception var7) {
            var7.printStackTrace();
            throw new RuntimeException(var7);
        }
        String ret = HttpsUtil.doPost(getURLOfQuerySignature(), params);
        log.info("法大大返回：" + ret);
        return JSONObject.parseObject(ret);
    }

    /**
     * 替换公章
     * @param signatureId 法大大印章id
     */
    public JSONObject replaceSignature(String customerId, String signatureId, String base64Str) {
        ArrayList<NameValuePair> params = new ArrayList<>();

        try {
            String timeStamp = HttpsUtil.getTimeStamp();
            String sha1 = FddEncryptTool.sha1(this.appId + FddEncryptTool.md5Digest(timeStamp)
                    + FddEncryptTool.sha1(this.appSecret + customerId + signatureId + base64Str));
            String msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));
            params.add(new BasicNameValuePair("customer_id", customerId));
            params.add(new BasicNameValuePair("signature_id", signatureId));
            params.add(new BasicNameValuePair("signature_img_base64", base64Str));
            params.add(new BasicNameValuePair("app_id", this.appId));
            params.add(new BasicNameValuePair("timestamp", timeStamp));
            params.add(new BasicNameValuePair("v", this.version));
            params.add(new BasicNameValuePair("msg_digest", msgDigest));
        } catch (Exception var7) {
            var7.printStackTrace();
            throw new RuntimeException(var7);
        }
        String ret = HttpsUtil.doPost(getURLOfReplaceSignature(), params);
        log.info("法大大返回：" + ret);
        return JSONObject.parseObject(ret);
    }

    /**
     * 移除已有印章
     */
    public JSONObject removeSignature(String customerId, String signatureId) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        signatureGenerate(customerId, signatureId, params);
        String ret = HttpsUtil.doPost(getURLOfRemoveSignature(), params);
        log.info("法大大返回：" + ret);
        return JSONObject.parseObject(ret);
    }

    /**
     * 设置默认印章
     */
    public JSONObject defaultSignature(String customerId, String signatureId) {
        ArrayList<NameValuePair> params = new ArrayList<>();
        signatureGenerate(customerId, signatureId, params);
        String ret = HttpsUtil.doPost(getURLOfDefaultSignature(), params);
        log.info("法大大返回：" + ret);
        return JSONObject.parseObject(ret);
    }

    /**
     * 印章接口参数填充
     */
    private void signatureGenerate(String customerId, String signatureId, ArrayList<NameValuePair> params) {
        try {
            String timeStamp = HttpsUtil.getTimeStamp();
            String sha1 = FddEncryptTool.sha1(this.appId + FddEncryptTool.md5Digest(timeStamp)
                    + FddEncryptTool.sha1(this.appSecret + customerId + signatureId));
            String msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));
            params.add(new BasicNameValuePair("customer_id", customerId));
            params.add(new BasicNameValuePair("signature_id", signatureId));
            params.add(new BasicNameValuePair("app_id", this.appId));
            params.add(new BasicNameValuePair("timestamp", timeStamp));
            params.add(new BasicNameValuePair("v", this.version));
            params.add(new BasicNameValuePair("msg_digest", msgDigest));
        } catch (Exception var7) {
            var7.printStackTrace();
            throw new RuntimeException(var7);
        }
    }

    /**
     * 获取短连接
     */
    public String getShortUrl(String url) {
        FddClient client = new FddClient(this.appId, this.appSecret, this.version, this.host);
        ArrayList<NameValuePair> requestParams = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>();

        try {
            String timeStamp = HttpsUtil.getTimeStamp();
            params.put("expire_time", "60");
            params.put("source_url", url);
            String[] sortForParameters = MsgDigestUtil.sortforParameters(params);
            String msgDigest = MsgDigestUtil.getCheckMsgDigest(client.getAppId(), client.getSecret(), timeStamp, sortForParameters);

            requestParams.add(new BasicNameValuePair("expire_time", "60"));
            requestParams.add(new BasicNameValuePair("source_url", url));
            requestParams.add(new BasicNameValuePair("app_id", client.getAppId()));
            requestParams.add(new BasicNameValuePair("timestamp", timeStamp));
            requestParams.add(new BasicNameValuePair("v", client.getVersion()));
            requestParams.add(new BasicNameValuePair("msg_digest", msgDigest));
        } catch (Exception var6) {
            var6.printStackTrace();
            throw new RuntimeException(var6);
        }
        return HttpsUtil.doPost(this.host + "short_url.api", requestParams);
    }


    /**
     * base64解码
     */
    public String decode(String bizContent) {
        try {
            bizContent = URLDecoder.decode(bizContent, "utf-8");
            bizContent = new String(Base64.decodeBase64(bizContent.getBytes()));
        } catch (UnsupportedEncodingException e) {
            return "";
        }
        return bizContent;
    }



    public String cancellationOfContract(String contract_id) {
        ArrayList<NameValuePair> params = new ArrayList<>();

        try {
            String timeStamp = HttpsUtil.getTimeStamp();
            String sha1 = FddEncryptTool.sha1(this.appId + FddEncryptTool.md5Digest(timeStamp)
                    + FddEncryptTool.sha1(this.appSecret + contract_id));

            String msgDigest = new String(FddEncryptTool.Base64Encode(sha1.getBytes()));
            params.add(new BasicNameValuePair("app_id", this.appId));
            params.add(new BasicNameValuePair("v", this.version));
            params.add(new BasicNameValuePair("timestamp", timeStamp));
            params.add(new BasicNameValuePair("contract_id", contract_id));
            params.add(new BasicNameValuePair("msg_digest", msgDigest));
        } catch (Exception var7) {
            var7.printStackTrace();
            throw new RuntimeException(var7);
        }

        return HttpsUtil.doPost(this.getURLCancellationOfContract(), params);
    }


    public static void main(String[] args) {
        FddApi api = new FddApi("403043", "go8oCEhI4TWW0qDBUNAFlycx", "2.0", "https://testapi.fadada.com:8443/api/");

        String ret = api.cancelAuthSign("BF661617128A78D4B5ED9376D05F7F5E", "1", "2");
        System.out.println(ret);

        // String authSingUrl = api.beforeAuthSign("123", "123", "DC789F36146EDC3416C5CFB93CC780A6", "1", "2");
        // System.out.println(authSingUrl);

        // this.testGetCompanyAuthUrl("", "");
        // api.getSignatureList(api, "DC789F36146EDC3416C5CFB93CC780A6");
    }

    private void testGetCompanyAuthUrl(String returnUrl, String notifyUrl) {
        Company company = new Company();
        company.setBankName("中国银行");
        company.setBankId("6227524803198509");
        company.setSubBranchName("桃源居支行");

        company.setLegalName("周金鹏");
        company.setLegalId("421181198912183970");
        company.setLegalBankCardNo("6013822000615317958");
        company.setLegalIdFrontPath("C:\\Users\\allan\\Desktop\\图片\\credit.jpg");
        company.setLegalMobile("15002088150");

        company.setCompanyName("文德数慧");
        company.setCreditNo("123456");
        company.setCreditImagePath("");

        company.setCustomerId("");

        JSONObject ret = getCompanyAuthUrl(company, returnUrl, notifyUrl);
        System.out.println(ret);
    }
    private void getSignatureList(FddApi api, String customerId) {
        // FddApi api = new FddApi("403043", "go8oCEhI4TWW0qDBUNAFlycx", "2.0", "https://testapi.fadada.com:8443/api/");
        JSONObject ret = api.querySignature(customerId, null);
        System.out.println(ret);
    }

}
