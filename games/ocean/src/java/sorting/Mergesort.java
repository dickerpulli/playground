package sorting;

import java.util.Arrays;
import java.util.Comparator;

public class Mergesort<T> extends AbstractSortAlgorithm<T> {

	public Mergesort(Comparator<T> newcomp) {
		super(newcomp);
	}

	@Override
	public T[] sort(T[] data) {
		return sort(0, data.length - 1, data);
	}

	private T[] sort(int l, int r, T[] data) {
		if (l < r) {
			int q = (l + r) / 2;

			sort(l, q, data);
			sort(q + 1, r, data);
			merge(l, q, r, data);
		}
		return data;
	}

	private void merge(int l, int q, int r, T[] data) {
		T[] arr = (T[]) new Object[data.length];
		int i, j;
		// copy left part to new array
		for (i = l; i <= q; i++) {
			arr[i] = data[i];
		}
		// copy right part to new array
		for (j = q + 1; j <= r; j++) {
			arr[r + q + 1 - j] = data[j];
		}
		i = l;
		j = r;
		// run compare method
		for (int k = l; k <= r; k++) {
			if (comp.compare(arr[i], arr[j]) <= 0) {
				data[k] = arr[i];
				i++;
			} else {
				data[k] = arr[j];
				j--;
			}
		}
	}

	public static void main(String[] args) {
		SortAlgorithm<Integer> mergesort = new Mergesort<Integer>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		System.out.println(Arrays.toString(mergesort.sort(new Integer[] { 2, 3, 4, 1, 6, 2, 5, 0 })));
	}
}
