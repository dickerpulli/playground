package de.tbosch.commons.utils;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ListUtilsTest {

	@Test
	public void delete() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> listDeleted = ListUtils.delete(list, 2, 3);
		assertEquals(3, listDeleted.size());
		assertEquals(1, listDeleted.get(0).intValue());
		assertEquals(2, listDeleted.get(1).intValue());
		assertEquals(5, listDeleted.get(2).intValue());
	}

	@Test
	public void get() {
		List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
		List<Integer> listNew = ListUtils.get(list, 2, 3);
		assertEquals(2, listNew.size());
		assertEquals(3, listNew.get(0).intValue());
		assertEquals(4, listNew.get(1).intValue());
	}

}
