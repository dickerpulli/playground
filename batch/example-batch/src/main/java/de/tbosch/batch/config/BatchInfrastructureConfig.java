package de.tbosch.batch.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class BatchInfrastructureConfig {

	@Bean
	public DataSource dataSource() {
		return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.HSQL)
				.addScript("classpath:org/springframework/batch/core/schema-hsqldb.sql").build();
		// BasicDataSource dataSource = new BasicDataSource();
		// dataSource.setDriverClassName(jdbcDriver.class.getName());
		// dataSource.setUrl("jdbc:hsqldb:mem:testdb");
		// dataSource.setUsername("sa");
		// dataSource.setPassword("");
		// return dataSource;
	}

}
