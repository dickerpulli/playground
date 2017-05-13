package de.tbosch.web.service.impl;

import java.io.IOException;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Service;

import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;

import de.tbosch.web.service.EmailService;

/**
 * SendGrid Implementierung des {@link EmailService}.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
@Service
public class SendGridEmailService implements EmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SendGridEmailService.class);

	@Autowired
	private Environment env;

	/**
	 * @see de.tbosch.web.service.EmailService#sendMeAnEmail(java.lang.String, String, java.lang.String,
	 *      java.lang.String)
	 */
	@Override
	public boolean sendMeAnEmail(String name, String subject, String email, String message) {
		LOGGER.info("Send mail from {} to me with sender email {} and subject {}", name, email, subject);
		LOGGER.debug("... and message: {}", message);
		Email from = new Email(env.getProperty("smtp.from"));
	    Email to = new Email(env.getProperty("smtp.to"));
	    String text = "Nachricht aus dem Kontaktformular meiner Webseite:\n\n"//
				+ "Von " + name + " (" + email + ")\n\n"//
				+ message;
	    Content content = new Content("text/plain", text);
	    Mail mail = new Mail(from, subject, to, content);
	    mail.setReplyTo(new Email(email));

	    SendGrid sg = new SendGrid(System.getenv("SENDGRID_API_KEY"));
	    Request request = new Request();
	    try {
	      request.method = Method.POST;
	      request.endpoint = "mail/send";
	      request.body = mail.build();
	      Response response = sg.api(request);
	      System.out.println(response.statusCode);
	      System.out.println(response.body);
	      System.out.println(response.headers);
	    } catch (IOException ex) {
	    	LOGGER.error("Unable to send email", ex);
			return false;
	    }
		return true;
	}
	
}
