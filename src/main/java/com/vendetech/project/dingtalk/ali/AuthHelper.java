package com.vendetech.project.dingtalk.ali;

import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.vendetech.project.dingtalk.config.Constant;
import com.vendetech.project.dingtalk.utils.AccessTokenUtil;
import com.vendetech.project.dingtalk.utils.AliFileUtils;
import com.vendetech.project.dingtalk.utils.JsapiTicketUtil;
import com.vendetech.project.system.domain.SysUser;

/**
 * AccessToken和JsapiTicket的获取封装
 */
public class AuthHelper {

	// 调整到1小时50分钟
	public static final long CACHE_TIME = 1000 * 60 * 55 * 2;

	/*
	 * 在此方法中，为了避免频繁获取access_token， 在距离上一次获取access_token时间在两个小时之内的情况，
	 * 将直接从持久化存储中读取access_token
	 *
	 * 因为access_token和jsapi_ticket的过期时间都是7200秒 所以在获取access_token的同时也去获取了jsapi_ticket
	 * 注：jsapi_ticket是在前端页面JSAPI做权限验证配置的时候需要使用的 具体信息请查看开发者文档--权限验证配置
	 */
	public static String getAccessToken() throws ApiException {
		long curTime = System.currentTimeMillis();
		JSONObject accessTokenValue = (JSONObject) AliFileUtils.getValue("accesstoken", Constant.APP_KEY);
		String accessToken = "";
		JSONObject jsontemp = new JSONObject();
		if (null == accessTokenValue || curTime - accessTokenValue.getLong("begin_time") >= CACHE_TIME) {
			try {
				accessToken = AccessTokenUtil.getToken();
				// save accessToken
				JSONObject jsonAccess = new JSONObject();
				jsontemp.clear();
				jsontemp.put("access_token", accessToken);
				jsontemp.put("begin_time", curTime);
				jsonAccess.put(Constant.APP_KEY, jsontemp);
				// 真实项目中最好保存到数据库中
				AliFileUtils.write2File(jsonAccess, "accesstoken");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			return accessTokenValue.getString("access_token");
		}

		return accessToken;
	}

	/**
	 * 获取JSTicket, 用于js的签名计算
	 * 正常的情况下，jsapi_ticket的有效期为7200秒，所以开发者需要在某个地方设计一个定时器，定期去更新jsapi_ticket
	 */
	public static String getJsapiTicket(String accessToken) throws ApiException {
		JSONObject jsTicketValue = (JSONObject) AliFileUtils.getValue("jsticket", Constant.APP_KEY);
		long curTime = System.currentTimeMillis();
		String jsapiTicket = "";

		if (jsTicketValue == null || curTime - jsTicketValue.getLong("begin_time") >= CACHE_TIME) {
			try {
				jsapiTicket = JsapiTicketUtil.getTicket(accessToken);
				JSONObject jsonTicket = new JSONObject();
				JSONObject jsontemp = new JSONObject();
				jsontemp.clear();
				jsontemp.put("ticket", jsapiTicket);
				jsontemp.put("begin_time", curTime);
				jsonTicket.put(Constant.APP_KEY, jsontemp);
				AliFileUtils.write2File(jsonTicket, "jsticket");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsapiTicket;
		} else {
			return jsTicketValue.getString("ticket");
		}
	}

	/**
	 * Dingtalk扫码登录，通过扫码返回的code获得钉钉userid
	 *  
	 */
	/*
	public static String getUseridByScan(String tmpCode) throws ApiException {
		String userid = null;
		try {
			String accessToken = getAccessToken();
			Map<String, String> persistentMap = AccessTokenUtil.getPersistentCode(accessToken, tmpCode);
			String snsToken = AccessTokenUtil.getSnsToken(
					persistentMap.get("persistentCode"),
					persistentMap.get("openid"), 
					accessToken);
			String unionid = AccessTokenUtil.getSnsUnionid(snsToken);
			userid = AccessTokenUtil.getUseridByUnionid(accessToken, unionid);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ApiException(ex);
		}
		return userid;
	}
	*/
	/**
	 * Dingtalk扫码登录，通过扫码返回的code获得钉钉userid
	 *  
	 */
	public static String getSnsUserByCode(String tmpCode) throws ApiException {
		String userid = null;
		try {
			String accessToken = getAccessToken();
			String unionid = AccessTokenUtil.getSnsUserByCode(tmpCode);
			System.out.println(unionid);
			userid = AccessTokenUtil.getUseridByUnionid(accessToken, unionid);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ApiException(ex);
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
	public static SysUser getEmpUser(String userid) throws ApiException {
		SysUser user = null;
		try {
			user = AccessTokenUtil.getEmpUser(getAccessToken(), userid);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ApiException(ex);
		}
		return user;
	}

	public static String getUserInfo(String tmpAuthCode) throws ApiException {
		String username = null;

		String userid = AuthHelper.getSnsUserByCode(tmpAuthCode);
		SysUser duser = AuthHelper.getEmpUser(userid);
		username = duser.getUserName(); // 员工号

		return username;
	}

	public static String getAppUserInfo(String code) throws ApiException {
		String username = null;

		String userid = AccessTokenUtil.getUserInfo(getAccessToken(), code);
		SysUser duser = AuthHelper.getEmpUser(userid);
		username = duser.getUserName(); // 员工号

		return username;
	}

//	public static String sign(String ticket, String nonceStr, long timeStamp, String url) throws ApiException {
//		try {
//			return DingTalkJsApiSingnature.getJsApiSingnature(url, nonceStr, timeStamp, ticket);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			throw new ApiException(ex);
//		}
//	}
//
//	/**
//	 * 计算当前请求的jsapi的签名数据<br/>
//	 * <p>
//	 * 如果签名数据是通过ajax异步请求的话，签名计算中的url必须是给用户展示页面的url
//	 *
//	 * @param request
//	 * @return
//	 */
//	public static String getConfig(HttpServletRequest request) throws Exception {
//		String urlString = request.getRequestURL().toString();
//		String queryString = request.getQueryString();
//		urlString = urlString.replaceAll(":8081", "");
//		// urlString = "http://wshh.vaiwan.com/service.jsp";
//		String queryStringEncode = null;
//		String url;
//		if (queryString != null) {
//			queryStringEncode = URLDecoder.decode(queryString, StandardCharsets.UTF_8.name());
//			url = urlString + "?" + queryStringEncode;
//		} else {
//			url = urlString;
//		}
//
//		String nonceStr = "abcdefg";
//		long timeStamp = System.currentTimeMillis() / 1000;
//		String signedUrl = url;
//		String accessToken = null;
//		String ticket = null;
//		String signature = null;
//		String agentid = null;
//
//		try {
//			accessToken = AuthHelper.getAccessToken();
//
//			ticket = AuthHelper.getJsapiTicket(accessToken);
//			signature = AuthHelper.sign(ticket, nonceStr, timeStamp, signedUrl);
//			agentid = "";
//
//		} catch (ApiException e) {
//			e.printStackTrace();
//		}
//		String configValue = "{jsticket:'" + ticket + "',signature:'" + signature + "',nonceStr:'" + nonceStr
//				+ "',timeStamp:'" + timeStamp + "',corpId:'" + Constant.CORP_ID + "',agentid:'" + agentid + "'}";
//		return configValue;
//	}

	public static void main(String[] args) throws ApiException {
		String accessToken = AccessTokenUtil.getToken();
		System.out.println(accessToken);
		String tmpCode = "";
		String code = "afbb117ec51e33c2a8dafb35a340fd22";
		
//		String userid1 = getSnsUserByCode(tmpCode);
//		System.out.println(userid1);
//		EmpUser user1 = getEmpUser(userid1);
//		System.out.println(user1.getEmpNum());
//		System.out.println(user1.getEmpName());
//		String username = getUserInfo(tmpCode);
//		System.out.println(username);
		String empNumber = getAppUserInfo(code);
		System.out.println(empNumber);
	}

}
