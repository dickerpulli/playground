package padTree;

import org.junit.Test;

public class BinSearchTreeTest {

	@Test
	public void insert() throws Exception {
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

	@Test
	public void contains() throws Exception {
		BinSearchTree<Integer> tree = new BinSearchTree<Integer>(12);
		tree.insert(7);
		tree.insert(71);
		tree.insert(1);
		tree.insert(9);
		tree.insert(19);
		tree.insert(84);
		System.out.println(tree.contains(12));
		System.out.println(tree.contains(1));
		System.out.println(tree.contains(84));
		System.out.println(tree.contains(99));
	}

}
