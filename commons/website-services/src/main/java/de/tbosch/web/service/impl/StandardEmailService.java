package de.tbosch.web.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Service;

import de.tbosch.web.service.EmailService;

/**
 * Standard Implementierung des {@link EmailService}.
 * 
 * @author Thomas Bosch (tbosch@gmx.de)
 */
@Service
public class StandardEmailService implements EmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StandardEmailService.class);

	@Autowired
	private JavaMailSender mailSender;

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
		try {
			MimeMailMessage mail = new MimeMailMessage(mailSender.createMimeMessage());
			mail.setSubject(subject);
			if (email != null) {
				mail.setReplyTo(email);
			}
			mail.setText("Nachricht aus dem Kontaktformular meiner Webseite:\n\nVon " + name + " (" + email + ")\n\n"
					+ message);
			mail.setTo(env.getProperty("smtp.to"));
			mail.setFrom(env.getProperty("smtp.from"));
			mailSender.send(mail.getMimeMessage());
		} catch (Exception e) {
			LOGGER.error("Unable to send email", e);
			return false;
		}
		return true;
	}
}
