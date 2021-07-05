package com.vendetech.project.dingtalk.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
import com.vendetech.project.dingtalk.config.Constant;
import com.vendetech.project.dingtalk.config.UrlConstant;

public class DdSentMsgThead extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(DdSentMsgThead.class);

	private String userId;
	private String msgContent;

	public DdSentMsgThead(String userId, String msgContent) {
		this.userId = userId;
		this.msgContent = msgContent;
	}

	@Override
	public void run() {
		logger.info("DdSentMsgThead start");
		try {
			sendDDMessage(Constant.AGENT_ID, userId, msgContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description:发送消息
	 * @Method:sendDDMessage
	 * @param agentId    微应用的ID
	 * @param userId     接收者的用户userid列表
	 * @param msgcontent 消息内容
	 */
	public static String sendDDMessage(Long agentId, String userId, String msgContent) { // JSONObject
		DingTalkClient client = new DefaultDingTalkClient(UrlConstant.URL_SEND_MSG);
		try {
			OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
			request.setUseridList(userId);
			request.setAgentId(agentId);
			request.setToAllUser(false);

			OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
			msg.setMsgtype("text"); // 消息类型
			msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
			msg.getText().setContent(msgContent);
			request.setMsg(msg);
//			msg.setOa(new OapiMessageCorpconversationAsyncsendV2Request.OA());
//			msg.getOa().setHead(new OapiMessageCorpconversationAsyncsendV2Request.Head());
//			msg.getOa().getHead().setText("head");
//			msg.getOa().setBody(new OapiMessageCorpconversationAsyncsendV2Request.Body());
//			msg.getOa().getBody().setContent("xxx");
//			msg.setMsgtype("oa");
//			request.setMsg(msg);

			String token = AccessTokenUtil.getToken();
			OapiMessageCorpconversationAsyncsendV2Response response = client.execute(request, token);
			if (null != response && "0".equals(response.getErrorCode())) {
//				response.getErrorCode();
				return response.getMessage();
//			JSONObject json = JSONObject.parseObject(rsp.getResult());
//			if (json != null) {
//				return json;
			} else {
				return response.getMessage();
			}
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return null;
	}

}
