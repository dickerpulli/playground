package de.tbosch.batch.item;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import de.tbosch.batch.model.Person;

public class SysoutItemWriter implements ItemWriter<Person> {

	@Override
	public void write(List<? extends Person> items) throws Exception {
		for (Person item : items) {
			System.out.println(item);
		}
	}

}
