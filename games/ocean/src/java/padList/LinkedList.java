package padList ;

import java.util.NoSuchElementException ;


/**
 * Class for linked lists. The class especially provides methods for explicitly
 * manipulating ths list's pointer.
 */
public class LinkedList<T>
{
   private class ListNode<T>  
   {
   /***  Daten  ***/

      public T obj ;
      public ListNode<T> next ;

   /***  Konstruktoren  ***/

      public ListNode()
      {
         obj  = null ;
         next = null ;
      }

      public ListNode(T myObj)
      {
         obj  = myObj ;
         next = null ;
      }
   }  // class ListNode


/***  Daten  ***/

   private ListNode<T> _head ;              // Referenz auf ersten Knoten
   private ListNode<T> _curr ;              // Referenz auf akt. Knoten
    
    private int _numElements;               // Anzahl der Elemente in der Liste

 /**
   * Default Constructor. Creates an empty LinkedList.
   *
   *
   */
   public LinkedList( )
   {
      _head = _curr = null ;
      _numElements = 0;
   }


 /**
   * Copy Constructor.
   *
   */
   public LinkedList(LinkedList<T> myList )
   {
      this() ;                                             // ruft LinkedList()

      ListNode<T> tail = null ;

      for ( myList.reset() ;  ! myList.isAtEnd() ;  myList.increment() )
      {
         ListNode<T> newNode = new ListNode<T>( myList.currentData() ) ;
 
	 if ( tail == null )
	    _head = tail      = newNode ;
	 else
	    tail  = tail.next = newNode ;
      }
      _numElements = myList._numElements;
   
   }  // end LinkedList()


 /**
   * Returns if the list is empty.
   *
   * @return true iff the list has no element.
   */

   public boolean isEmpty ( )
   {
      return ( _head == null ) ;
   }


 /**
   * Test if the pointer is at the end of the list.
   *
   * @return true iff the list's pointer is behind the last element of the list or the list is empty.
   */
   public boolean isAtEnd ( )
   {
      return ( _curr == null ) ;
   }


 /**
   * Returns the element the list's pointer points to.
   *
   * @return the element the list's pointer points to
   */
   public T currentData ( )
      throws NoSuchElementException
   {
      if ( isAtEnd() )
	 throw new NoSuchElementException( "No current list node!" ) ;

      return _curr.obj ;
   }

    
/**
   * Resets the list's pointer to the first element of the list if present.
   *
   */
    public void reset ( )
   {
      _curr = _head ;
   }


/**
   * Increments the list's pointer.
   *
   */
    public void increment ( )
   {
      if ( ! isAtEnd() )                         // bleibt sonst am Ende stehen
	 _curr = _curr.next ;
   }


/**
   * Inserts an element at the beginning of the list.
   *
   * @param obj the element to be inserted.
   */
   public void insert ( T obj )
   {
      ListNode<T> newNode = new ListNode<T>( obj ) ;     // constructor for ListNode

      _numElements++;

      newNode.next = _head ;
      _head        = newNode ;
   }

    
/**
   * Removes all elements which equal the specified object (via its equals-method)
   * from the list.
   *
   * @param obj the element to be removed.
   */
   public void remove ( T obj )
   {
      ListNode<T> lastNode = null ;

      for ( reset() ;  ! isAtEnd() ;  increment() )
      {
	 if ( obj.equals( _curr.obj ) )
	 {
	    if ( lastNode == null )
	       _head         = _curr.next ;
	    else
	       lastNode.next = _curr.next ;
	 
	    _numElements--;
	 }

	 lastNode = _curr ;

      }  // end for ( )
   }  // end remove()


    /**
   * Removes the element currently pointed to from the list.
   *
   */
public void removeCurrent ( )
   {
      ListNode<T> delNode  = _curr ;                  // Knoten zum L�schen merken
      ListNode<T> lastNode = null ;

      for ( reset() ;  ! isAtEnd() ;  increment() )
      {
	 if ( _curr == delNode )                       // _curr wieder gefunden
	 {
	    if ( lastNode == null )
	       _head         = _curr.next ;
	    else
	       lastNode.next = _curr.next ;
	    
	    _numElements--;
	    break ;                        // kann nur ein solches Objekt geben
	 }

	 lastNode = _curr ;

      }  // end for ( )
   }  // end remove()



/**
   * Returns the number of elements currently in the list.
   *
   * @return the number of elements currently in the list.
   */
    public int getSize() {
	return _numElements;
    }


/**
   * Returns a string representation of this list.
   *
   * @return a String representing this list.
   */
   public String toString ( )
   {
      StringBuffer strbuf = new StringBuffer() ;

      for ( reset() ;  ! isAtEnd() ;  increment() )
         strbuf.append( currentData().toString() ) ;

      return strbuf.toString() ;

   }  // end toString()



}  // class LinkedList
