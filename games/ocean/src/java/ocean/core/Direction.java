package ocean.core;

/**
 * Encodes directions to go from one cell in the fish matrix
 * 
 * @author wiese
 *
 */
public enum Direction {
	
	/** direction left */
	LEFT,
	
	/** direction right */
	RIGHT,
	
	/** direction up */
	UP,
	
	/** direction down */
	DOWN,
	
	/** direction upper left */
	UPPER_LEFT,
	
	/** direction upper right */
	UPPER_RIGHT,
	
	/** direction lower left */
	LOWER_LEFT,
	
	/** direction lower right */
	LOWER_RIGHT,
	
	/** don't move at all */
	STAY
}
