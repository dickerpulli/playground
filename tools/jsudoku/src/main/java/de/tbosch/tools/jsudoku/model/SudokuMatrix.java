package de.tbosch.tools.jsudoku.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author tbo
 */
public class SudokuMatrix {

	private static final Log LOG = LogFactory.getLog(SudokuMatrix.class);

	/** Der leere Eintrag */
	public static final int LEER = 0;

	/** Die Dimension der Matrix, 9 = 9x9 */
	private final int dimension;

	/** Der Inhalt */
	private final int[][] zahlen;

	/**
	 * Der Konstruktor.
	 * 
	 * @param zahlen
	 *            Das interne Zahlen-Array.
	 */
	public SudokuMatrix(int[][] zahlen) {
		if (Math.sqrt(zahlen.length) % 1 != 0) {
			throw new IllegalArgumentException(
					"Dimension muss quadratisch sein");
		}
		for (int i = 0; i < zahlen.length; i++) {
			if (Math.sqrt(zahlen[i].length) % 1 != 0) {
				throw new IllegalArgumentException(
						"Dimension muss quadratisch sein");
			}
		}
		this.dimension = zahlen.length;
		this.zahlen = new int[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				this.zahlen[i][j] = zahlen[i][j];
			}
		}
	}

	/**
	 * Der Konstruktor. Leere Elemente.
	 * 
	 * @param dimension
	 *            Die Dimension.
	 */
	public SudokuMatrix(int dimension) {
		if (Math.sqrt(dimension) % 1 != 0) {
			throw new IllegalArgumentException(
					"Dimension muss quadratisch sein");
		}
		this.dimension = dimension;
		this.zahlen = new int[dimension][dimension];
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				this.zahlen[i][j] = LEER;
			}
		}
	}

	/**
	 * Konstruktor zum Kopieren.
	 * 
	 * @param matrix
	 */
	public SudokuMatrix(SudokuMatrix matrix) {
		this(matrix.getZahlen());
	}

	/**
	 * Die Dimension der Matrix.
	 * 
	 * @return Dimension.
	 */
	public int getDimension() {
		return dimension;
	}

	/**
	 * Gibt die interne Darstellung der Matrix wieder.
	 * 
	 * @return Zahlen-Array.
	 */
	public int[][] getZahlen() {
		return zahlen;
	}

	/**
	 * Prüft, on die Matrix komplett gefüllt ist.
	 * 
	 * @return Keine LEERen Felder mehr?
	 */
	public boolean istFertig() {
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (zahlen[i][j] == LEER)
					return false;
			}
		}
		return true;
	}

	/**
	 * Sagt, ob eine SudokuMatrix in einem gültigen Zustand ist.
	 * 
	 * @param sudokuMatrix
	 * @return
	 */
	public boolean istGueltig() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Prüfe Matrix " + this);
		}
		Map<Integer, List<Integer>> spalteMap = new HashMap<Integer, List<Integer>>();
		Map<Integer, List<Integer>> zeileMap = new HashMap<Integer, List<Integer>>();
		Map<Integer, List<Integer>> quadratMap = new HashMap<Integer, List<Integer>>();
		for (int i = 0; i < this.getDimension(); i++) {
			// Hole die Zahlen der aktuellen Zeile
			List<Integer> zeile = zeileMap.get(i);
			if (zeile == null) {
				zeile = new ArrayList<Integer>();
				zeileMap.put(i, zeile);
			}
			for (int j = 0; j < this.getDimension(); j++) {
				// Hole die Zahlen der aktuellen Spalte
				List<Integer> spalte = spalteMap.get(j);
				if (spalte == null) {
					spalte = new ArrayList<Integer>();
					spalteMap.put(j, spalte);
				}

				// Hole die Zahlen des aktuellen Quadrats
				int q = getQuadratNummer(i, j);
				List<Integer> quadrat = quadratMap.get(q);
				if (quadrat == null) {
					quadrat = new ArrayList<Integer>();
					quadratMap.put(q, quadrat);
				}

				// Validiere die aktuelle Zahl
				int zahl = this.getZahlen()[i][j];
				if (LOG.isDebugEnabled()) {
					LOG.debug("Prüfe Zeile " + i + ", Spalte " + j + ": "
							+ zahl);
				}
				if (zahl != SudokuMatrix.LEER
						&& zahl > 0
						&& zahl <= this.getDimension()
						&& (zeile.contains(zahl) || spalte.contains(zahl) || quadrat
								.contains(zahl))) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Zeile " + i + ", Spalte " + j + ": " + zahl
								+ " ist ungültig");
						LOG.debug("Matrix ist ungültig");
					}
					return false;
				}
				zeile.add(zahl);
				spalte.add(zahl);
				quadrat.add(zahl);
			}
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug("Matrix ist gültig");
		}
		return true;
	}

	/**
	 * Holt die Nummer des Quadrats bei Zeile i und Spalte j. Z.B. (bei
	 * Dimension 9): 0 1 2, 3 4 5, 6 7 8
	 * 
	 * @param i
	 *            Die Zeile
	 * @param j
	 *            Die Spalte
	 * @return Die Nummer
	 */
	public int getQuadratNummer(int i, int j) {
		int x = j / (int) Math.sqrt(dimension) + 1;
		int y = i / (int) Math.sqrt(dimension);
		return x + (y * (int) Math.sqrt(dimension)) - 1;
	}

	/**
	 * Versucht eine gültige Zahl an der Stelle i,j einzufügen.
	 * 
	 * @param i
	 *            Zeile.
	 * @param j
	 *            Spalte.
	 * @param min
	 *            Der Mindestwert.
	 * @return Die Zahl, bzw. LEER, wenn keine gültige Zahl gefunden wurde.
	 */
	public int getGueltigeZahl(int i, int j, int min) {
		if (zahlen[i][j] != LEER) {
			throw new IllegalArgumentException(
					"Versuche für eine nicht-leere Position eine Zahl zu finden, i="
							+ i + ", j=" + j);
		}
		for (int z = min; z <= dimension; z++) {
			SudokuMatrix pruefMatrix = new SudokuMatrix(this);
			int[][] pruefZahlen = pruefMatrix.getZahlen();
			pruefZahlen[i][j] = z;
			if (pruefMatrix.istGueltig())
				return z;
		}
		return LEER;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb = sb.append("\nDimension = " + dimension + "\n");
		sb = sb.append(StringUtils.rightPad("", dimension * 4 + 1, "-"));
		sb = sb.append("\n");
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				if (j == 0) {
					sb = sb.append("| ");
				}
				sb = sb.append(zahlen[i][j] == LEER ? " " : zahlen[i][j]);
				if ((j + 1) % Math.sqrt(dimension) == 0) {
					sb = sb.append(" | ");
				} else {
					sb = sb.append("   ");
				}
			}
			if ((i + 1) % Math.sqrt(dimension) == 0) {
				sb = sb.append("\n");
				sb = sb.append(StringUtils.rightPad("", dimension * 4 + 1, "-"));
				sb = sb.append("\n");
			} else if (i < dimension - 1) {
				sb = sb.append("\n");
			}
		}
		return sb.toString();
	}

	/**
	 * Setzt eine Zahl an die gegebene Position.
	 * 
	 * @param i
	 *            Zeile.
	 * @param j
	 *            Spalte.
	 * @param zahl
	 *            Einzufügende Zahl.
	 */
	public void setZahl(int i, int j, int zahl) {
		setZahl(i, j, zahl, false);
	}

	/**
	 * Setzt eine Zahl an die gegebene Position.
	 * 
	 * @param i
	 *            Zeile.
	 * @param j
	 *            Spalte.
	 * @param zahl
	 *            Einzufügende Zahl.
	 * @param force
	 *            wenn TRUE, dann wird nicht geprüft, ob das Feld leer ist.
	 */
	public void setZahl(int i, int j, int zahl, boolean force) {
		if (zahlen[i][j] != LEER && !force) {
			throw new IllegalArgumentException(
					"Versuche an nicht-leere Position eine Zahl zu setzen");
		}
		zahlen[i][j] = zahl;
	}

}
