import java.util.* ;            
import padInterfaces.* ;       
import padTree.* ;             

/**
 * class to test TraversableSortedContainers
 * see usage for details
 *
 */
public class SearchTreeTest
{
    private static final int AVL = 0;
    private static final int ST = 1;
    
    private static int mode = AVL;
    private static boolean debug = false;
    private static boolean verbose = false;

    private int _numElements;
    private int _maxAccess;
    
    private int    _mconn ;             
    private int    _ncall ;              
    
    private int    _nactive ;            
    private int[]  _active ;             
    private int[]  _remAcs ;             
    private final int _connwin ;         
    
    private Random _rand ;               

    private static final int    MAXCONN = 100000 ;      
    private static final int    SEED    =   1111 ;      
  
    public static void main(String[] argv) {
	if(argv.length<2 || argv.length>3) {
	    printUsage();
	    return;
	}
	boolean optionsGiven = false;
	int numElements, maxAccess;
	// read options
	if(argv[0].startsWith("-")) {
	    if(argv.length < 3) {
		printUsage();
		return;
	    }
	    optionsGiven = true;
	    for(int i=1;i<argv[0].length();i++)
		if(argv[0].charAt(i) == 'a')
		    mode = AVL;
		else if(argv[0].charAt(i) == 'b')
		    mode = ST;
		else if(argv[0].charAt(i) == 'd')
		    debug = true;
		else if(argv[0].charAt(i) == 'v')
		    verbose = true;
	}
	// read number of elements
	try {
	    numElements = Integer.parseInt(argv[(optionsGiven ? 1 : 0)]);
	}
	catch(NumberFormatException nfe) {
	    System.out.println("ERROR: Integer expected for numElements");
	    printUsage();
	    return;
	}
	if(numElements > 100000) {
	    System.out.println("ERROR: Maximum number of elements is 100000");
	    printUsage();
	    return;

	}
	try {
	    maxAccess = Integer.parseInt(argv[(optionsGiven ? 2 : 1)]);
	}
	catch(NumberFormatException nfe) {
	    System.out.println("ERROR: Integer expected for maxAccess");
	    printUsage();
	    return;
	 
	}
	
	testTree(numElements, maxAccess);
    }

    static void printUsage() {
	System.out.println(   "usage: SearchTreeTest [-[a|b][v][d]] <numElements> <access>") ;
	System.out.println(   "       Insert <numElements> elements in search tree, access them <access> times");
	System.out.println(   "       and then remove them (all in randomized order)." ) ;
	    System.out.println(   "       <numElements> : number of elements to insert into tree; must be <= 100000");
	    System.out.println(   "       <access>      : number of requests for a tree element before it is removed" ) ;
	    System.out.println(   "options:");
	    System.out.println(   "       a : use AVLTree (default)"     ) ;
	    System.out.println(   "       b : use SearchTree"  ) ;
	    System.out.println(   "       d : debug mode; after each insert/remove, search tree property is checked");
	    System.out.println(   "           by traversal; if an AVLTree is used, balance property is checked too;");
	    System.out.println(   "           Additionally, maximum size and height of tree are calculated;");
	    System.out.println(   "           CAUTION: may be equally slow with option a and b for large instances");
	    System.out.println(   "       v : verbose mode; search tree is output after each insert/remove" ) ;
    }
    
    static void testTree(int numElements, int maxAccess) {
	
	System.out.println("Using "+(mode==AVL ? "AVL" : "Search")+"Tree...");
	if(debug)
	    System.out.println("Running in DEBUG mode...");
	
	TraversableSortedContainer<Counter> sCont = null ;

	if (mode==AVL) {
	    sCont = new AVLTree<Counter>() ;
	}
	else if (mode==ST) {
	    sCont = new SearchTree<Counter>() ;
	}
	
	SearchTreeTest test = new SearchTreeTest(numElements, maxAccess) ;
	long       nInsert  = 0L ;
	long       nRemove  = 0L ;
	long       nAccess  = 0L ;
	long maxSize = 0L;
	long maxHeight = 0L;
	long       time    = System.currentTimeMillis() ;
	int        conn ;
	
	while ( 0 != ( conn = test.nextElement() ) ) // get next traffic token
	    {
		if ( conn > 0 ) {                // another token for connection conn	
		    Counter tok = new Counter(conn);
		    Counter tok2 = (Counter)sCont.contains(tok); 
		    if(tok2==null) {
			sCont.insert(tok);
			nInsert++;
			if(verbose) {
			    System.out.println("\n* Inserted "+tok);
			    System.out.println("* New tree: ");
			    System.out.print(sCont);
			}
			if(debug) {
			    long size = sCont.getSize();
			    long height = sCont.maxSearch();
			    if(size > maxSize)
				maxSize = size;
			    if(height > maxHeight)
				maxHeight = height;
			    if(verbose)
				System.out.print("** Checking search tree property...");
			    long last = 0;
			    for(sCont.reset();!sCont.isAtEnd();sCont.increment()) {
				long curr = ((Counter)sCont.currentData()).conn;
				if(curr < last) {
				    System.out.println((verbose ? " failed: " : "** Check of search property failed: ")+ curr+" < "+last);
				    System.out.println("** Exiting...");
				    return;
				}
			    }
			    if(verbose)
				System.out.println(" ok");
			    if(mode==AVL) {
				if(verbose)
				    System.out.print("** Checking balance property...");
				if(!((AVLTree)sCont).checkBalances()) {
				    System.out.println((verbose ? " failed." : "** Check of balance property failed."));
				    System.out.println("** Exiting...");
				    return;
				}
				if(verbose)
				    System.out.println(" ok");
			    }
			}
		    }
		    else {
			nAccess++;
			++(tok2.count ) ;
			if(verbose) {
			    System.out.println("\n* Accessed "+tok2+" ("+tok2.count+" times so far)");
			}
		    }
		}
		else {                   // conn < 0 : last token for connection conn
		    Counter tok = new Counter( -conn ) ;
		    tok = (Counter)sCont.remove(tok) ;
		    nRemove++;
		    if(verbose) {
			System.out.println("\n* Removed "+tok );
			System.out.println("* New tree: ");
			System.out.print(sCont);
		    }
		    if(debug) {
			if(verbose)
			    System.out.print("** Checking search tree property...");
			long last = 0;
			for(sCont.reset();!sCont.isAtEnd();sCont.increment()) {
			    long curr = ((Counter)sCont.currentData()).conn;
			    if(curr < last) {
				System.out.println((verbose ? " failed: " : "** Check of search property failed: ")+ curr+" < "+last);
				System.out.println("** Exiting...");
				return;
			    }
			}
			if(verbose)
			    System.out.println(" ok");
			if(mode==AVL) {
			    if(verbose)
				System.out.print("** Checking balance property...");
			    if(!((AVLTree)sCont).checkBalances()) {
				System.out.println((verbose ? " failed." : "** Check of balance property failed."));
				System.out.println("** Exiting...");
				return;
			    }
			    if(verbose)
				System.out.println(" ok");
			}
		    }
		    
		    ++( tok.count ) ;
	   
		}      // else ( conn > 0 )
	    }          // while ( 0 != ( conn = traffic.nextToken() ) ) 
	
	time = System.currentTimeMillis() - time ;
	
	/***  show results  ***/

	System.out.println("\nInserted "+nInsert+" elements, queried tree " +nAccess+ " times, removed "+nRemove+" elements.");
	if(debug) {
	    System.out.println("Maximum tree size: "+maxSize+".");
	    System.out.println("Maximum tree height: "+maxHeight+".");
	}
	System.out.println("Total time: "+ time + " ms.");

    }


    /**
     * initialization constructor
     *
     * @param numElements number of elements to generate, must be >= 1
     * @param maxAccess maximum requests for each element before it is removed >= 0
     */
    public SearchTreeTest(int numElements, int maxAccess) throws IllegalArgumentException
    {
	if ( numElements < 1 )
	    throw new IllegalArgumentException(  "SearchTreeTest( numElements, maxAccess ):"
					       + "numElements must be >= 1 !" ) ;
	if ( maxAccess < 0 )
	    throw new IllegalArgumentException(  "SearchTreeTest( numElements, maxAccess ):"
					       + "maxAccess must be >= 0 !" ) ;

	_numElements   = numElements ;
	_maxAccess  = maxAccess+1 ;
	_connwin = Math.max( MAXCONN - numElements, 0 ) + 1 ;

	_mconn   = 0 ;
	_ncall   = 0 ;

	_nactive = 0 ;
	_active  = new int[ numElements ] ;
	_remAcs   = new int[ MAXCONN + 1 ] ;

	_rand    = new Random( SEED ) ;

    }  // TrafficGen()


    
    public int nextElement ( )
    {
	int conn ;

	if ( _mconn == _numElements )                     
	{
	    if ( _nactive == 0 )                   
		return 0 ;

	    int iact = _rand.nextInt( _nactive ) ;
	    conn     = _active[ iact ] ;

	    if ( --_remAcs[ conn ] == 0 )
	    {                                          
		_active[ iact ] = _active[ --_nactive ] ;
		conn            = -conn ;           
	    }
	}  

	else
	{

	    if ( ++_ncall % 2 == 0 )
		conn = _active[ _rand.nextInt( _nactive ) ] ;
	    else
		conn = _mconn + _rand.nextInt( _connwin ) + 1 ;


	    if ( _remAcs[ conn ] == 0 )                         
            {
		++_mconn ;              

		_remAcs [  conn      ] =  _maxAccess;
		_active[ _nactive++ ] = conn ;               
	    }


	    else if ( --_remAcs[ conn ] == 0 )           
	    {
		for ( int iact = 0 ;  iact < _nactive ;  ++iact )
		    if ( _active[ iact ] == conn )
		    {
			_active[ iact ] = _active[ --_nactive ] ;
			break ;
		    }

		conn = -conn ;                   
	    }
	} 
        return ( conn > 0  ?  (int)(conn - 1) + 3
	           :  (int)(conn + 1) - 3 ) ;
                                                 
    } 


    private static class Counter
	implements Comparable<Counter>
    {
	public int conn ;
	public int count ;
	
	public Counter ( int conn )
	{
	    this.conn  = conn ;
	    this.count = 0 ;
	}
	
	public int compareTo ( Counter obj )
	{
	    if ( conn == obj.conn )
		return 0 ;
	    else
		return ( conn < obj.conn  ?  -1  :  1 ) ;
	}
	
	public boolean equals ( Counter obj )
	{
	    return ( conn == ( (Counter)obj ).conn ) ;
	}
	
	public String toString ( )
	{
	    return conn+"";
	}

    } 
}
