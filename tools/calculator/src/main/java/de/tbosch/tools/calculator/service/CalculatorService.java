package de.tbosch.tools.calculator.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CalculatorService {

	private static final Log LOG = LogFactory.getLog(CalculatorService.class);

	private Operand first = new Operand();

	private Operand second = new Operand();

	private Double result;

	private Operator operator;

	public double appendNumber(int value) {
		if (operator == null) {
			first.appendNumber(value);
			double calc = first.getValue();
			if (LOG.isDebugEnabled()) {
				LOG.debug("Value " + value + " appended to first: " + calc);
			}
			return calc;
		} else {
			second.appendNumber(value);
			double calc = second.getValue();
			if (LOG.isDebugEnabled()) {
				LOG.debug("Value " + value + " appended to second: " + calc);
			}
			return calc;
		}
	}

	public void appendPoint() {
		if (operator == null) {
			first.appendPoint();
			if (LOG.isDebugEnabled()) {
				LOG.debug("Point appended to first: " + first);
			}
		} else {
			second.appendPoint();
			if (LOG.isDebugEnabled()) {
				LOG.debug("Point appended to second: " + second);
			}
		}
	}

	public void call(Operator operator) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Operator set: " + operator);
		}
		this.operator = operator;
	}

	public double solve() {
		if (result != null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Solve: first=" + result + ", second=" + second
						+ ", operator=" + operator);
			}
			result = operate(result, second);
			return result;
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("Solve: first=" + first + ", second=" + second
						+ ", operator=" + operator);
			}
			result = operate(first, second);
			return result;
		}
	}

	private double operate(Operand one, Operand two) {
		return operate(one.getValue(), two.getValue());
	}

	private double operate(double one, Operand two) {
		return operate(one, two.getValue());
	}

	private double operate(double one, double two) {
		if (operator == null) {
			throw new IllegalArgumentException("Operator not set");
		}
		double calc;
		switch (operator) {
		case PLUS:
			calc = one + two;
			break;
		case MINUS:
			calc = one - two;
			break;
		case MULTIPLY:
			calc = one * two;
			break;
		case DIVIDE:
			calc = one / two;
			break;
		default:
			throw new IllegalArgumentException("Operator nicht bekannt: "
					+ operator);
		}
		return calc;
	}

	public void reset() {
		operator = null;
		result = null;
		first = new Operand();
		second = new Operand();
	}

}
