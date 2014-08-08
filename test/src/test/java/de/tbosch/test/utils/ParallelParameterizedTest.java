package de.tbosch.test.utils;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;

import de.tbosch.test.utils.ParallelParameterized.ThreadCount;

@RunWith(ParallelParameterized.class)
public class ParallelParameterizedTest {

	private final String name;

	@ThreadCount
	public static int count() {
		return 4;
	}

	@Parameters
	public static Collection<Object[]> params() {
		return Arrays.asList(new Object[] { "1" }, new Object[] { "2" }, new Object[] { "3" }, new Object[] { "4" },
				new Object[] { "5" }, new Object[] { "6" });
	}

	public ParallelParameterizedTest(String name) {
		this.name = name;
	}

	@Test
	public void test() {
		System.out.println(Thread.currentThread().getId() + ": " + name);
	}

}
