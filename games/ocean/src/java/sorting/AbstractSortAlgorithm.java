package sorting;

import java.util.Comparator;

public abstract class AbstractSortAlgorithm<T> implements SortAlgorithm<T> {

	protected Comparator<T> comp;

	public AbstractSortAlgorithm(Comparator<T> comp) {
		this.comp = comp;
	}

}