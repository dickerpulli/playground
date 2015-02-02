package padTree;

public abstract class BinTree<T> {

///Public ENUM
	public enum OriginEnum{ABOVE, LEFT, RIGHT}
	
///Class BinTreeNode--GenericNodesForBinaryTreesThatAreLeftOriantated
 
	public class BinTreeNode{
	///Declarations
 		protected 	T Data;
 		protected	BinTreeNode Parent;
 		protected	BinTreeNode LSon;
 		protected	BinTreeNode RSon;
	///Constructors
		public BinTreeNode(){
			Parent=LSon=RSon=null;
		}
		public BinTreeNode(T NewData){
			Data=NewData;
			Parent=LSon=RSon=null;
		}
	///PublicMethods
		public T getData(){
			return Data;
		}
		public boolean isLeaf(){
			return LSon==null && RSon == null;
		}
		public boolean isRoot(){
			return Parent==_dummy;
		}
		public boolean isLeftChild(){
			if(Parent==null) return false;
			return this==Parent.getLeftChild();
		}
		public BinTreeNode getParent(){
			return Parent;
		}
		public BinTreeNode getLeftChild(){
			return LSon;
		}
		public BinTreeNode getRightChild(){
			return RSon;
		}
		public void setLeftChild(BinTreeNode NewLeft){
			NewLeft.Parent=this;
			LSon=NewLeft;
		}
		public void setRightChild(BinTreeNode NewRight){
			NewRight.Parent=this;
			RSon=NewRight;
		}
		public String toString(){
			return Data.toString();
		}
	}//class BinTreeNode

///Declarations
	private BinTreeNode		_dummy;
	protected BinTreeNode	_curr;
	protected OriginEnum	Origin;
	
///Constructors
	public BinTree(){
		_dummy	= new BinTreeNode();
	}
	public BinTree(T NewData){
		_dummy	= new BinTreeNode();
		_curr	= new BinTreeNode(NewData);
		_dummy.setLeftChild(_curr);
	}
	public BinTree(BinTreeNode NewNode){
		_dummy	= new BinTreeNode();
		_curr	= NewNode;
		_dummy.setLeftChild(_curr);
	}
///PublicMethods
	public boolean isEmpty(){
		return _dummy.isLeaf();
	}
	public int getSize(){
		return getSize(_dummy.getLeftChild());
	}
	public int getSize(BinTreeNode Node){		//SizeOfTheSubtree
		return (Node==null)? 0 : 1+getSize(Node.getLeftChild())+getSize(Node.getRightChild()) ;
	}
	public int getHeight(){
		return getHeight(_dummy.getLeftChild());
	}
	public int getHeight(BinTreeNode Node){
		if(Node==null) return 0;
		else{
			int HeightLeft	= getHeight(Node.getLeftChild());
			int HeightRight	= getHeight(Node.getRightChild());
			return (HeightLeft<HeightRight)?1+ HeightLeft : 1+HeightRight ;
		}
	}
	public String toString(){
		return toString(_dummy.getLeftChild());
	}
	public String toString(BinTreeNode Node){
		return(Node==null)?	"*" : "(" + toString(Node.getLeftChild())+")"+ Node.toString() +"("+ toString(Node.getRightChild())+")";
		//					Nothing				LeftPart					Data				RightPart
	}
	public void reset(){			//GoesToThe'Leftest'Child
		_curr=_dummy;
		while(_curr.getLeftChild()!=null){
			_curr=_curr.getLeftChild();
			Origin=OriginEnum.LEFT;
		}
	}
	public void increment(){
		do{
			switch(Origin){
			 case ABOVE:
				if (_curr.getLeftChild() != null) {
					_curr = _curr.getLeftChild();
				}
				else {
					Origin = OriginEnum.LEFT;
				}
				break;//ABOVE
			 case LEFT: 
				if (_curr.getRightChild() != null) {
					_curr = _curr.getRightChild();
					Origin = OriginEnum.ABOVE;
				}
				else {
					Origin = OriginEnum.RIGHT;
				}
				break;//LEFT
			 case RIGHT:
				if (_curr.isLeftChild()) {
					Origin = OriginEnum.LEFT;
				}
				else {
					Origin = OriginEnum.RIGHT;
				}
				_curr = _curr.getParent();
				break; //RIGHT
			default: System.err.println("Error: increment hat keine Richtung gespeichert");
				break;
			}//switch(Origin)
		}while(Origin!=OriginEnum.LEFT && !(Origin==OriginEnum.ABOVE && _curr.getLeftChild()==null));
	}//increment()
	public boolean isAtEnd(){
		return _curr==_dummy;
	}
	public boolean isAtLeaf(){
		return _curr.getLeftChild()==null;
	}
	public T currentData(){
		return _curr.getData();
	}
}//abstract class BinTree<T>
