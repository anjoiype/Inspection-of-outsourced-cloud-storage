package EIA;
import java.util.Properties;
 
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
 
public class SendMailTLS {
	
 
	public void sendMail() {
 
		final String username = "anjoiype@gmail.com";
		final String password = "xyemqxiswgmxlckb";
 
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
 
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("anjoiype@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("anjoiype@gmail.com"));
			message.setSubject("<URGENT> Data corrupted");
			message.setText("Dear Data owner,"
				+ "\n\n Your data has got corrupted in cloud storage.. Please take necessary "
				+ "actions");
 
			Transport.send(message);
 
			System.out.println("Done");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}