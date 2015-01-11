package padTree;

import padInterfaces.Valued;

public class HuffmanTree extends BinTree<Valued> implements
		Comparable<HuffmanTree> {

	public enum Direction {
		UP, RIGHT, LEFT;
	}

	public HuffmanTree(Valued object) {
		// TODO
	}

	public HuffmanTree(HuffmanTree firstTree, HuffmanTree secondTree) {
		// TODO
	}

	public boolean append(Direction direction, HuffmanToken token) {
		// TODO
		return false;
	}

	@Override
	public int compareTo(HuffmanTree o) {
		// TODO
		return 0;
	}

	public class HuffmanTreeNode extends BinTreeNode<Valued> {

		public HuffmanTreeNode(Valued data) {
			super(data);
		}

		public HuffmanTreeNode(double data) {
			// TODO
			super(null);
		}

	}

}
