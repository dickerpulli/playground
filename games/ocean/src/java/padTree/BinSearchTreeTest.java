package padTree;

import org.junit.Test;

public class BinSearchTreeTest {

	@Test
	public void contains() throws Exception {
		BinSearchTree<Integer> tree = new BinSearchTree<Integer>(12);
		System.out.println(tree);
		tree.insert(7);
		System.out.println(tree);
		tree.insert(71);
		System.out.println(tree);
		tree.insert(1);
		System.out.println(tree);
		tree.insert(9);
		System.out.println(tree);
		tree.insert(19);
		System.out.println(tree);
		tree.insert(84);
		System.out.println(tree);
	}

}
