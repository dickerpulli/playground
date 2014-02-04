package de.tbosch.tools.calculator.service;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

public class OperandTest {

	private Operand operand;

	@Before
	public void before() {
		operand = new Operand();
	}

	@Test
	public void calcDecimals() throws Exception {
		Method method = ReflectionUtils.findMethod(Operand.class,
				"calcDecimals", int.class);
		ReflectionUtils.makeAccessible(method);
		assertEquals(0.5,
				(double) ReflectionUtils.invokeMethod(method, operand, 50000),
				0.009);
		assertEquals(0.1,
				(double) ReflectionUtils.invokeMethod(method, operand, 10000),
				0.009);
		assertEquals(0.99, (double) ReflectionUtils.invokeMethod(method,
				operand, 99000000), 0.009);
	}

}
