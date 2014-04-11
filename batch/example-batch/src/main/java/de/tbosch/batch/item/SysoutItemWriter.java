package de.tbosch.batch.item;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class SysoutItemWriter implements ItemWriter<Object> {

	@Override
	public void write(List<? extends Object> items) throws Exception {
		for (Object item : items) {
			System.out.println(item);
		}
	}

}
