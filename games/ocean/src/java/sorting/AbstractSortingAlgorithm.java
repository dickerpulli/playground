package sorting;

import java.util.Comparator;

/**
 * The abstract sorting algorithm that holds the common fields and methods like the {@link Comparator} object.
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public abstract class AbstractSortingAlgorithm<T> implements SortingAlgorithm<T> {

	/**
	 * The internal comparator defining the sorting order.
	 */
	protected Comparator<T> comp;

	public AbstractSortingAlgorithm(Comparator<T> comp) {
		this.comp = comp;
	}

}