package de.tbosch.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.tbosch.batch.config.BatchInfrastructureConfig;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "de.tbosch.batch.controller")
@Import({ BatchInfrastructureConfig.class })
public class Batch {

	public static void main(String[] args) {
		SpringApplication.run(Batch.class, args);
	}

}
