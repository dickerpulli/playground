package de.tbosch.tools.jsudoku.model;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;

import org.junit.Test;
import org.springframework.util.ReflectionUtils;

public class SudokuMatrixTest {

	@Test
	public void testIstGueltig() throws Exception {
		SudokuMatrix matrix = new SudokuMatrix(new int[][] { { 1, 2, 3, 4 }, { 3, 4, 1, 2 }, { 2, 3, 4, 1 },
				{ 4, 1, 2, 3 } });
		boolean gueltig = matrix.istGueltig();
		assertEquals(true, gueltig);

		matrix = new SudokuMatrix(new int[][] { { 1, 2, 3, 0 }, { 3, 4, 1, 2 }, { 2, 3, 0, 1 }, { 4, 1, 2, 3 } });
		gueltig = matrix.istGueltig();
		assertEquals(true, gueltig);
	}

	@Test
		public void testGetQuadratNummer() throws Exception {
			SudokuMatrix matrix = new SudokuMatrix(new int[][] { { 1, 2, 3, 4 }, { 3, 4, 1, 2 }, { 2, 3, 4, 1 },
					{ 4, 1, 2, 3 } });
			Method method = ReflectionUtils.findMethod(SudokuMatrix.class, "holeQuadratNummer", int.class, int.class,
					int.class);
			ReflectionUtils.makeAccessible(method);
			assertEquals(0, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 0, 0, 4)).intValue());
			assertEquals(0, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 0, 1, 4)).intValue());
			assertEquals(1, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 0, 2, 4)).intValue());
			assertEquals(1, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 0, 3, 4)).intValue());
			assertEquals(0, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 1, 0, 4)).intValue());
			assertEquals(0, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 1, 1, 4)).intValue());
			assertEquals(1, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 1, 2, 4)).intValue());
			assertEquals(1, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 1, 3, 4)).intValue());
			assertEquals(2, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 2, 0, 4)).intValue());
			assertEquals(2, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 2, 1, 4)).intValue());
			assertEquals(3, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 2, 2, 4)).intValue());
			assertEquals(3, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 2, 3, 4)).intValue());
			assertEquals(2, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 3, 0, 4)).intValue());
			assertEquals(2, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 3, 1, 4)).intValue());
			assertEquals(3, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 3, 2, 4)).intValue());
			assertEquals(3, ((Integer)ReflectionUtils.invokeMethod(method, matrix, 3, 3, 4)).intValue());
		}

	@Test
	public void testGetGueltigeZahl() throws Exception {
		SudokuMatrix matrix = new SudokuMatrix(new int[][] { { 1, 2, 3, 0 }, { 3, 4, 1, 2 }, { 2, 3, 0, 1 },
				{ 4, 1, 2, 3 } });
		assertEquals(4, matrix.getGueltigeZahl(0, 3, 1));
		matrix.setZahl(0, 3, SudokuMatrix.LEER, true);
		assertEquals(4, matrix.getGueltigeZahl(0, 3, 4));
		matrix.setZahl(0, 3, SudokuMatrix.LEER, true);
		assertEquals(SudokuMatrix.LEER, matrix.getGueltigeZahl(0, 3, 5));
	}

}
