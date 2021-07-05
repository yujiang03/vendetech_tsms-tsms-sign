/**  
 * Copyright © 2018pactera. All rights reserved.
 *
 * @Title: MailSentThread.java
 * @Prject: pactera_pmo
 * @Package: com.pactera.pmo.utils
 * @Description: TODO
 * @author: Administrator  
 * @date: 2018年6月6日 下午5:35:16
 * @version: V1.0  
 */
package com.vendetech.project.tsms.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: MailSentThread
 * @Description: TODO
 * @author: Administrator
 * @date: 2018年6月6日 下午5:35:16
 */
public class MailSentThread extends Thread {

	/**
	 * @Description: TODO(logger this class)
	 * 
	 */

	private static final Logger logger = LoggerFactory.getLogger(MailSentThread.class);

	private String to;
	private String cc;
	private String bcc;
	private String subject;
	private String html;

	public MailSentThread(String to, String subject, String html) {
		this.to = to;
		this.subject = subject;
		this.html = html;
	}

	public MailSentThread(String to, String cc, String bcc, String subject, String html) {
		this.to = to;
		this.cc = cc;
		this.bcc = bcc;
		this.subject = subject;
		this.html = html;
	}

	@Override
	public void run() {

		logger.info("MailSentThread start");
		try {
			MailUtils.sendEmail(to, subject, html);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
