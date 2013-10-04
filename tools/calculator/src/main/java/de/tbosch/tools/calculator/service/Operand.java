package de.tbosch.tools.calculator.service;

public class Operand {

	private int beforePoint;

	private int afterPoint;

	private boolean withPoint;

	private double calcDecimals(int decimals) {
		if (decimals == 0) {
			return 0.0;
		}
		return decimals / Math.pow(10, Math.floor(Math.log10(decimals)) + 1);
	}

	public double getValue() {
		return beforePoint + calcDecimals(afterPoint);
	}

	public void appendNumber(int value) {
		if (withPoint) {
			afterPoint = afterPoint * 10 + value;
		} else {
			beforePoint = beforePoint * 10 + value;
		}
	}

	@Override
	public String toString() {
		return "Operand [beforePoint=" + beforePoint + ", afterPoint="
				+ afterPoint + ", withPoint=" + withPoint + "]";
	}

	public void appendPoint() {
		withPoint = true;
	}

}
