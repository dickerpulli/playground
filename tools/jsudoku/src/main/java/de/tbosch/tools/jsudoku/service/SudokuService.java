package de.tbosch.tools.jsudoku.service;

import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.tbosch.tools.jsudoku.model.SudokuMatrix;

/**
 * 
 * @author tbo
 */
public class SudokuService {

	private static final Log LOG = LogFactory.getLog(SudokuService.class);

	/**
	 * Löst die Sudoku-Matrix.
	 * 
	 * @param startMatrix
	 *            Die zu lösende Matrix.
	 * @return Die Lösung, NULL wenn nicht zu lösen.
	 */
	public SudokuMatrix loeseSudoku(SudokuMatrix startMatrix) {
		if (LOG.isInfoEnabled()) {
			LOG.info("Versuche Lösung für Matrix: " + startMatrix);
		}
		SudokuMatrix matrix = new SudokuMatrix(startMatrix);
		Stack<Zahlposition> stack = new Stack<Zahlposition>();
		int min = 1;
		int zaehler = 0;
		while (!matrix.istFertig()) {
			zaehler = loop(matrix, stack, 0, 0, min, zaehler);
		}
		if (LOG.isInfoEnabled()) {
			LOG.info("Lösung in " + zaehler + " Schritten");
			LOG.info("Gelöste Matrix: " + matrix);
		}
		return matrix;
	}

	private int loop(SudokuMatrix matrix, Stack<Zahlposition> stack, int si,
			int sj, int min, int zaehler) {
		for (int i = si; i < matrix.getDimension(); i++) {
			for (int j = sj; j < matrix.getDimension(); j++) {
				if (matrix.getZahlen()[i][j] == SudokuMatrix.LEER) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Zahl suchen für Position " + i + ", " + j);
					}
					int zahl = matrix.getGueltigeZahl(i, j, min);
					if (zahl == SudokuMatrix.LEER) {
						if (stack.isEmpty()) {
							if (LOG.isDebugEnabled()) {
								LOG.debug("Keine Lösung gefunden");
							}
							throw new IllegalArgumentException(
									"Keine Lösung gefunden");
						}
						Zahlposition zahlposition = stack.pop();
						if (LOG.isDebugEnabled()) {
							LOG.debug("Alte Zahlposition wieder aufgenommen: "
									+ zahlposition);
						}
						matrix.setZahl(i, j, SudokuMatrix.LEER);
						min = zahlposition.getZahl() + 1;
						i = zahlposition.getI();
						j = zahlposition.getJ();
						matrix.setZahl(i, j, SudokuMatrix.LEER, true);
						if (LOG.isDebugEnabled()) {
							LOG.debug("Matrix: " + matrix);
						}
						zaehler = loop(matrix, stack, i, j, min, zaehler);
						break;
					}
					min = 1;
					sj = 0;
					matrix.setZahl(i, j, zahl);
					stack.push(new Zahlposition(i, j, zahl));
					zaehler++;
					if (LOG.isDebugEnabled()) {
						LOG.debug("Zahl gefunden: i=" + i + ", j=" + j
								+ ", zahl=" + zahl);
						LOG.debug("Stack: " + stack);
						LOG.debug("Matrix: " + matrix);
					}
				}
			}
		}
		return zaehler;
	}

}
