package de.tbosch.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.tbosch.batch.config.BatchInfrastructureConfig;
import de.tbosch.batch.config.ExampleBatchConfig;

@Configuration
@EnableAutoConfiguration
@EnableBatchProcessing(modular = true)
@ComponentScan(basePackages = "de.tbosch.batch.controller")
@Import({ BatchInfrastructureConfig.class, ExampleBatchConfig.class })
public class Batch {

	public static void main(String[] args) {
		SpringApplication.run(Batch.class, args);
	}

}
