package de.tbosch.batch.item;

import java.util.Arrays;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class SysoutItemWriter implements ItemWriter<String[]> {

	@Override
	public void write(List<? extends String[]> items) throws Exception {
		for (String[] item : items) {
			System.out.println(Arrays.toString(item));
		}
	}

}
