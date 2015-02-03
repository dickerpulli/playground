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
		reset();
	}

	public SearchTree(Comparable<T> NewData) {
		root = traversal = new BinTreeNode(NewData);
		reset();
	}

	// /Override - Automatisch eingefügt wegen 'implements
	// TraversableSortedContainer<T>'
	@Override
	@SuppressWarnings("unchecked")
	public Comparable<T> contains(Comparable<T> obj) {
		if (root == null)
			return null;
		if (root.getData().compareTo((T) obj) == 0) {
			return root.getData();
		} else {
			BinTreeNode pointer = root;
			pointer = contains(obj, pointer);
			return (pointer.getData().compareTo((T) obj) == 0) ? pointer
					.getData() : null;
		}
	}// contains(Comparable<T> obj)

	@SuppressWarnings("unchecked")
	public BinTreeNode contains(Comparable<T> obj, BinTreeNode pointer) {
		if (pointer.getData().compareTo((T) obj) == 0) {
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
		Comparable<T> exist = contains(obj);
		if (exist == null) {
			BinTreeNode pointer = contains(obj, root);
			if (pointer.getData().compareTo((T) obj) > 0) {
				BinTreeNode NewNode = new BinTreeNode(obj);
				pointer.setLeftChild(NewNode);
				pointer = pointer.getLeftChild();
			} else if (pointer.getData().compareTo((T) obj) < 0) {
				BinTreeNode NewNode = new BinTreeNode(obj);
				pointer.setRightChild(NewNode);
				pointer = pointer.getRightChild();
			}
			return pointer.getData();
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Comparable<T> remove(Comparable<T> obj) {
		if (obj == null || obj == null)
			return null;
		BinTreeNode pointer = contains(obj, root);
		if (pointer.getData().compareTo((T) obj) != 0)
			return null;

		if (pointer == traversal)
			increment();

		delete(pointer);
		return obj;
	}

	public BinTreeNode delete(BinTreeNode pointer) {
		boolean isroot = false;
		if (pointer == root) {
			isroot = true;
		}
		if (pointer.isLeaf()) {
			if (isroot) {
				root = traversal = null;
				return null;
			}
			if (pointer.Parent.LSon == pointer) {
				pointer.Parent.LSon = null;
			} else {
				pointer.Parent.RSon = null;
			}
			return pointer.Parent;
		}// if(pointer.isLeaf())
		if (pointer.LSon == null) {
			if (isroot) {
				root = traversal = pointer.getRightChild();
				pointer.getRightChild().Parent = null;
				return root;
			}
			pointer.getRightChild().Parent = pointer.Parent;
			if (pointer.Parent.LSon == pointer) {
				pointer.Parent.setLeftChild(pointer.RSon);
			} else {
				pointer.Parent.setRightChild(pointer.RSon);
			}
			return pointer.Parent;
		}// if(pointer.LSon==null)
		if (pointer.RSon == null) {
			if (isroot) {
				root = traversal = pointer.getLeftChild();
				pointer.getLeftChild().Parent = null;
				return root;
			}
			pointer.getLeftChild().Parent = pointer.Parent;
			if (pointer.Parent.LSon == pointer) {
				pointer.Parent.setLeftChild(pointer.LSon);
			} else {
				pointer.Parent.setRightChild(pointer.LSon);
			}
			return pointer.Parent;
		}// if(pointer.RSon==null)
			// Fall mit beiden Kindern -- Gehe einmal nach li und dann das
			// rechteste Kind!
		BinTreeNode pointerSubset = pointer.getLeftChild();
		while (pointerSubset.getRightChild() != null) {
			pointerSubset = pointerSubset.getRightChild();
		}
		BinTreeNode result = pointerSubset.getParent();
		pointer.Data = pointerSubset.getData();
		if (pointerSubset.Parent == pointer) {
			if (pointerSubset.getLeftChild() != null) {
				pointerSubset.Parent.setLeftChild(pointerSubset.getLeftChild());
			} else {
				pointerSubset.Parent.LSon = null;
			}
		} else {
			if (pointerSubset.getLeftChild() != null) {
				pointerSubset.Parent
						.setRightChild(pointerSubset.getLeftChild());
			} else {
				pointerSubset.Parent.RSon = null;
			}
		}
		return result;
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
				if (traversal.getRightChild() != null) {
					traversal = traversal.getRightChild();
					Origin = OriginEnum.ABOVE;
				} else {
					Origin = OriginEnum.RIGHT;
				}
				break;// LEFT
			case RIGHT:
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
		BinTreeNode traversalOld = traversal;
		while (!isAtEnd()) {
			increment();
			if (traversalOld != traversal) {
				size++;
				traversalOld = traversal;
			}
		}
		return size + 1;
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
		String traverse = "" + getMin().getData() + " (" + maxSearch(getMin())
				+ ")";
		reset();
		BinTreeNode temp = traversal;
		while (!isAtEnd()) {
			increment();
			if (temp != traversal)
				traverse += ", " + traversal.getData() + " ("
						+ maxSearch(traversal) + ")";
			temp = traversal;
		}
		return traverse;
	}

}// Class SearchTree<T>
