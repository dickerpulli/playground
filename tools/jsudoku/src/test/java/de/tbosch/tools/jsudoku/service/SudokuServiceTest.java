/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.tbosch.tools.jsudoku.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.tbosch.tools.jsudoku.model.SudokuMatrix;

public class SudokuServiceTest {

	private SudokuService service;

	@Before
	public void setUp() {
		service = new SudokuService();
	}

	@Test
	public void testLoeseSudoku() {
		SudokuMatrix matrix = new SudokuMatrix(new int[][] { { 1, 2, 3, 0 }, { 3, 4, 1, 2 }, { 2, 3, 0, 1 },
				{ 4, 1, 2, 3 } });
		SudokuMatrix geloest = service.loeseSudoku(matrix);
		assertNotNull(geloest);
		assertEquals(4, geloest.getZahlen()[0][3]);
		assertEquals(4, geloest.getZahlen()[2][2]);

		matrix = new SudokuMatrix(new int[][] { { 1, 2, 3, 0 }, { 0, 4, 1, 2 }, { 2, 3, 0, 1 }, { 4, 1, 2, 3 } });
		geloest = service.loeseSudoku(matrix);
		assertNotNull(geloest);
		assertEquals(4, geloest.getZahlen()[0][3]);
		assertEquals(3, geloest.getZahlen()[1][0]);
		assertEquals(4, geloest.getZahlen()[2][2]);

		matrix = new SudokuMatrix(new int[][] { { 1, 2, 3, 0 }, { 0, 4, 1, 2 }, { 2, 0, 0, 1 }, { 4, 0, 2, 3 } });
		geloest = service.loeseSudoku(matrix);
		assertNotNull(geloest);
		assertEquals(4, geloest.getZahlen()[0][3]);
		assertEquals(3, geloest.getZahlen()[1][0]);
		assertEquals(3, geloest.getZahlen()[2][1]);
		assertEquals(4, geloest.getZahlen()[2][2]);
		assertEquals(1, geloest.getZahlen()[3][1]);

		matrix = new SudokuMatrix(new int[][] { { 1, 2, 0, 0 }, { 0, 4, 1, 2 }, { 2, 0, 0, 1 }, { 4, 0, 2, 3 } });
		geloest = service.loeseSudoku(matrix);
		assertNotNull(geloest);
		assertEquals(4, geloest.getZahlen()[0][3]);
		assertEquals(3, geloest.getZahlen()[0][2]);
		assertEquals(3, geloest.getZahlen()[1][0]);
		assertEquals(3, geloest.getZahlen()[2][1]);
		assertEquals(4, geloest.getZahlen()[2][2]);
		assertEquals(1, geloest.getZahlen()[3][1]);

		matrix = new SudokuMatrix(new int[][] { { 1, 2, 0, 0 }, { 0, 4, 0, 0 }, { 2, 0, 0, 1 }, { 4, 0, 2, 3 } });
		geloest = service.loeseSudoku(matrix);
		assertNotNull(geloest);
		assertEquals(4, geloest.getZahlen()[0][3]);
		assertEquals(3, geloest.getZahlen()[0][2]);
		assertEquals(3, geloest.getZahlen()[1][0]);
		assertEquals(1, geloest.getZahlen()[1][2]);
		assertEquals(2, geloest.getZahlen()[1][3]);
		assertEquals(3, geloest.getZahlen()[2][1]);
		assertEquals(4, geloest.getZahlen()[2][2]);
		assertEquals(1, geloest.getZahlen()[3][1]);
	}

	@Test
	public void testLoesung() {
		SudokuMatrix matrix = new SudokuMatrix(new int[][] { { 1, 2, 0, 0 }, { 0, 0, 0, 0 }, { 2, 4, 3, 1 },
				{ 0, 1, 0, 0 } });
		SudokuMatrix geloest = service.loeseSudoku(matrix);
		assertNotNull(geloest);
		assertEquals(4, geloest.getZahlen()[0][2]);
		assertEquals(3, geloest.getZahlen()[0][3]);
		assertEquals(4, geloest.getZahlen()[1][0]);
		assertEquals(3, geloest.getZahlen()[1][1]);
		assertEquals(1, geloest.getZahlen()[1][2]);
		assertEquals(2, geloest.getZahlen()[1][3]);
		assertEquals(2, geloest.getZahlen()[2][0]);
		assertEquals(4, geloest.getZahlen()[2][1]);
		assertEquals(3, geloest.getZahlen()[2][2]);
		assertEquals(1, geloest.getZahlen()[3][1]);
		assertEquals(2, geloest.getZahlen()[3][2]);
		assertEquals(4, geloest.getZahlen()[3][3]);
	}

	@Test
	public void testLoesung9x9() {
		SudokuMatrix matrix = new SudokuMatrix(new int[][] { { 7, 1, 0, 0, 0, 8, 0, 9, 4 },
				{ 4, 0, 0, 1, 9, 0, 0, 0, 0 }, { 0, 0, 8, 0, 0, 2, 0, 3, 0 }, { 0, 7, 0, 2, 5, 0, 0, 0, 0 },
				{ 5, 0, 4, 9, 0, 3, 8, 0, 6 }, { 1, 0, 0, 0, 0, 0, 3, 2, 0 }, { 0, 5, 0, 4, 0, 0, 7, 0, 0 },
				{ 2, 0, 7, 0, 6, 1, 0, 0, 3 }, { 9, 4, 0, 3, 0, 0, 0, 0, 8 } });
		SudokuMatrix geloest = service.loeseSudoku(matrix);
		assertNotNull(geloest);
		assertTrue(geloest.istGueltig());
	}

}