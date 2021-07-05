package com.vendetech.project.dingtalk.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiSnsGetPersistentCodeRequest;
import com.dingtalk.api.request.OapiSnsGetSnsTokenRequest;
import com.dingtalk.api.request.OapiSnsGetuserinfoBycodeRequest;
import com.dingtalk.api.request.OapiSnsGetuserinfoRequest;
import com.dingtalk.api.request.OapiUserGetRequest;
import com.dingtalk.api.request.OapiUserGetUseridByUnionidRequest;
import com.dingtalk.api.request.OapiUserGetuserinfoRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiSnsGetPersistentCodeResponse;
import com.dingtalk.api.response.OapiSnsGetSnsTokenResponse;
import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse;
import com.dingtalk.api.response.OapiSnsGetuserinfoBycodeResponse.UserInfo;
import com.dingtalk.api.response.OapiSnsGetuserinfoResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.dingtalk.api.response.OapiUserGetUseridByUnionidResponse;
import com.dingtalk.api.response.OapiUserGetuserinfoResponse;
import com.taobao.api.ApiException;
import com.vendetech.project.dingtalk.config.Constant;
import com.vendetech.project.dingtalk.config.UrlConstant;
import com.vendetech.project.system.domain.SysUser;

/**
 * 获取access_token工具类
 */
public class AccessTokenUtil {
	private static final Logger bizLogger = LoggerFactory.getLogger(AccessTokenUtil.class);

	/**
	 * -获得Access Token
	 */
	public static String getToken() throws RuntimeException {
		String accessToken = null;
		try {
			DefaultDingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_GET_TOKEN);
			OapiGettokenRequest request = new OapiGettokenRequest();
			request.setAppkey(Constant.APP_KEY);
			request.setAppsecret(Constant.APP_SECRET);
			request.setHttpMethod("GET");

			OapiGettokenResponse response = client.execute(request);
			accessToken = response.getAccessToken();
		} catch (ApiException e) {
			bizLogger.error("getAccessToken failed", e);
			throw new RuntimeException();
		}
		return accessToken;
	}

	/**
	 * -通过临时授权码code获取用户的信息
	 */
	public static String getSnsUserByCode(String tmpCode) throws RuntimeException {
		String unionid = null;
		try {
			DefaultDingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_SNS_USER_CODE_GET);
			OapiSnsGetuserinfoBycodeRequest request = new OapiSnsGetuserinfoBycodeRequest();
			request.setTmpAuthCode(tmpCode);
//			request.setHttpMethod("POST");

			OapiSnsGetuserinfoBycodeResponse response = client.execute(request, Constant.APP_ID, Constant.APP_SCAN_SECRET);
			if (null != response && "0".equals(response.getErrorCode())) {
				UserInfo user = response.getUserInfo();
				if (null != user) {
					unionid = user.getUnionid();
				}
			}
		} catch (ApiException e) {
			e.printStackTrace();
			bizLogger.error("getSnsUserByCode failed", e);
			throw new RuntimeException();
		}
		return unionid;
	}

	
	
	
	/**
	 * -获取用户授权的持久授权码
	 * 
	 */
	public static Map<String, String> getPersistentCode(String accessToken, String code) {
		Map<String, String> persistentMap = new HashMap<String, String>();
		try {
			DingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_PERSISTENT_CODE_GET);
			OapiSnsGetPersistentCodeRequest request = new OapiSnsGetPersistentCodeRequest();
			request.setTmpAuthCode(code);
			request.setHttpMethod("POST");

			OapiSnsGetPersistentCodeResponse response = client.execute(request, accessToken);
			String persistentCode = response.getPersistentCode();
			String openid = response.getOpenid();
			persistentMap.put("persistentCode", persistentCode);
			persistentMap.put("openid", openid);
		} catch (ApiException e) {
			bizLogger.error("getPersistentCode failed", e);
			throw new RuntimeException();
		}
		return persistentMap;
	}

	/**
	 * -获取用户授权的SNS Token
	 * 
	 */
	public static String getSnsToken(String openId, String persistentCode, String accessToken) {
		String snsToken = null;
		try {
			DingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_SNS_TOKEN_GET);
			OapiSnsGetSnsTokenRequest request = new OapiSnsGetSnsTokenRequest();
			request.setOpenid(openId);
			request.setPersistentCode(persistentCode);
			request.setHttpMethod("POST");

			OapiSnsGetSnsTokenResponse response = client.execute(request, accessToken);
			snsToken = response.getSnsToken();
		} catch (ApiException e) {
			bizLogger.error("getSnsToken failed", e);
			throw new RuntimeException();
		}
		return snsToken;
	}

	/**
	 * -获取用户授权的个人信息
	 * 
	 * @param snsToken
	 * @return
	 */
	public static String getSnsUnionid(String snsToken) {
		String unionid = null;
		try {
			DingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_SNS_USER_INFO_GET);
			OapiSnsGetuserinfoRequest request = new OapiSnsGetuserinfoRequest();
			request.setSnsToken(snsToken);
			request.setHttpMethod("GET");

			OapiSnsGetuserinfoResponse response = client.execute(request);
			unionid = response.getUserInfo().getUnionid();
		} catch (ApiException e) {
			bizLogger.error("getSnsUnionid failed", e);
			throw new RuntimeException();
		}
		return unionid;
	}

	/**
	 * -根据Unionid获取Userid
	 * 
	 */
	public static String getUseridByUnionid(String accessToken, String unionid) {
		String userid = null;
		try {
			DingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_USER_INFO_BY_UNIONID_GET);
			OapiUserGetUseridByUnionidRequest request = new OapiUserGetUseridByUnionidRequest();
			request.setUnionid(unionid);
			request.setHttpMethod("GET");

			OapiUserGetUseridByUnionidResponse response = client.execute(request, accessToken);
			if("ok".equals(response.getErrmsg())) {
				userid = response.getUserid();
			}
		} catch (ApiException e) {
			bizLogger.error("getUseridByUnionid failed", e);
			throw new RuntimeException();
		}
		return userid;
	}

	/**
	 * 获取用户授权的个人信息
	 * 
	 * @param accessToken
	 * @param uid
	 * @return
	 */
	public static SysUser getEmpUser(String accessToken, String userid) {
		SysUser user = null;
		try {
			DingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_USER_GET); // .SDINGTALKSERVICE + "/user/get");
			OapiUserGetRequest request = new OapiUserGetRequest();
			request.setUserid(userid);
			request.setHttpMethod("GET");
			
			OapiUserGetResponse response = client.execute(request, accessToken);
			user = new SysUser();
			if (null != response && "0".equals(response.getErrorCode())) {
				user.setUserName(response.getJobnumber());
				user.setNickName(response.getName());
				user.setStatus(response.getActive() ? "0" : "1");
			}
		} catch (ApiException e) {
			bizLogger.error("getEmpUser failed", e);
			throw new RuntimeException();
		}
		return user;
	}

	/**
	 * 获取用户授权的个人信息
	 * 
	 * @param accessToken
	 * @param uid
	 * @return
	 */
	public static String getUserInfo(String accessToken, String requestAuthCode) {
		String userid = null;
		try {
			DingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_GET_USER_INFO);
			OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
			request.setCode(requestAuthCode);
			request.setHttpMethod("GET");
			OapiUserGetuserinfoResponse response = client.execute(request, accessToken);
			userid = response.getUserid();
		} catch (ApiException e) {
			e.printStackTrace();
			return null;
		}
		return userid;
	}
	
	public static void main(String[] args) throws ApiException {
		String accessToken = AccessTokenUtil.getToken();
		System.out.println(accessToken);
	}
}
