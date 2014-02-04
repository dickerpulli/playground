package de.tbosch.tools.calculator.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CalculatorServiceTest {

	private CalculatorService calculator;

	@Before
	public void before() {
		calculator = new CalculatorService();
	}

	@Test(expected = IllegalArgumentException.class)
	public void calculateFail() {
		calculator.solve();
	}

	@Test
	public void calculate1() {
		calculator.call(Operator.PLUS);
		assertEquals(0.0, calculator.solve(), 0.009);
	}

	@Test
	public void calculate2() {
		calculator.appendNumber(0);
		calculator.appendNumber(0);
		calculator.appendNumber(1);
		calculator.appendNumber(2);
		calculator.call(Operator.PLUS);
		calculator.appendNumber(2);
		calculator.appendNumber(0);
		assertEquals(32.0, calculator.solve(), 0.009);
	}

	@Test
	public void calculate3() {
		calculator.appendNumber(1);
		calculator.appendNumber(2);
		calculator.call(Operator.MINUS);
		calculator.appendNumber(2);
		calculator.appendNumber(0);
		assertEquals(-8.0, calculator.solve(), 0.009);
	}

	@Test
	public void calculate4() {
		calculator.appendNumber(1);
		calculator.appendPoint();
		calculator.appendNumber(1);
		calculator.call(Operator.PLUS);
		calculator.appendNumber(2);
		assertEquals(3.1, calculator.solve(), 0.009);
	}

	@Test
	public void calculate5() {
		calculator.appendNumber(1);
		calculator.appendPoint();
		calculator.appendPoint();
		calculator.appendNumber(1);
		calculator.appendPoint();
		calculator.appendNumber(1);
		calculator.call(Operator.PLUS);
		calculator.appendNumber(2);
		assertEquals(3.11, calculator.solve(), 0.009);
	}

	@Test
	public void calculate6() {
		calculator.appendNumber(1);
		calculator.appendPoint();
		calculator.appendNumber(1);
		calculator.call(Operator.PLUS);
		calculator.appendNumber(2);
		assertEquals(3.1, calculator.solve(), 0.009);
		assertEquals(5.1, calculator.solve(), 0.009);
	}

}
