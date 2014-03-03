package de.tbosch.batch.item;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class LogItemProcessor implements ItemProcessor<String[], String[]> {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogItemProcessor.class);

	@Override
	public String[] process(String[] item) throws Exception {
		LOGGER.info("Item {} processed ...", Arrays.toString(item));
		return item;
	}

}
