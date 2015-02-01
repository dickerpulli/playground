package padTree;

import padInterfaces.TraversableSortedContainer;

public class SearchTree<T> extends BinTree<Comparable<T>> implements
		TraversableSortedContainer<T> {
	// Binärer Suchbaum mit Elementen aus Comparable<T>
	// abgeleitet von BinTree<BinTree<Comparable<T>>>

	// /Declarations
	protected BinTreeNode root;
	protected BinTreeNode traversal;

	// /Constructors
	public SearchTree() {
		// ThisConstructorIsLeftBlankIntentionally
	}

	public SearchTree(Comparable<T> NewData) {
		root = traversal = new BinTreeNode(NewData);
	}

	// /Override - Automatisch eingefügt wegen 'implements
	// TraversableSortedContainer<T>'
	@Override
	@SuppressWarnings("unchecked")
	public Comparable<T> contains(Comparable<T> obj) {
		if (root == null)
			return null;
		if (root.getData().compareTo((T) obj) == 0) { // .equals((T)obj)
			return root.getData();
		} else {
			BinTreeNode pointer = root;
			pointer = contains(obj, pointer);
			return (pointer.getData().equals(obj)) ? pointer.getData() : null;
		}
	}// contains(Comparable<T> obj)

	@SuppressWarnings("unchecked")
	public BinTreeNode contains(Comparable<T> obj, BinTreeNode pointer) {
		if (pointer.getData().compareTo((T) obj) == 0) { // .equals((T)obj)
			return pointer;
		} else {
			if (pointer.getData().compareTo((T) obj) > 0) {
				if (pointer.getLeftChild() == null) {
					return pointer;
				} else {
					pointer = pointer.getLeftChild();
				}
			} else {
				if (pointer.getRightChild() == null) {
					return pointer;
				} else {
					pointer = pointer.getRightChild();
				}
			}
			return contains(obj, pointer);
		}
	}// contains(Comparable<T> obj, BinTreeNode pointer)

	@Override
	@SuppressWarnings("unchecked")
	public Comparable<T> insert(Comparable<T> obj) {
		if (root == null) {
			root = traversal = new BinTreeNode(obj);
			return root.getData();
		}
		BinTreeNode pointer = contains(obj, root);
		if (pointer.getData().compareTo((T) obj) > 0) {
			BinTreeNode NewNode = new BinTreeNode(obj);
			pointer.setLeftChild(NewNode);
			pointer = pointer.getLeftChild();
		}
		if (pointer.getData().compareTo((T) obj) < 0) {
			BinTreeNode NewNode = new BinTreeNode(obj);
			pointer.setRightChild(NewNode);
			pointer = pointer.getRightChild();
		}
		return pointer.getData();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Comparable<T> remove(Comparable<T> obj) {
		if (obj == null || obj == null)
			return null;
		BinTreeNode pointer = contains(obj, root);
		if (pointer == traversal)
			reset();
		if (pointer.getData().compareTo((T) obj) != 0)
			return null;
		int size = getSize();
		delete(pointer);
		size = getSize();
		return obj;
	}

	public void delete(BinTreeNode pointer) {
		boolean isroot = false;
		if (pointer == root) {
			isroot = true;
		}
		if (pointer.isLeaf()) {
			if (isroot) {
				root = traversal = null;
				return;
			}
			if (pointer.Parent.LSon == pointer) {
				pointer.Parent.setLeftChild(pointer.Parent);
			} else {
				pointer.Parent.setRightChild(pointer.Parent);
			}
			return;
		}// if(pointer.isLeaf())
		if (pointer.LSon == null) {
			if (isroot) {
				root = traversal = root.getRightChild();
				root.Parent = null;
				return;
			}
			// pointer.getRightChild().Parent=pointer.Parent;
			if (pointer.Parent.LSon == pointer) {
				pointer.Parent.setLeftChild(pointer.RSon);
			} else {
				pointer.Parent.setRightChild(pointer.RSon);
			}
			return;
		}// if(pointer.LSon==null)
		if (pointer.RSon == null) {
			if (isroot) {
				root = traversal = root.getLeftChild();
				root.Parent = null;
				return;
			}
			pointer.getLeftChild().Parent = pointer.Parent;
			if (pointer.Parent.LSon == pointer) {
				pointer.Parent.setLeftChild(pointer.LSon);
				;
			} else {
				pointer.Parent.setRightChild(pointer.LSon);
			}
			return;
		}// if(pointer.RSon==null)
		// Fall mit beiden Kindern -- Gehe einmal nach li und dann das rechteste
		// Kind!!!
		BinTreeNode pointerSubset = pointer.getLeftChild();
		while (pointerSubset.getRightChild() != null) { // Ganz nach Rechts
														// gehen
			pointerSubset = pointerSubset.getRightChild();
		}
		pointer.Data = pointerSubset.getData();
		if (pointerSubset.getLeftChild() != null) {
			pointerSubset.Parent.setRightChild(pointerSubset.getLeftChild());
		} else {
			pointerSubset.Parent.RSon = null;
		}
		// pointerSubset.LSon=null;
		// pointerSubset.RSon=null;
		// pointerSubset.Parent=null;
	}// delete(BinTreeNode pointer)

	@Override
	public void reset() {
		traversal = getMin();
		Origin = OriginEnum.LEFT;
	}

	@Override
	public void increment() {
		if (isAtEnd())
			return;
		if (traversal == null) {
			System.err.println("Error in increment: Nullpointer!");
			return;
		}
		do {
			switch (Origin) {
			case ABOVE:
				if (traversal.getLeftChild() != null) {
					traversal = traversal.getLeftChild();
				} else {
					Origin = OriginEnum.LEFT;
				}
				break;// ABOVE
			case LEFT:
				if (traversal == root) {
					traversal = traversal.getRightChild();
					Origin = OriginEnum.ABOVE;
					break;
				}
				if (traversal.getRightChild() != null) {
					traversal = traversal.getRightChild();
					Origin = OriginEnum.ABOVE;
				} else {
					Origin = OriginEnum.RIGHT;
				}
				break;// LEFT
			case RIGHT:
				if (traversal == root) {
					traversal = traversal.getRightChild();
					Origin = OriginEnum.ABOVE;
					break;
				}
				if (traversal.isLeftChild()) {
					Origin = OriginEnum.LEFT;
				} else {
					Origin = OriginEnum.RIGHT;
				}
				traversal = traversal.getParent();
				break; // RIGHT
			default:
				System.err
						.println("Error: increment hat keine Richtung gespeichert");
				break;
			}// switch(Origin)
		} while (!(Origin == OriginEnum.LEFT || (Origin == OriginEnum.ABOVE && traversal
				.getLeftChild() == null)));
	}// increment()

	@Override
	public boolean isAtEnd() {
		if (traversal == null)
			return true;
		return isMax(traversal);
	}

	@Override
	public Comparable<T> currentData() {
		return traversal.getData();
	}

	@Override
	public int getSize() {
		int size = 0;
		traversal = getMin();
		while (!isAtEnd()) {
			increment();
			size++;
		}
		return size;
	}

	@Override
	public int maxSearch() {
		return maxSearch(root);
	}

	public int maxSearch(BinTreeNode Node) {
		if (Node == null)
			return 0;
		else {
			int HeightLeft = maxSearch(Node.getLeftChild());
			int HeightRight = maxSearch(Node.getRightChild());
			return (HeightLeft > HeightRight) ? 1 + HeightLeft
					: 1 + HeightRight;
		}
	}

	// /PrivateMethods

	// /PublicMethods
	public boolean isMax(BinTreeNode pointer) {
		BinTreeNode max = root;
		while (max.getRightChild() != null)
			max = max.getRightChild();
		return max == pointer;
	}

	public boolean isMin(BinTreeNode pointer) {
		BinTreeNode max = root;
		while (max.getLeftChild() != null)
			max = max.getLeftChild();
		return max == pointer;
	}

	public BinTreeNode getMax() {
		if (root == null)
			return null;
		BinTreeNode max = root;
		while (max.getRightChild() != null)
			max = max.getRightChild();
		return max;
	}

	public BinTreeNode getMin() {
		if (root == null)
			return null;
		BinTreeNode min = root;
		while (min.getLeftChild() != null)
			min = min.getLeftChild();
		return min;
	}

	public boolean checkBalances() {
		for (traversal = getMin(); !isAtEnd(); increment()) {
			int balance = checkBalances(traversal);
			if (-1 > balance || balance > 1) {
				return false;
			}
		}
		return true;
	}

	public int checkBalances(BinTreeNode pointer) {
		if (pointer.isLeaf())
			return 0;
		else {
			return maxSearch(pointer.getRightChild())
					- maxSearch(pointer.getLeftChild());
		}
	}

	@Override
	public String toString() {
		if (root == null) {
			return "Leerer Baum!";
		}
		String traverse = "" + getMin().getData();
		reset();
		while (!isAtEnd()) {
			increment();
			traverse += ", " + traversal.getData();
		}
		return traverse;
	}

	/*
	 * public static void main(String[] args) { SearchTree tree = new
	 * SearchTree();
	 * 
	 * for (Integer i = 0; i <= 5; i++) { tree.insert(i); }
	 * 
	 * //tree.remove(0);
	 * 
	 * System.out.println(tree.toString() + " getSize: " + tree.getSize()); }
	 */

}// Class SearchTree<T> 