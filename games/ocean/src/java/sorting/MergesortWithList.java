package sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Abbildung des Algorithmus von http://de.wikipedia.org/wiki/Mergesort
 *
 * @author Thomas Bosch (tbosch@gmx.de)
 */
public class MergesortWithList<T> extends AbstractSortAlgorithm<T> {

	public MergesortWithList(Comparator<T> newcomp) {
		super(newcomp);
	}

	@Override
	public T[] sort(T[] data) {
		return (T[]) sort(new ArrayList<T>(Arrays.asList(data))).toArray();
	}

	public List<T> sort(List<T> data) {
		if (data.size() <= 1) {
			return data;
		}
		int middle = data.size() / 2;
		List<T> left = sort(new ArrayList<T>(data.subList(0, middle)));
		List<T> right = sort(new ArrayList<T>(data.subList(middle, data.size())));
		return merge(left, right);
	}

	private List<T> merge(List<T> left, List<T> right) {
		List<T> data = new ArrayList<T>();
		while (!left.isEmpty() && !right.isEmpty()) {
			T firstLeft = left.get(0);
			T firstRight = right.get(0);
			if (comp.compare(firstLeft, firstRight) <= 0) {
				data.add(firstLeft);
				left.remove(firstLeft);
			} else {
				data.add(firstRight);
				right.remove(firstRight);
			}
		}
		while (!left.isEmpty()) {
			T firstLeft = left.get(0);
			data.add(firstLeft);
			left.remove(firstLeft);
		}
		while (!right.isEmpty()) {
			T firstRight = right.get(0);
			data.add(firstRight);
			right.remove(firstRight);
		}
		return data;
	}

	public static void main(String[] args) {
		MergesortWithList<Integer> mergesort = new MergesortWithList<Integer>(new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return o1.compareTo(o2);
			}
		});
		System.out.println(Arrays.toString(mergesort.sort(new Integer[] { 2, 3, 4, 1, 6, 2, 5, 0 })));
	}
}
