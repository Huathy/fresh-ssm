package com.hx.utils;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.hx.utils.ParamsUtil;

/**
 * 邮件发送工具类
 * @author Huathy
 * @date 2020年4月5日
 */
@Component
public class SendMailUtil {
	private String sendEmail;	//发件人邮箱
	private String pwd;			//发件箱授权码
	private String host;		//邮箱主机。若是网易的则使用smtp.163.com,QQ:smtp.qq.com
	
	public void setSendEmail(String sendEmail) {
		this.sendEmail = sendEmail;
	}
	
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * @param sendEmail		邮件发送账号
	 * @param pwd			邮件SMTP授权码
	 * @param host			邮件主机
	 */
	public SendMailUtil(String sendEmail, String pwd, String host) {
		super();
		this.sendEmail = sendEmail;
		this.pwd = pwd;
		this.host = host;
	}
	public SendMailUtil() {
		super();
	}
	
	/**
	 * 邮件发送类
	 * @param receiveMail	接收方邮箱
	 * @param mainText		发送的正文
	 * @param subject		发送的主题
	 * @return
	 */
	public boolean sendEmail(String receiveMail,String mainText,String subject){
		try {
			if(ParamsUtil.checkNull(receiveMail,mainText,subject)){
				throw new Exception("参数不可为空，请检查参数：receiveMail,mainText,subject");
			}
			if(ParamsUtil.checkNull(sendEmail,pwd,host)){
				throw new Exception("请检查参数配置：sendEmail,pwd,host");
			}
			JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
			
			senderImpl.setHost(host); 		//邮箱主机。若是网易的则使用smtp.163.com,QQ:smtp.qq.com
			senderImpl.setDefaultEncoding("UTF-8"); 	//编码集
			
			//建立邮件的消息，我们需要发送的是html格式的邮件
			MimeMessage mimeMessage = senderImpl.createMimeMessage();
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			
			//设置收件人，寄件人，邮件主题
			messageHelper.setTo(receiveMail);
			messageHelper.setFrom(sendEmail);
			messageHelper.setSubject(subject);
			
			//设置邮件正文
			messageHelper.setText(mainText,true);
			
			//设置名称
			senderImpl.setUsername(sendEmail);	//发件箱
			senderImpl.setPassword(pwd);		//发件箱密码
			
			Properties prop = new Properties();
			prop.put("mail.smtp.auth", "true");		//让服务器去认证用户名何密码
			prop.put("mail.smtp.timeout", "2500");	//连接超时时间
			
			senderImpl.setJavaMailProperties(prop);
			senderImpl.send(mimeMessage);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
