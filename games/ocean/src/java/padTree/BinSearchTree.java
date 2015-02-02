package padTree;

import padInterfaces.TraversableSortedContainer;

public class BinSearchTree<T> extends BinTree<Comparable<T>> implements
		TraversableSortedContainer<T> {

	public BinSearchTree() {
		super();
	}

	public BinSearchTree(BinTree<Comparable<T>>.BinTreeNode NewNode) {
		super(NewNode);
	}

	public BinSearchTree(Comparable<T> NewData) {
		super(NewData);
	}

	@Override
	public Comparable<T> contains(Comparable<T> obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Comparable<T> insert(Comparable<T> obj) {
		while (!isAtLeaf()) {
			Comparable<T> currentData = currentData();
			if (currentData.compareTo((T) obj) == 0) {
				return currentData;
			} else if (currentData.compareTo((T) obj) > 0) {
				if (_curr.getLeftChild() != null) {
					_curr = _curr.getLeftChild();
					continue;
				} else {
					_curr.setLeftChild(new BinTreeNode(obj));
					return obj;
				}
			} else if (currentData.compareTo((T) obj) < 0) {
				if (_curr.getRightChild() != null) {
					_curr = _curr.getRightChild();
					continue;
				} else {
					_curr.setRightChild(new BinTreeNode(obj));
					return obj;
				}
			}
		}
		if (currentData() == null) {
			 _curr = new BinTreeNode(obj);
		} else if (currentData().compareTo((T) obj) > 0) {
			_curr.setLeftChild(new BinTreeNode(obj));
		} else if (currentData().compareTo((T) obj) < 0) {
			_curr.setRightChild(new BinTreeNode(obj));
		}
		return obj;
	}

	@Override
	public Comparable<T> remove(Comparable<T> obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int maxSearch() {
		// TODO Auto-generated method stub
		return 0;
	}

}
