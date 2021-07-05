package com.vendetech.project.dingtalk.config;

/**
 * 企业应用接入时的常量定义
 */
public class Env {
	/**
	 * DING API地址
	 */
	
	public static final String OAPI_HOST = "https://oapi.dingtalk.com";
	/**
	 * 企业应用后台地址，用户管理后台免登使用
	 */
	public static final String OA_BACKGROUND_URL = "";

	/***
	 *  企业通讯回调加密Token，注册事件回调接口时需要传递给钉钉服务器
	 */
	public static final String TOKEN = "135790";
	public static final String ENCODING_AES_KEY = "2019vendetech12yanfabu16DDceshi";

}
