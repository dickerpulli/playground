package padTree;

public class BinTree<T> {
	protected BinTreeNode<T> _dummy;
	protected BinTreeNode<T> _curr;

	public BinTree() {
		_dummy = new BinTreeNode<T>();
	}

	public BinTree(T single) {
		this();
		BinTreeNode<T> left = new BinTreeNode<T>();
		left.setData(single);
		_dummy.setLeft(left);
	}

	public boolean isEmpty() {
		return _dummy.getLeft() == null;
	}

	public int getHeight() {
		// TODO
		return 0;
	}

	public int getSize() {
		// TODO
		return 0;
	}

	@Override
	public String toString() {
		if (_curr != null) {
			return toString(_curr);
		} else {
			return "<empty>";
		}
	}

	private String toString(BinTreeNode<T> node) {
		String text = "";
		if (node.getLeft() != null) {
			text += toString(node.getLeft());
		}
		text += node.getData().toString() + "\n";
		if (node.getRight() != null) {
			text += toString(node.getRight());
		}
		return text;
	}

	public void reset() {
		_curr = _dummy.getLeft();
	}

	public void increment() {
		// TODO
	}

	public boolean isAtEnd() {
		// TODO
		return false;
	}

	public boolean isAtLeaf() {
		return _curr.getLeft() == null && _curr.getRight() == null;
	}

	public T currentData() {
		return _curr.getData();
	}

	protected class BinTreeNode<T> {
		private BinTreeNode<T> left;
		private BinTreeNode<T> right;
		private T data;

		public BinTreeNode<T> getLeft() {
			return left;
		}

		public void setLeft(BinTreeNode<T> left) {
			this.left = left;
		}

		public BinTreeNode<T> getRight() {
			return right;
		}

		public void setRight(BinTreeNode<T> right) {
			this.right = right;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}

	}

	public static void main(String[] args) {
		BinTree<Integer> tree = new BinTree<Integer>();
		// tree.
	}

}
