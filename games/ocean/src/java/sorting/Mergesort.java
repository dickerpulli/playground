package sorting;

import java.util.Comparator;

public class Mergesort<T> {

	protected Comparator<T> comp;
	private final T[] data;

	public Mergesort(T[] newdata, Comparator<T> newcomp) {
		comp = newcomp;
		data = newdata;
		sort(0, data.length - 1);
		newdata = data;
	}

	private T[] sort(int l, int r) {
		if (l < r) {
			int q = (l + r) / 2;

			sort(l, q);
			sort(q + 1, r);
			merge(l, q, r);
		}
		return data;
	}

	private void merge(int l, int q, int r) {
		Comparable<T>[] leftdata = new Comparable[data.length];

	}

}
