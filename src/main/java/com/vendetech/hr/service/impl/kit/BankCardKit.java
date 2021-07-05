package com.vendetech.hr.service.impl.kit;

import com.alibaba.fastjson.JSONObject;
import com.vendetech.common.utils.http.HttpUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


/**
 * 银行卡验证工具类，原理是调用阿里银行卡号校验接口
 *
 * @author larry
 */
@Component
public class BankCardKit {
    private static Logger logger = LoggerFactory.getLogger(BankCardKit.class);

    private static String bankJsonPath;
    private static String tltBankJsonPath;
    @Value("${payConfig.bankConf}")
    public void setBankJsonPath(String bankJsonPath) {
        BankCardKit.bankJsonPath = bankJsonPath;
    }
    @Value("${payConfig.tltBankConf}")
    public void setTltBankJsonPath(String tltBankJsonPath) {
        BankCardKit.tltBankJsonPath = tltBankJsonPath;
    }


    private static Map<String, String> bankMap = new HashMap<>();
    private static Map<String, String> tltBankMap = new HashMap<>();
    private static Map<String, String> availableBankMap = new HashMap<>();

    private static void generateBankConf() {
        String bankConf = readFile(bankJsonPath);
        logger.info(bankConf);
        bankMap = (Map<String, String>) JSONObject.parse(bankConf);

        String tltBankConf = readFile(tltBankJsonPath);
        logger.info(tltBankConf);
        tltBankMap = (Map<String, String>) JSONObject.parse(tltBankConf);

        for (Map.Entry<String, String> entry : bankMap.entrySet()) {
            if (!StringUtils.isBlank(tltBankMap != null ? tltBankMap.get(entry.getValue()) : null)) {
                availableBankMap.put(entry.getValue(), tltBankMap.get(entry.getValue()));
            }
        }
        logger.info("================ 银行信息加载成功 ================");
    }

    private static String readFile(String path) {
        if (!new File(path).exists()) {
            return null;
        }
        StringBuilder content = new StringBuilder("");
        FileInputStream fis = null;
        BufferedReader reader = null;
        InputStreamReader ins = null;
        try {
            fis = new FileInputStream(path);
            ins = new InputStreamReader(fis, StandardCharsets.UTF_8);
            reader = new BufferedReader(ins);
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(reader, ins, fis);
        }
        return content.toString();
    }

    public static void close(Closeable... closeables) {
        if (closeables == null || closeables.length == 0) {
            return;
        }
        String cur = "";
        try {
            for (Closeable c : closeables) {
                if (c != null) {
                    cur = c.getClass().getName();
                    c.close();
                }
            }
        } catch (IOException e) {
            System.out.println("close stream[" + cur + "] fail ");
        }
    }

    public static String checkBankCard(String ciBank, String cardNo) {
        JSONObject retData = new JSONObject();

        String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json?";
        String param = "cardNo=" + cardNo + "&cardBinCheck=true";
        String ret = HttpUtils.sendGet(url, param, null);
        JSONObject retObj = JSONObject.parseObject(ret);

        if ("false".equals(retObj.getString("validated"))) {
            retData.put("msg", "银行卡信息错误，请输入正确卡号！");
            retData.put("code", 500);
            return retData.toJSONString();
        }

        if ("CC".equals(retObj.getString("cardType"))) {
            retData.put("msg", "不支持信用卡，请输入储蓄卡卡号！");
            retData.put("code", 500);
            return retData.toJSONString();
        }

        String bankCode = retObj.getString("bank");
        String bankName = bankMap.get(bankCode);
        retObj.put("bankname", bankName);

        if (!ciBank.equals(bankName)) {
            retData.put("msg", "银行名称与卡号不符，请重新选择！");
            retData.put("code", 500);
            return retData.toJSONString();
        }

        //根据name获取通联通 的银行编码
        String tltCode = getTltBankCode(bankName);
        if (!StringUtils.isBlank(tltCode)) {
            retObj.put("tltBankCode", tltCode);
            retObj.put("msg", "银行卡号信息变更成功！");
            retObj.put("code", 200);
            return retObj.toJSONString();
        } else {
            retData.put("msg", "未查到该卡信息，请更换卡号再尝试！");
            retData.put("code", 500);
            return retData.toJSONString();
        }
    }

    public static String getCiBankCard(String cardNo) {
        if (bankMap.isEmpty()) {
            generateBankConf();
        }

        JSONObject retData = new JSONObject();
        cardNo = cardNo.replace(" ", "");
        String url = "https://ccdcapi.alipay.com/validateAndCacheCardInfo.json";
        String param = "cardNo=" + cardNo + "&cardBinCheck=true";
        String ret = HttpUtils.sendGet(url, param, null);
        JSONObject retObj = JSONObject.parseObject(ret);

        if ("false".equals(retObj.getString("validated"))) {
            retData.put("msg", "银行卡信息错误，请输入正确卡号！");
            retData.put("code", 500);
            return retData.toJSONString();
        }

        if ("CC".equals(retObj.getString("cardType"))) {
            retData.put("msg", "不支持信用卡，请输入储蓄卡卡号！");
            retData.put("code", 500);
            return retData.toJSONString();
        }

        String bankCode = retObj.getString("bank");
        String bankName = bankMap.get(bankCode);
        retData.put("msg", bankName);
        retData.put("code", 200);

        return retData.toJSONString();
    }

    /**
     * 根据银行名称获取通联通 约定的银行代码
     */
    private static String getTltBankCode(String bankName) {
        if (tltBankMap.containsKey(bankName)) {
            return tltBankMap.get(bankName);
        }
        return null;
    }

    public static Map<String, String> getAvailableBankMap() {
        return availableBankMap;
    }

    public static Map<String, String> getTltBankCardInfo() {
        return tltBankMap;
    }

    public static Map<String, String> getAliBankCardInfo() {
        return bankMap;
    }

    public static void main(String[] args) {
        // String ret = checkBankCard("中国银行", "6214856551133535");
        generateBankConf();
        System.out.println(bankMap);
    }

}
