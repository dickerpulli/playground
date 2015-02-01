package padTree;

import padInterfaces.TraversableSortedContainer;


public class AVLTree<T>
	extends SearchTree<T>
	implements TraversableSortedContainer<T>{

///Declarations
	
	
///Constructors
	public AVLTree(){
		//ThisConstructorIsLeftBlankIntentionally
	}
	
	public AVLTree(Comparable<T> NewData){
		root=traversal=new BinTreeNode(NewData);
	}


///PublicMethods
	public BinTreeNode rotateLeft(BinTreeNode v){
		BinTreeNode temp = v.getRightChild();
		v.RSon=temp.LSon;
		temp.LSon=v;
		v=temp;
		return v;
	}
	
	public BinTreeNode rotateLL(BinTreeNode v){
		v=rotateRight(v);
		v=rotateLeft(v.getParent());
		return v;
	}
	
	public BinTreeNode rotateRight(BinTreeNode v){
		BinTreeNode temp = v.getLeftChild();
		v.LSon=temp.RSon;
		temp.RSon=v;
		v=temp;
		return v;
	}
	
	public BinTreeNode rotateRR(BinTreeNode v){
		v=rotateRight(rotateLeft(v).getParent());
		return v;
	}
	
	@Override
	//@SuppressWarnings("unchecked")
	public Comparable<T> insert(Comparable<T> obj) {
		super.insert(obj);
		return obj;
	}

	
}//class AVLTree<T>
