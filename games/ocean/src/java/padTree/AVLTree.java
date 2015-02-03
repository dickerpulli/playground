package padTree;

import padInterfaces.TraversableSortedContainer;

public class AVLTree<T> extends SearchTree<T> implements
		TraversableSortedContainer<T> {

	// /Declarations

	// /Constructors
	public AVLTree() {
		// ThisConstructorIsLeftBlankIntentionally
	}

	public AVLTree(Comparable<T> NewData) {
		root = traversal = new BinTreeNode(NewData);
	}

	// /PublicMethods
	public BinTreeNode rotateLeft(BinTreeNode v) {
		BinTreeNode parentOfV = v.getParent();
		BinTreeNode temp = v.getRightChild();
		if (temp.getLeftChild() != null) {
			v.setRightChild(temp.getLeftChild());
		} else {
			v.RSon = null;
		}
		temp.setLeftChild(v);
		if (parentOfV == null) {
			root = temp;
		} else {
			if (parentOfV.getRightChild() == v) {
				parentOfV.setRightChild(temp);
			} else {
				parentOfV.setLeftChild(temp);
			}
		}
		return v;
	}

	public BinTreeNode rotateLL(BinTreeNode v) {
		rotateRight(v.RSon);
		rotateLeft(v);
		return v;
	}

	public BinTreeNode rotateRight(BinTreeNode v) {
		BinTreeNode parentOfV = v.getParent();
		BinTreeNode temp = v.getLeftChild();
		if (temp.getRightChild() != null) {
			v.setLeftChild(temp.getRightChild());
		} else {
			v.LSon = null;
		}
		temp.setRightChild(v);
		if (parentOfV == null) {
			root = temp;
		} else {
			if (parentOfV.getRightChild() == v) {
				parentOfV.setRightChild(temp);
			} else {
				parentOfV.setLeftChild(temp);
			}
		}
		return v;
	}

	public BinTreeNode rotateRR(BinTreeNode v) {
		rotateLeft(v.LSon);
		rotateRight(v);
		return v;
	}

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
			if (!(NewNode.getParent() == pointer)) {
				System.out.println("!!!");
			}
			pointer = pointer.getLeftChild();
			rebalanceAfterInsert(pointer);
		}
		if (pointer.getData().compareTo((T) obj) < 0) {
			BinTreeNode NewNode = new BinTreeNode(obj);
			pointer.setRightChild(NewNode);
			if (!(NewNode.getParent() == pointer)) {
				System.out.println("!!!");
			}
			pointer = pointer.getRightChild();
			rebalanceAfterInsert(pointer);
		}
		return pointer.getData();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Comparable<T> remove(Comparable<T> obj) {
		if (obj == null)
			return null;
		BinTreeNode pointer = contains(obj, root);
		if (pointer == traversal)
			reset();
		if (pointer.getData().compareTo((T) obj) != 0)
			return obj;
		BinTreeNode toRotate = delete(pointer);
		// pointer = contains(obj, root);
		if (toRotate != null) {
			rebalanceAfterDelete(toRotate);
		}
		return obj;
	}

	public void rebalanceAfterInsert(BinTreeNode v) {
		BinTreeNode node = v;
		while (node != root) {
			BinTreeNode parent = node.getParent();
			int beta = checkBalances(parent);
			if (beta == 0)
				break;
			if (-1 > beta || beta > 1) {
				rebalance(parent, beta);
				break;
			}
			node = parent;
		}// while
	}

	public void rebalanceAfterDelete(BinTreeNode v) {
		BinTreeNode node = v;
		while (node != null) {
			// BinTreeNode parent = node.getParent();
			int beta = checkBalances(node);
			if (-1 > beta || beta > 1) {
				rebalance(node, beta);
			}
			if (node == root) {
				break;
			}
			node = node.getParent();

			// if ((checkBalances(parent) == 1) || (checkBalances(parent) ==
			// -1)) {
			// break;
			// }
			// node = parent;
		}// while
	}

	public void rebalance(BinTreeNode v, int beta) {
		if (-2 > beta || beta > 2) {
			System.err.println("Error in rebalance: beta ist: " + beta);
			return;
		}
		if (beta == -2) {
			BinTreeNode v1 = v.LSon;
			int beta1 = checkBalances(v1);
			if (-2 > beta1 || beta1 > 2) {
				System.err.println("Error in rebalance: beta' ist: " + beta);
				return;
			}
			if (v == root) {
				root = v.getLeftChild();
				root.Parent = null;
			}
			if (beta1 == 1) {
				rotateRR(v);
			} else // beta1==0 or beta1==-1
			{
				rotateRight(v);
			}
		}

		if (beta == 2) {
			BinTreeNode v1 = v.RSon;
			int beta1 = checkBalances(v1);
			if (-2 > beta1 || beta1 > 2) {
				System.err.println("Error in rebalance: beta' ist: " + beta);
				return;
			}
			if (v == root) {
				root = v.getRightChild();
				root.Parent = null;
			}
			if (beta1 == -1) {
				rotateLL(v);
			} else // beta1==0 or beta1==1
			{
				rotateLeft(v);
			}
		}

	}
}// class AVLTree<T>
