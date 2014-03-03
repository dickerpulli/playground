package de.tbosch.batch.item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import de.tbosch.batch.model.Person;

public class LogItemProcessor implements ItemProcessor<Person, Person> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogItemProcessor.class);

	@Override
	public Person process(Person item) throws Exception {
		LOGGER.info("Item {} processed ...", item);
		return item;
	}

}
