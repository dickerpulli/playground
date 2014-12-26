package sorting;

import java.util.Comparator;

/**
 * Sorting algorithm that implements an algorithm to sort an array of elements in a defined order. The order is defined
 * by the {@link Comparator} that have to be given to the constructor.
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public interface SortingAlgorithm<T> {

	public abstract T[] sort(T[] data);

}