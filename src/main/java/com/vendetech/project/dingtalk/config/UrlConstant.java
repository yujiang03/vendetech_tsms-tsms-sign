package com.vendetech.project.dingtalk.config;

/**
 * 
 * 
 * @author vendetech
 */
public class UrlConstant {

	public static final String URL_DINGTALK_OAPI = "https://oapi.dingtalk.com";
	/**
	 * 钉钉网关gettoken地址
	 */
	public static final String URL_GET_TOKEN = "https://oapi.dingtalk.com/gettoken";

	public static final String URL_SEND_MSG = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";

	/**
	 * 钉钉网关gettoken地址
	 */
	public static final String URL_GET_JSAPI_TICKET = "https://oapi.dingtalk.com/get_jsapi_ticket";

	/**
	 * 获取用户在企业内userId的接口URL
	 */
	public static final String URL_GET_USER_INFO = "https://oapi.dingtalk.com/user/getuserinfo";

	/**
	 * 获取用户姓名的接口url
	 */
	public static final String URL_USER_GET = "https://oapi.dingtalk.com/user/get";

	
	public static final String URL_SNS_USER_CODE_GET = URL_DINGTALK_OAPI + "/sns/getuserinfo_bycode";
	
	public static final String URL_PERSISTENT_CODE_GET = URL_DINGTALK_OAPI + "/sns/get_persistent_code";

	public static final String URL_SNS_TOKEN_GET = URL_DINGTALK_OAPI + "/sns/get_sns_token";

	public static final String URL_SNS_USER_INFO_GET = URL_DINGTALK_OAPI + "/sns/getuserinfo";

	public static final String URL_USER_INFO_BY_UNIONID_GET = URL_DINGTALK_OAPI + "/user/getUseridByUnionid";

}
