package com.vendetech.project.tsms.utils;

import org.apache.commons.mail.HtmlEmail;

public class MailUtils {
	
	// 发送邮箱-通用
	public static void sendEmail(String emailAddress, String subject, String htmlText) {
		// 不要使用SimpleEmail,会出现乱码问题
		HtmlEmail email = new HtmlEmail();
		try {
			// 这里是SMTP发送服务器的名字：，普通qq号只能是smtp.qq.com ；
			email.setHostName(MailConstant.Send_Host_Name);
			// 设置需要鉴权端口
			email.setSmtpPort(MailConstant.Send_Smtp_Port);
			// 开启 SSL 加密
			email.setSSLOnConnect(true);
			// 字符编码集的设置
			email.setCharset(MailConstant.Send_Charset);
			// 收件人的邮箱
			email.addTo(emailAddress);
			// 发送人的邮箱
			email.setFrom(MailConstant.Send_From);
			// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和得到的授权码
			email.setAuthentication(MailConstant.Send_Authentication_userName,
					MailConstant.Send_Authentication_password);
			email.setSubject(subject);
			// 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
			email.setMsg(htmlText);
			// 发送
			email.send();
			System.out.println("邮件发送成功!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("邮件发送失败!");
		}
	}

	// 发送重置邮箱验证码
	public static void sendResetEmail(String emailAddress, String code) {
		// 不要使用SimpleEmail,会出现乱码问题
		HtmlEmail email = new HtmlEmail();
		try {
			// 这里是SMTP发送服务器的名字：，普通qq号只能是smtp.qq.com ；
			email.setHostName(MailConstant.Send_Host_Name);
			// 设置需要鉴权端口
			email.setSmtpPort(MailConstant.Send_Smtp_Port);
			// 开启 SSL 加密
			email.setSSLOnConnect(true);
			// 字符编码集的设置
			email.setCharset(MailConstant.Send_Charset);
			// 收件人的邮箱
			email.addTo(emailAddress);
			// 发送人的邮箱
			email.setFrom(MailConstant.Send_From);
			// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和得到的授权码
			email.setAuthentication(MailConstant.Send_Authentication_userName,
					MailConstant.Send_Authentication_password);
			email.setSubject(MailConstant.Send_Subject);
			// 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
			email.setMsg(MailConstant.Send_Msg + code);
			// 发送
			email.send();
			System.out.println("邮件发送成功!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("邮件发送失败!");
		}
	}

	// 创建账号发送邮箱验证码
	public static void sendSaveEmail(String emailAddress, String code) {
		// 不要使用SimpleEmail,会出现乱码问题
		HtmlEmail email = new HtmlEmail();
		try {
			// 这里是SMTP发送服务器的名字：，普通qq号只能是smtp.qq.com ；
			email.setHostName(MailConstant.Send_Host_Name);
			// 设置需要鉴权端口
			email.setSmtpPort(MailConstant.Send_Smtp_Port);
			// 开启 SSL 加密
			email.setSSLOnConnect(true);
			// 字符编码集的设置
			email.setCharset(MailConstant.Send_Charset);
			// 收件人的邮箱
			email.addTo(emailAddress);
			// 发送人的邮箱
			email.setFrom(MailConstant.Send_From);
			// 如果需要认证信息的话，设置认证：用户名-密码。分别为发件人在邮件服务器上的注册名称和得到的授权码
			email.setAuthentication(MailConstant.Send_Authentication_userName,
					MailConstant.Send_Authentication_password);
			email.setSubject(MailConstant.Send_SAVE_Subject);
			// 要发送的信息，由于使用了HtmlEmail，可以在邮件内容中使用HTML标签
			email.setMsg(MailConstant.Send_Msg_Save + code + "请及时更换初始密码");
			// 发送
			email.send();
			System.out.println("邮件发送成功!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("邮件发送失败!");
		}
	}
}
