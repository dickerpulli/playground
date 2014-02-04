package de.tbosch.commons.utils;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

	private ListUtils() {
		// utils
	}

	/**
	 * Löscht alle Elemente innerhalb der gegebenen Indexgrenzen.
	 * 
	 * @param original
	 *            Die original-Liste
	 * @param fromIndex
	 *            Der Startindex
	 * @param toIndex
	 *            Der Endindex
	 * @return Die modifizierte Liste
	 */
	public static <T> List<T> delete(List<T> original, int fromIndex, int toIndex) {
		List<T> deleted = new ArrayList<T>();
		for (int i = 0; i < original.size(); i++) {
			if (i < fromIndex || i > toIndex) {
				deleted.add(original.get(i));
			}
		}
		return deleted;
	}

	/**
	 * Holt alle Elemente innerhalb der Indexgrenzen.
	 * 
	 * @param original
	 *            Die original-Liste
	 * @param fromIndex
	 *            Der Startindex
	 * @param toIndex
	 *            Der Endindex
	 * @return Die modifizierte Liste
	 */
	public static <T> List<T> get(List<T> original, int fromIndex, int toIndex) {
		List<T> deleted = new ArrayList<T>();
		for (int i = 0; i < original.size(); i++) {
			if (i >= fromIndex && i <= toIndex) {
				deleted.add(original.get(i));
			}
		}
		return deleted;
	}

}
