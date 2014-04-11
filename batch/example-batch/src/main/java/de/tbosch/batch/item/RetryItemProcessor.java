package de.tbosch.batch.item;

import org.springframework.batch.item.ItemProcessor;

import de.tbosch.batch.model.Person;

public class RetryItemProcessor implements ItemProcessor<Person, String> {

	private static int i = 0;

	@Override
	public String process(Person item) throws Exception {
		if (i++ == 2) {
			throw new IllegalArgumentException("i is " + i);
		}
		return item.getFirstname();
	}

}
