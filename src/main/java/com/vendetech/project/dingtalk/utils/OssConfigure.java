package com.vendetech.project.dingtalk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.springframework.util.ResourceUtils;

/**
 * 
 * 
 * @author vendetech
 */
public class OssConfigure {

	private String endpoint;
	private String accessKeyId;
	private String accessKeySecret;
	private String bucketName;
	private String path;
	private String mode;
	private String service;
	private String taskThreadNum;

	/**
	 * 通过配置文件.properties文件获取，这几项内容。
	 * 
	 * @param storageConfName
	 * @throws IOException
	 */
	public OssConfigure() {

		Properties prop = new Properties();
		try {
			File fi = ResourceUtils.getFile("classpath:ali-conf.properties");
			prop.load(new InputStreamReader(new FileInputStream(fi)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		endpoint = prop.getProperty("endpoint").trim();
		accessKeyId = prop.getProperty("accessKeyId").trim();
		accessKeySecret = prop.getProperty("accessKeySecret").trim();
		bucketName = prop.getProperty("bucketName").trim();
		path = prop.getProperty("path").trim();
		service = prop.getProperty("service").trim();
		mode = prop.getProperty("mode").trim();
		taskThreadNum = prop.getProperty("taskThreadNum").trim();

	}

	public OssConfigure(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String path,
			String service, String mode, String taskThreadNum) {

		this.endpoint = endpoint;
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
		this.bucketName = bucketName;
		this.path = path;
		this.service = service;
		this.mode = mode;
		this.taskThreadNum = taskThreadNum;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	/**
	 * @return the service
	 */
	public String getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(String service) {
		this.service = service;
	}

	/**
	 * @return the taskThreadNum
	 */
	public String getTaskThreadNum() {
		return taskThreadNum;
	}

	/**
	 * @param taskThreadNum the taskThreadNum to set
	 */
	public void setTaskThreadNum(String taskThreadNum) {
		this.taskThreadNum = taskThreadNum;
	}

}
