package sorting;

import java.util.Arrays;
import java.util.Comparator;

public class Bubblesort<T> extends AbstractSortAlgorithm<T> {

	public Bubblesort(Comparator<T> comp) {
		super(comp);
	}

	@Override
	public T[] sort(T[] data) {
		// Bei einer Länge von 1 ist der Algothmus am Emde
		if (data.length <= 1) {
			return data;
		}
		// Vertausche immer alle Elemente. die in der unsortierten Reihenfolge liegen
		for (int i = 0; i < data.length - 1; i++) {
			if (comp.compare(data[i], data[i + 1]) > 0) {
				T temp = data[i + 1];
				data[i + 1] = data[i];
				data[i] = temp;
			}
		}
		// Das letzte Element ist dann immer das größte
		T greatest = data[data.length - 1];
		// Sortiere dann den Rest
		T[] others = sort(Arrays.copyOf(data, data.length - 1));
		T[] sorted = Arrays.copyOf(others, data.length);
		// ... und verknüpfe die sortierte Restmenge mit dem größten Element
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
