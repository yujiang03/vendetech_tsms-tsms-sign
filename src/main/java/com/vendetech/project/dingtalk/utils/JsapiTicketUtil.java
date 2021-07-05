package com.vendetech.project.dingtalk.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceGetcolumnvalRequest;
import com.dingtalk.api.request.OapiGetJsapiTicketRequest;
import com.dingtalk.api.response.OapiAttendanceGetcolumnvalResponse;
import com.dingtalk.api.response.OapiAttendanceGetcolumnvalResponse.ColumnValForTopVo;
import com.dingtalk.api.response.OapiGetJsapiTicketResponse;
import com.taobao.api.ApiException;
import com.taobao.api.internal.util.StringUtils;
import com.vendetech.project.dingtalk.ali.AuthHelper;
import com.vendetech.project.dingtalk.config.UrlConstant;

/**
 * 获取access_token工具类
 */
public class JsapiTicketUtil {
	private static final Logger bizLogger = LoggerFactory.getLogger(JsapiTicketUtil.class);

	public static String getTicket(String accessToken) throws RuntimeException {
		try {
			DefaultDingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_GET_JSAPI_TICKET);
			OapiGetJsapiTicketRequest request = new OapiGetJsapiTicketRequest();
			request.putOtherTextParam("access_token", accessToken);
			request.setHttpMethod("GET");
			OapiGetJsapiTicketResponse response = client.execute(request);
			String jsapiTicekt = response.getTicket();
			return jsapiTicekt;
		} catch (ApiException e) {
			bizLogger.error("getAccessToken failed", e);
			throw new RuntimeException();
		}

	}

	public static void main(String[] args) throws ApiException {
		String accessToken = AccessTokenUtil.getToken();
//		String jsapiTicket = JsapiTicketUtil.getTicket(accessToken);
//		System.out.println(jsapiTicket);
		
//		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getattcolumns");
//		OapiAttendanceGetattcolumnsRequest req = new OapiAttendanceGetattcolumnsRequest();
//		OapiAttendanceGetattcolumnsResponse rsp = client.execute(req, accessToken);
//		System.out.println(rsp.getBody());
		
		
		DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/attendance/getcolumnval");
		OapiAttendanceGetcolumnvalRequest request = new OapiAttendanceGetcolumnvalRequest();
		request.setUserid("160635502121412782");
		request.setColumnIdList("65148833,65148810,65148829");	// 考勤结果, 应出勤天数,加班-审批单统计 
		request.setFromDate(StringUtils.parseDateTime("2019-11-01 00:00:00"));
		request.setToDate(StringUtils.parseDateTime("2019-11-11 00:00:00"));
		OapiAttendanceGetcolumnvalResponse execute = client.execute(request, AuthHelper.getAccessToken());
		System.out.println(execute.getBody());
		List<ColumnValForTopVo> results = execute.getResult().getColumnVals();
		System.out.println(results.get(0).getColumnVals().get(4).getValue());
		System.out.println(results.get(1).getColumnVals().get(4).getValue());
		System.out.println(results.get(2).getColumnVals().get(4).getValue());
		System.out.println(results.get(0).getColumnVals().get(5).getValue());
		System.out.println(results.get(1).getColumnVals().get(5).getValue());
		System.out.println(results.get(2).getColumnVals().get(5).getValue());
		System.out.println(results.get(0).getColumnVals().get(6).getValue());
		System.out.println(results.get(1).getColumnVals().get(6).getValue());
		System.out.println(results.get(2).getColumnVals().get(6).getValue());
	}
}
