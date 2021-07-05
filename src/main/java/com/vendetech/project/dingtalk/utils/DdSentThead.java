package com.vendetech.project.dingtalk.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import com.vendetech.project.dingtalk.config.Constant;

public class DdSentThead extends Thread {

	private String url;
	private String param;

	public DdSentThead(String url, String param) {
		this.url = url;
		this.param = param;
	}

	@Override
	public void run() {
		try {
			// httpPostWithJSON(url, param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void httpPostWithJson(String url, String json) throws Exception {
		// 将JSON进行UTF-8编码,以便传输中文
		String encoderJson = URLEncoder.encode(json, StandardCharsets.UTF_8.name());
        // DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader(HTTP.CONTENT_TYPE, Constant.APPLICATION_JSON);
		StringEntity se = new StringEntity(encoderJson);
		se.setContentType(Constant.CONTENT_TYPE_TEXT_JSON);
		se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, Constant.APPLICATION_JSON));
		httpPost.setEntity(se);
		httpClient.execute(httpPost);
	}

}
