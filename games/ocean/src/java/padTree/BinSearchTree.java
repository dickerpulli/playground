package padTree;

import padInterfaces.TraversableSortedContainer;

public class BinSearchTree<T> extends BinTree<Comparable<T>> implements
		TraversableSortedContainer<T> {

	private BinTreeNode traversal;
	private BinTreeNode root;

	public BinSearchTree() {
		super();
		root = traversal = _curr;
	}

	public BinSearchTree(BinTree<Comparable<T>>.BinTreeNode NewNode) {
		super(NewNode);
		root = traversal = _curr;
	}

	public BinSearchTree(Comparable<T> NewData) {
		super(NewData);
		root = traversal = _curr;
	}

	@Override
	public Comparable<T> contains(Comparable<T> obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Comparable<T> insert(Comparable<T> obj) {
		// initialize traversal
		traversal = root;
		
		// while traversal does not reach the end
		Comparable<T> currentData = traversal.getData();
		while (!isAtLeaf()) {
			currentData = traversal.getData();
			if (currentData.compareTo((T) obj) == 0) {
				// data already exists
				return currentData;
			} else if (currentData.compareTo((T) obj) > 0) {
				// data is smaller than traveral
				if (traversal.getLeftChild() != null) {
					// go to next left node
					traversal = traversal.getLeftChild();
					continue;
				} else {
					// right place found to insert
					break;
				}
			} else if (currentData.compareTo((T) obj) < 0) {
				// data is bigger than traveral
				if (traversal.getRightChild() != null) {
					// go to next right node
					traversal = traversal.getRightChild();
					continue;
				} else {
					// right place found to insert
					break;
				}
			}
		}
		
		// end / leaf is reached in iteration
		if (currentData.compareTo((T) obj) > 0) {
			traversal.setLeftChild(new BinTreeNode(obj));
		} else if (currentData.compareTo((T) obj) < 0) {
			traversal.setRightChild(new BinTreeNode(obj));
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
	
	@Override
	public boolean isAtLeaf() {
		return traversal.isLeaf();
	}

}
