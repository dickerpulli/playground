package de.tbosch.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.ArrayFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import de.tbosch.batch.item.LogItemProcessor;
import de.tbosch.batch.item.SysoutItemWriter;

@Configuration
public class ExampleBatchConfig {

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Bean
	public Job job(Step step) {
		return jobs.get("example-job").start(step).build();
	}

	@Bean
	protected Step step() {
		return steps.get("step").<String[], String[]> chunk(10).reader(itemReader()).processor(itemProcessor())
				.writer(itemWriter()).build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<String[]> itemReader() {
		FlatFileItemReader<String[]> itemReader = new FlatFileItemReader<String[]>();
		itemReader.setResource(new ClassPathResource("in.txt"));
		DefaultLineMapper<String[]> lineMapper = new DefaultLineMapper<String[]>();
		lineMapper.setFieldSetMapper(new ArrayFieldSetMapper());
		lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
		lineMapper.afterPropertiesSet();
		itemReader.setLineMapper(lineMapper);
		return itemReader;
	}

	@Bean
	@StepScope
	public LogItemProcessor itemProcessor() {
		return new LogItemProcessor();
	}

	@Bean
	@StepScope
	public SysoutItemWriter itemWriter() {
		return new SysoutItemWriter();
	}

}
