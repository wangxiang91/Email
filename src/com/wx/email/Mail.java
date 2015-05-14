package com.wx.email;

import java.util.Date;
import java.util.Properties;
import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


/**发送邮件
 * Q
 * 1.登录一个邮箱
 * 2.写邮件加附件
 * 3.发给谁
 * A
 * 1.找来一个可以使用的邮箱账号和密码(有可能需要到邮件服务商配置一下)
 * 2.写邮件ing添加标题,内容,附件
 * 3.一堆合法的收件人邮件地址
 * 发送!!!
 * @author WangXiang
 * 2015年4月28日下午5:16:26
 */
public class Mail extends javax.mail.Authenticator {
	/**用于发送邮件的登录账号也是发送邮件的fromUser*/
	private String user;
	/**用于发送邮件的登录密码*/
	private String pwd;
	/**收件邮箱列表*/
	private String[] toUser;
	/**STMP端口*/
	private String port;
	/**default socket factory port*/
	private String socketProt;
	/**发送邮件服务器*/
	private String host;
	/**邮件默认主题*/
	private String subject;
	/**邮件默认正文*/
	private String body;
	/**是否使用SMTP验证,默认开启*/
	private boolean auth=true;
	/**debug模式,默认关闭*/
	private boolean debugable=false;

	private Multipart multipart;

	public Mail() {
		multipart = new MimeMultipart();
		// There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.
		MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
		mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
		mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
		mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
		mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
		mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
		CommandMap.setDefaultCommandMap(mc);
	}

	/**发送邮件
	 * @return 成功返回true,否则返回false
	 * @throws Exception
	 */
	public boolean send() throws Exception {
		Properties props = _setProperties();

		if(!user.equals("") && !pwd.equals("") && toUser.length > 0  && !subject.equals("") && !body.equals("")) {
			Session session = Session.getInstance(props, this);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(user));
			InternetAddress[] addressTo = new InternetAddress[toUser.length];
			for (int i = 0; i < toUser.length; i++) {
				addressTo[i] = new InternetAddress(toUser[i]);
			}
			msg.setRecipients(MimeMessage.RecipientType.TO, addressTo);
			msg.setSubject(subject);
			msg.setSentDate(new Date());
			// setup message body
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(body);
			multipart.addBodyPart(messageBodyPart);
			// Put parts in message
			msg.setContent(multipart);
			// send email
			Transport.send(msg);
			return true;
		} else {
			return false;
		}
	}
	/**给邮件添加附件,重复调用添加多个附件
	 * @param filename 文件名
	 * @throws Exception
	 */
	public void addAttachment(String filename) throws Exception {
	    BodyPart messageBodyPart = new MimeBodyPart();
	    DataSource source = new FileDataSource(filename);
	    messageBodyPart.setDataHandler(new DataHandler(source));
	    messageBodyPart.setFileName(filename);
	    multipart.addBodyPart(messageBodyPart);
	}

	@Override
	public PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, pwd);
	}
  
	//加载默认设置
	private Properties _setProperties() {
	    Properties props = new Properties();
	
	    props.put("mail.smtp.host", host);
	
	    if(debugable) {
	      props.put("mail.debug", "true");
	    }
	
	    if(auth) {
	      props.put("mail.smtp.auth", "true");
	    }
	
	    props.put("mail.smtp.port", port);
	    props.put("mail.smtp.socketFactory.port", socketProt);
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.smtp.socketFactory.fallback", "false");
	
	    return props;
	}
	//get set
	public String getBody() {
		return body;
    }

	public Mail setBody(String _body) {
		this.body = _body;
		return this;
    }

	public String getUser() {
		return user;
	}

	public Mail setUser(String user) {
		this.user = user;
		return this;
	}

	public String getPwd() {
		return pwd;
	}

	public Mail setPwd(String pwd) {
		this.pwd = pwd;
		return this;
	}

	public String[] getToUser() {
		return toUser;
	}

	public Mail setToUser(String[] toUser) {
		this.toUser = toUser;
		return this;
	}

	public String getPort() {
		return port;
	}

	public Mail setPort(String port) {
		this.port = port;
		return this;
	}

	public String getSocketPort() {
		return socketProt;
	}

	public Mail setSocketPort(String sPort) {
		this.socketProt = sPort;
		return this;
	}

	public String getHost() {
		return host;
	}

	public Mail setHost(String host) {
		this.host = host;
		return this;
	}

	public String getSubject() {
		return subject;
	}

	public Mail setSubject(String subject) {
		this.subject = subject;
		return this;
	}

	public boolean isAuth() {
		return auth;
	}

	public Mail setAuth(boolean auth) {
		this.auth = auth;
		return this;
	}

	public boolean isDebugable() {
		return debugable;
	}

	public Mail setDebugable(boolean debugable) {
		this.debugable = debugable;
		return this;
	}

	public Multipart getMultipart() {
		return multipart;
	}

	public Mail setMultipart(Multipart multipart) {
		this.multipart = multipart;
		return this;
	}
}
