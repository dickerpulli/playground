package sorting;

import java.util.Arrays;
import java.util.Comparator;

public class Bubblesort<T> extends AbstractSortAlgorithm<T> {

	public Bubblesort(Comparator<T> comp) {
		super(comp);
	}

	@Override
	public T[] sort(T[] data) {
		if (data.length <= 1) {
			return data;
		}
		for (int i = 0; i < data.length - 1; i++) {
			if (comp.compare(data[i], data[i + 1]) > 0) {
				T temp = data[i + 1];
				data[i + 1] = data[i];
				data[i] = temp;
			}
		}
		T greatest = data[data.length - 1];
		T[] others = sort(Arrays.copyOf(data, data.length - 1));
		T[] sorted = Arrays.copyOf(others, data.length);
		sorted[data.length - 1] = greatest;
		return sorted;
	}

	public static void main(String[] args) {
		SortAlgorithm<Integer> mergesort = new Bubblesort<Integer>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		System.out.println(Arrays.toString(mergesort.sort(new Integer[] { 2, 3, 4, 1, 6, 2, 5, 0 })));
	}
}
