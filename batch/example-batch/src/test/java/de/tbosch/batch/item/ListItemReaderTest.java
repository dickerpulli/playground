package de.tbosch.batch.item;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.PassThroughLineMapper;
import org.springframework.core.io.ClassPathResource;

public class ListItemReaderTest {

	private ListItemReader listItemReader;

	@Before
	public void before() throws Exception {
		listItemReader = new ListItemReader();
		listItemReader.setDelegate(stringItemReader());
	}

	@Test
	public void read() throws Exception {
		listItemReader.open(new ExecutionContext());
		assertEquals("[123, 456]", listItemReader.read().toString());
		assertEquals("[789, 012]", listItemReader.read().toString());
		assertEquals("[345, 678]", listItemReader.read().toString());
		assertEquals("[901]", listItemReader.read().toString());
		assertEquals(null, listItemReader.read());
	}

	private ItemReader<String> stringItemReader() throws Exception {
		FlatFileItemReader<String> itemReader = new FlatFileItemReader<>();
		itemReader.setResource(new ClassPathResource("in-test.txt"));
		itemReader.setLineMapper(new PassThroughLineMapper());
		itemReader.afterPropertiesSet();
		return itemReader;
	}

}
