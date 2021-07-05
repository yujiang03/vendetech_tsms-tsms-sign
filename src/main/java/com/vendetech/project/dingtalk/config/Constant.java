package com.vendetech.project.dingtalk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 项目中的常量定义类
 */

@Component
public class Constant {

	/**企业应用接入秘钥相关*/
	public static String CORP_ID;
	/**开发者后台->企业自建应用->选择您创建的E应用->查看->AppKey*/
	public static String APP_KEY;
	/**开发者后台->企业自建应用->选择您创建的E应用->查看->AppSecret*/
	public static String APP_SECRET;
	public static Long AGENT_ID;
	/**开发者后台->企业自建应用->选择您创建的E应用->登录->AppID & AppSecret*/
	public static String APP_ID;
	public static String APP_SCAN_SECRET;

	@Value("${dingTalk.corpId}")
	public void setCorpId(String corpId) {
		Constant.CORP_ID = corpId;
	}
	@Value("${dingTalk.appKey}")
	public void setAppKey(String appKey) {
		Constant.APP_KEY = appKey;
	}
	@Value("${dingTalk.appSecret}")
	public void setAppSecret(String appSecret) {
		Constant.APP_SECRET = appSecret;
	}
	@Value("${dingTalk.agentId}")
	public void setAgentId(Long agentId) {
		Constant.AGENT_ID = agentId;
	}
	@Value("${dingTalk.appId}")
	public void setAppId(String appId) {
		Constant.APP_ID = appId;
	}
	@Value("${dingTalk.appScanSecret}")
	public void setAppScanSecret(String appScanSecret) {
		Constant.APP_SCAN_SECRET = appScanSecret;
	}


	public static final String TIME_SHEET_NOTIFICATION = "请及时对下属员工分配当月的项目。如果错过当月分配时段，需提交补充分配审批电子流由事业部主管审批，通过后由系统管理员补充分配。谢谢！";
	public static final String ASSIGN_NOTIFICATION = "您当月还未对下属员工分配项目，请尽快分配，如果错过当月分配时段，需提交补充分配审批电子流由事业部主管审批，通过后由系统管理员补充分配。谢谢！";
	public static final String APPLICATION_JSON = "application/json";
	public static final String CONTENT_TYPE_TEXT_JSON = "text/json";

}
