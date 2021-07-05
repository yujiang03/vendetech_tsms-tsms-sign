package com.vendetech.project.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.util.ResourceUtils;

/**
 * @ClassName: MailConfig
 * @Description: TODO
 * @author: Vende
 * @date: 2018年5月10日 上午11:52:51
 */
public class MailConfig {
	public static String host;
	public static Integer port;
	public static String userName;
	public static String passWord;
	public static String emailFrom;
	public static String timeout;
	public static String personal;
	public static Properties properties;
	static {
		init();
	}

	/**
	 * 初始化
	 */
	private static void init() {
		properties = new Properties();
		try {
			File fi = ResourceUtils.getFile("classpath:mail-config.properties");
			properties.load(new InputStreamReader(new FileInputStream(fi)));
			host = properties.getProperty("mailHost");
			port = Integer.parseInt(properties.getProperty("mailPort"));
			userName = properties.getProperty("mailUsername");
			passWord = properties.getProperty("mailPassword");
			emailFrom = properties.getProperty("mailFrom");
			timeout = properties.getProperty("mailTimeout");
			personal = properties.getProperty("mailFrom");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
