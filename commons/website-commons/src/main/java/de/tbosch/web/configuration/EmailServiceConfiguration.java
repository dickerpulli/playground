package de.tbosch.web.configuration;

import java.util.Properties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import de.tbosch.web.service.EmailService;
import de.tbosch.web.service.impl.SendGridEmailService;
import de.tbosch.web.service.impl.StandardEmailService;

@Configuration
@EnableConfigurationProperties({ EmailProperties.class })
public class EmailServiceConfiguration {

	@Bean
	@Primary
	public EmailService standardMailService() {
		return new StandardEmailService();
	}
	
	@Bean
	public EmailService sendGridEmailService() {
		return new SendGridEmailService();
	}

	@Bean
	public EmailProperties emailProperties() {
		return new EmailProperties();
	}

	@Bean
	public JavaMailSender javaMailSender(EmailProperties emailProperties) {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(emailProperties.getHost());
		mailSender.setPort(emailProperties.getPort());
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.auth", true);
		if (emailProperties.isSsl()) {
			javaMailProperties.put("mail.transport.protocol", "smtps");
		}
		if (emailProperties.isStarttls()) {
			javaMailProperties.put("mail.smtp.starttls.enable", true);
		}
		mailSender.setJavaMailProperties(javaMailProperties);
		mailSender.setUsername(emailProperties.getUsername());
		mailSender.setPassword(emailProperties.getPassword());
		return mailSender;
	}

}
