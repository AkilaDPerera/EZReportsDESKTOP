package main.logic;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailClient {
	private String sender = "ezreportz@gmail.com"; //FIXED
	private Properties props;
	private Session session;
	
	public EmailClient(){
		this.props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		
		this.session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(EmailClient.this.sender, "ezreportz12");
			}
		});
	}
	
	public boolean sendEmail(String to, String message, String subject){
		Message msg = new MimeMessage(this.session);
		
		try {
			msg.setFrom(new InternetAddress(this.sender));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			msg.setSubject(subject);
			msg.setText(message);
			Transport.send(msg);
			return true;
		} catch (Exception e) {
			return false;
			//e.printStackTrace();
		}
	}
}
