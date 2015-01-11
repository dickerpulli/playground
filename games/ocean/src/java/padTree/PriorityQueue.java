package padTree;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

/**
 * priority queue using an array
 *
 * @author Martin Oellrich
 */

public class PriorityQueue<T> {

	/***** private declarations *****/

	private static final int UNUSED = -1; // code for "no valid index"

	private static boolean _checkMode = false; // flag for debugging

	private Comparable<T>[] _objects; // heap as array, valid indices: 0 ... n-1

	private int _lastIndex; // index of last element in array

	private int _current;

	private final int _root;

	private int _currentlevel; // levels, where the root is of level 0

	/***** public functions *****/

	public PriorityQueue(int capacity) {
		_objects = new Comparable[capacity];
		_lastIndex = UNUSED;
		_current = 0;
		_root = (_objects.length >= 0) ? 0 : UNUSED;
		_currentlevel = _root;
	} // PriorityQueue()

	public PriorityQueue(Comparable<T>[] comp) {
		_objects = comp;
		// Erstelle vollen HEAP /**PLACEHOLDER**/
		_lastIndex = comp.length - 1;
		_current = 0;
		_root = (_objects.length >= 0) ? 0 : UNUSED;
		_currentlevel = _root;
	} // PriorityQueue()

	private int _parent(int newcurrent) {
		if (newcurrent == _root)
			return UNUSED; // root doesnt have a parent
		return (newcurrent - 1) / 2;

	} // _parent()

	private void goToParent() {
		if (_current == _root)
			_current = UNUSED; // root doesnt have a parent
		_current = (_current - 1) / 2;
	} // goToParent()

	private int goToleft() {
		if (2 * _current + 1 <= _lastIndex) {
			_current = 2 * _current + 1;
		} else
			_current = UNUSED;
		return _current;
	} // goToLeft()

	private int _left(int newcurrent) {
		if (2 * newcurrent + 1 <= _lastIndex) {
			return 2 * newcurrent + 1;
		} else
			return UNUSED;
	} // _left()

	private int goToRight() {
		if (2 * _current + 2 <= _lastIndex) {
			_current = 2 * _current + 2;
		} else
			_current = UNUSED;
		return _current;
	} // goToRight()

	private int _right(int newcurrent) {
		if (2 * newcurrent + 2 <= _lastIndex) {
			return 2 * newcurrent + 2;
		} else
			return UNUSED;
	} // _right()

	public void push(Comparable<T> element) {
		if (_lastIndex == _objects.length - 1) {
			return;
		}
		_lastIndex++;
		int index = _lastIndex;
		_objects[index] = element;
		int parent = _parent(index);

		while (parent != UNUSED
				&& _objects[parent].compareTo((T) _objects[index]) < 0) {
			// tausche, wenn _objects[parent] < _objects[index]
			_objects[index] = _objects[parent];

			// gehe zum alten parent und vergleiche mit dessen parent
			index = parent;
			parent = _parent(parent);
		}
		_objects[index] = element;

	} // push()

	public Comparable<T> top() throws NoSuchElementException {
		if (_lastIndex == UNUSED) { // alternativ: root
			throw new NoSuchElementException("Heap is empty!");
		}
		return _objects[_lastIndex];
	} // top()

	public Comparable<T> pop() throws NoSuchElementException {
		if (_lastIndex == UNUSED) { // alternativ: root
			throw new NoSuchElementException("Heap is empty!");
		}

		// speichere oberstes Element für später
		Comparable<T> topObj = _objects[0];

		// lösche oberstes Element
		_objects[0] = _objects[_lastIndex];
		_lastIndex--;

		// stelle heapify-Struktur wieder her
		if (_lastIndex > 0) {
			_heapify(0);
		}

		return topObj;
	} // pop()

	public int size() {
		return _lastIndex + 1;
	} // size()

	public boolean isEmpty() {
		return (_lastIndex == UNUSED);
	} // isEmpty()

	@Override
	public String toString() {
		// Tiefe des Baumes
		String tree = new String();
		double height = Math.ceil(Math.log(_lastIndex) / Math.log(2)) + 1;
		for (int i = 1; i <= height; i++) {
			for (int j = i + 1; j < height; j++) {
				tree += " ";
			}
			for (int k = (int) Math.pow(2, i - 1) - 1; k <= ((int) Math.pow(2,
					i) - 2) && k <= _lastIndex; k++) {
				tree += _objects[k] + " ";
			}
			tree += "\n";
		}
		return tree;
	} // toString()

	private void _heapify(int i) {
		Comparable<T> currObj = _objects[i];
		int min;
		while (true) {
			int left = _left(i);
			int right = _right(i);
			if (left == UNUSED)
				break;
			if (right == UNUSED) { // nur noch ein Kind über, eine Iteration
				if (_objects[left].compareTo((T) currObj) < 0) {
					// tausche
					_objects[i] = _objects[left];
					i = left;
				}
				break;
			}
			if (_objects[left].compareTo((T) _objects[right]) < 0)
				min = left;
			else
				min = right;
			if (_objects[min].compareTo((T) currObj) >= 0) // found correct
															// position
				break;
			tausche(i, min);
			i = min;
		}
	} // _heapify()

	private void tausche(int i, int j) {
		Comparable<T> temp = _objects[i];
		_objects[i] = _objects[j];
		_objects[j] = temp;
	}

	public void heapsort() {
		for (int i = _parent(_lastIndex); i >= 0; i--)
			_heapify(i);
	}

	/*** debugging methods *****************************************************/

	/**
	 * switch debugging mode
	 */
	public static void setCheck(boolean mode) {
		_checkMode = mode;
	} // setCheck()

	/**
	 * check consistency of heap property in entire tree
	 *
	 * @return is heap property consistent in queue?
	 */
	private boolean _checkHeap() {
		boolean retval = true;

		if (_checkMode) // checking wanted
			for (int i = _lastIndex; i > 0; --i) {
				Comparable<T> obj = _objects[i];
				Comparable<T> par = _objects[_parent(i)];

				if (obj.compareTo((T) par) < 0) {
					System.err.println("PriorityQueue: Inkonsistenz " + obj
							+ " < " + par + " bei Index " + i + " gefunden.");
					retval = false;
				}
			} // for ( i )

		return retval;

	} // _checkHeap()

	/*************************** MAIN-METHOD *****************************/

	public static void main(String[] args) {
		int Length;
		if (args.length == 1) {
			Length = Integer.parseInt(args[0]);
		} else {
			Scanner sc = new Scanner(System.in);
			System.out.print("Geben Sie bitte die Anzahl ein: ");
			Length = sc.nextInt();
		}
		// Create Random Array
		Integer[] IntRandomArray = new Integer[Length];
		Double[] DoubleRandomArray = new Double[Length];

		for (int i = 0; i < Length; i++) {
			IntRandomArray[i] = new Random().nextInt(100);
			DoubleRandomArray[i] = new Random().nextDouble();
		}

		for (int i = 0; i < Length; i++) {
			System.out.println(IntRandomArray[i].toString());
		}

		for (int i = 0; i < Length; i++) {
			System.out.println(DoubleRandomArray[i].toString());
		}

		PriorityQueue IntArray = new PriorityQueue(IntRandomArray);
		PriorityQueue DoubleArray = new PriorityQueue(DoubleRandomArray);

		IntArray.heapsort();
		DoubleArray.heapsort();
		System.out.println(IntArray.toString());
		System.out.println(DoubleArray.toString());

	} // main()
} // class PriorityQueue