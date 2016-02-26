package de.tbosch.web.test;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import de.tbosch.web.service.EmailService;

@Configuration
@PropertySource("classpath:application-test.properties")
@ComponentScan(basePackageClasses = { EmailService.class })
public class TestConfiguration {

	@Autowired
	private Environment env;

	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(env.getProperty("smtp.host"));
		mailSender.setPort(env.getProperty("smtp.port", Integer.class));
		mailSender.setDefaultEncoding("UTF-8");
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.auth", true);
		if (env.getProperty("smtp.ssl", Boolean.class)) {
			javaMailProperties.put("mail.transport.protocol", "smtps");
		}
		if (env.getProperty("smtp.starttls", Boolean.class)) {
			javaMailProperties.put("mail.smtp.starttls.enable", true);
		}
		mailSender.setJavaMailProperties(javaMailProperties);
		mailSender.setUsername(env.getProperty("smtp.username"));
		mailSender.setPassword(env.getProperty("smtp.password"));
		return mailSender;
	}

	@Bean
	public static PropertyPlaceholderConfigurer placeholderConfigurer() {
		return new PropertyPlaceholderConfigurer();
	}

}
