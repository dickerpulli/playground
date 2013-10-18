package de.tbosch.commons.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {

	private BigDecimalUtils() {
		// utils
	}

	/**
	 * Rundet eine Zahl auf n Stellen hinter dem Komma
	 * 
	 * @param number
	 *            Die Zahl
	 * @param decimals
	 *            Die Stellen hinter dem Komma
	 */
	public static BigDecimal round(BigDecimal number, int decimals) {
		if (number == null)
			return null;
		return number.setScale(decimals, RoundingMode.HALF_UP);
	}

	/**
	 * Addiert einen Prozentanteil hinzu.<br>
	 * NULL-sicher: Bei einer NULL im Parameter wird auch NULL zurück gegeben.
	 * 
	 * @param base
	 *            Die Basiszahl
	 * @param toAdd
	 *            Der Prozenzsatz zu addieren
	 * @return (x * (1 + prozent/100))
	 */
	public static BigDecimal addPercent(BigDecimal base, BigDecimal toAdd) {
		if (base == null || toAdd == null)
			return null;
		return base.multiply(new BigDecimal("1").add(toAdd.divide(new BigDecimal("100"))));
	}

	/**
	 * Holt den Prozentanteil einer Zahl.<br>
	 * NULL-sicher: Bei einer NULL im Parameter wird auch NULL zurück gegeben.
	 * 
	 * @param base
	 *            Die Zahl
	 * @param toGet
	 *            Der Prozentsatz
	 * @return (x * (prozent/100))
	 */
	public static BigDecimal getPercent(BigDecimal base, BigDecimal toGet) {
		if (base == null || toGet == null)
			return null;
		return base.multiply(toGet.divide(new BigDecimal("100")));
	}

	/**
	 * Subtrahiert einen Prozentanteil von einer Zahl.<br>
	 * NULL-sicher: Bei einer NULL im Parameter wird auch NULL zurück gegeben.
	 * 
	 * @param base
	 *            Die Zahl
	 * @param toSubtract
	 *            Der Prozentsatz
	 * @return (x / (1 + prozent/100))
	 */
	public static BigDecimal subtractPercent(BigDecimal base, BigDecimal toSubtract) {
		if (base == null || toSubtract == null)
			return null;
		// CSOFF: MagicNumberCheck
		BigDecimal sub = toSubtract.divide(new BigDecimal("100"), 32, RoundingMode.HALF_UP);
		BigDecimal div = new BigDecimal("1").add(sub);
		return base.divide(div, 32, RoundingMode.HALF_UP);
		// CSON: MagicNumberCheck
	}

	/**
	 * Addiert zwei Zahlen und gibt das Ergebnis zurück.<br>
	 * NULL-sicher: Ist der erste Parameter NULL wird auch NULL zurück gegeben. Ist der zweite Parameter NULL, so bleibt
	 * die Zahl, wird die erste Zahl unverändert zurück gegeben.
	 * 
	 * @param eins
	 *            Die erste Zahl
	 * @param zwei
	 *            Die zweite Zahl
	 * @return Die Additition
	 */
	public static BigDecimal add(BigDecimal eins, BigDecimal zwei) {
		if (eins == null)
			return null;
		if (zwei == null)
			return eins;
		return eins.add(zwei);
	}

	/**
	 * Multipiziert einen BigDecimal mit einem Double.<br>
	 * NULL-sicher: Bei einer NULL im Parameter wird auch NULL zurück gegeben.
	 * 
	 * @param eins
	 * @param zwei
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal eins, Double zwei) {
		if (eins == null || zwei == null)
			return null;
		return eins.multiply(new BigDecimal(zwei));
	}

	/**
	 * Multipiziert einen BigDecimal mit einem Double.<br>
	 * NULL-sicher: Bei einer NULL im Parameter wird auch NULL zurück gegeben.
	 * 
	 * @param eins
	 * @param zwei
	 * @return
	 */
	public static BigDecimal multiply(BigDecimal eins, Integer zwei) {
		if (eins == null || zwei == null)
			return null;
		return eins.multiply(new BigDecimal(zwei));
	}

}
