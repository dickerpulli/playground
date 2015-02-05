package padTree;

import padInterfaces.TraversableSortedContainer;
import padTree.BinTree.BinTreeNode;

public class BinSearchTree<T> extends BinTree<Comparable<T>> implements
		TraversableSortedContainer<T> {

	private BinTreeNode traversal;
	private BinTreeNode root;

	public BinSearchTree() {
		super();
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
		return root != null && contains(obj, root) != null ? contains(obj, root)
				.getData() : null;
	}

	@SuppressWarnings("unchecked")
	private BinTreeNode contains(Comparable<T> obj, BinTreeNode node) {
		Comparable<T> currentData = node.getData();
		if (currentData.compareTo((T) obj) == 0) {
			// found
			return node;
		} else if (currentData.compareTo((T) obj) > 0) {
			// data is smaller than traveral
			if (node.getLeftChild() != null) {
				// search in left sub-tree
				return contains(obj, node.getLeftChild());
			}
		} else if (currentData.compareTo((T) obj) < 0) {
			// data is bigger than traveral
			if (node.getRightChild() != null) {
				// search in right sub-tree
				return contains(obj, node.getRightChild());
			}
		}
		// not found
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Comparable<T> insert(Comparable<T> obj) {
		if (root == null) {
			root = new BinTreeNode(obj);
			reset();
			_curr.setLeftChild(root);
		}

		// initialize traversal
		traversal = root;

		// while traversal does not reach the end
		while (!isAtLeaf()) {
			Comparable<T> currentData = traversal.getData();
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
		Comparable<T> currentData = traversal.getData();
		if (currentData.compareTo((T) obj) > 0) {
			traversal.setLeftChild(new BinTreeNode(obj));
		} else if (currentData.compareTo((T) obj) < 0) {
			traversal.setRightChild(new BinTreeNode(obj));
		}

		return obj;
	}

	@Override
	public Comparable<T> remove(Comparable<T> obj) {
		BinTreeNode node = contains(obj, root);
		if (node != null) {
			if (node.isLeaf()) {
				// node is leaf
				if (node.isLeftChild()) {
					node.getParent().LSon = null;
				} else {
					node.getParent().RSon = null;
				}
			} else if (node.getLeftChild() != null
					&& node.getRightChild() == null) {
				// node has only one child, left one
				if (node.isLeftChild()) {
					// node to remove is left child, set new left child
					node.getParent().setLeftChild(node.getLeftChild());
				} else {
					node.getParent().setRightChild(node.getLeftChild());
				}
				// set parent of child to parent of node to remove
				node.getLeftChild().Parent = node.getParent();
			} else if (node.getRightChild() != null
					&& node.getLeftChild() == null) {
				// node has only one child, right one
				if (node.isLeftChild()) {
					// node to remove is left child, set new left child
					node.getParent().setLeftChild(node.getRightChild());
				} else {
					node.getParent().setRightChild(node.getRightChild());
				}
				// set parent of child to parent of node to remove
				node.getRightChild().Parent = node.getParent();
			} else {
				// node has two children
				BinTreeNode leftest = leftestChild(node.getRightChild());

				// first of all remove old reference of leftest
				if (leftest.isLeftChild()) {
					leftest.getParent().LSon = null;
				} else {
					leftest.getParent().RSon = null;
				}

				// set leftest node to parent of removing node's parent child
				if (node.isLeftChild()) {
					// node to remove is left child, set new left child
					node.getParent().setLeftChild(leftest);
				} else {
					node.getParent().setRightChild(leftest);
				}

				// inform old children about their new paren
				node.getLeftChild().Parent = leftest;
				node.getRightChild().Parent = leftest;

				// set old children to moved node (leftest)
				leftest.LSon = node.getLeftChild();
				leftest.RSon = node.getRightChild();
			}
			root = _curr.getLeftChild();
			return obj;
		}
		return null;
	}

	private BinTreeNode leftestChild(BinTreeNode node) {
		while (node.getLeftChild() != null) {
			node = node.getLeftChild();
		}
		return node;
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
