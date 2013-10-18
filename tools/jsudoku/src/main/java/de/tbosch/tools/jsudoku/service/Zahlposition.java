package de.tbosch.tools.jsudoku.service;

public class Zahlposition {

	private final int i;

	private final int j;

	private final int zahl;

	public Zahlposition(int i, int j, int zahl) {
		this.i = i;
		this.j = j;
		this.zahl = zahl;
	}

	/**
	 * @return the i
	 */
	public int getI() {
		return i;
	}

	/**
	 * @return the j
	 */
	public int getJ() {
		return j;
	}

	/**
	 * @return the zahl
	 */
	public int getZahl() {
		return zahl;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "[(" + i + "," + j + ")=" + zahl + "]";
	}

}
