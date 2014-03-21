package de.tbosch.web.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ApplicationConfiguration {

	@Profile("default")
	@Bean
	public String bean1() {
		System.out.println("profile:default");
		return "";
	}

	@Profile("test")
	@Bean
	public String bean2() {
		System.out.println("profile:test");
		return "";
	}

}
