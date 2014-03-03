package de.tbosch.batch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import de.tbosch.batch.item.LogItemProcessor;
import de.tbosch.batch.item.SysoutItemWriter;
import de.tbosch.batch.model.Person;

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
	protected Step step() throws Exception {
		return steps.get("step").<Person, Person> chunk(10).reader(itemReader()).processor(itemProcessor())
				.writer(itemWriter()).build();
	}

	@Bean
	@StepScope
	public FlatFileItemReader<Person> itemReader() throws Exception {
		FlatFileItemReader<Person> itemReader = new FlatFileItemReader<Person>();
		itemReader.setResource(new ClassPathResource("in.txt"));
		DefaultLineMapper<Person> lineMapper = new DefaultLineMapper<Person>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setNames(new String[] { "lastname", "firstname", "age" });
		lineMapper.setLineTokenizer(lineTokenizer);
		BeanWrapperFieldSetMapper<Person> fieldSetMapper = new BeanWrapperFieldSetMapper<Person>();
		fieldSetMapper.setTargetType(Person.class);
		fieldSetMapper.afterPropertiesSet();
		lineMapper.setFieldSetMapper(fieldSetMapper);
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
