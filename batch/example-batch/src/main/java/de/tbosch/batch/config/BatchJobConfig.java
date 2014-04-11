package de.tbosch.batch.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import de.tbosch.batch.item.LogItemProcessor;
import de.tbosch.batch.item.RetryItemProcessor;
import de.tbosch.batch.item.SysoutItemWriter;
import de.tbosch.batch.model.Person;
import de.tbosch.batch.skip.AsyncLimitCheckingItemSkipPolicy;

@Configuration
public class BatchJobConfig {

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Bean
	public Job exampleJob() throws Exception {
		return jobs.get("example-job").start(step1()).build();
	}

	@Bean
	public Job retryJob() throws Exception {
		return jobs.get("retry-job").start(step2()).build();
	}

	@Bean
	public Step step1() throws Exception {
		return steps.get("step1").<Person, Person> chunk(10).faultTolerant().skipPolicy(skipPolicy())
				.reader(itemReader()).processor(asyncItemProcessor()).writer(asyncItemWriter()).build();
	}

	@Bean
	public Step step2() throws Exception {
		return steps.get("step2").<Person, String> chunk(10).faultTolerant().retry(IllegalArgumentException.class)
				.retryLimit(3).reader(itemReader()).processor(retryItemProcessor()).writer(itemWriter()).build();
	}

	@Bean
	public SkipPolicy skipPolicy() {
		Map<Class<? extends Throwable>, Boolean> skip = new HashMap<Class<? extends Throwable>, Boolean>();
		skip.put(IllegalStateException.class, true);
		return new AsyncLimitCheckingItemSkipPolicy(1, skip);
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
	public RetryItemProcessor retryItemProcessor() {
		return new RetryItemProcessor();
	}

	@Bean
	@StepScope
	public SysoutItemWriter itemWriter() {
		return new SysoutItemWriter();
	}

	@Bean
	@StepScope
	public AsyncItemProcessor asyncItemProcessor() {
		AsyncItemProcessor<Person, Person> itemProcessor = new AsyncItemProcessor<>();
		itemProcessor.setDelegate(itemProcessor());
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(10);
		itemProcessor.setTaskExecutor(taskExecutor);
		return itemProcessor;
	}

	@Bean
	@StepScope
	public AsyncItemWriter asyncItemWriter() {
		AsyncItemWriter<Object> itemWriter = new AsyncItemWriter<>();
		itemWriter.setDelegate(itemWriter());
		return itemWriter;
	}

}
