package ocean.core;

/**
 * A simple class that represents an (x,y)-coordinate.
 *
 * @author dressler
 */
public class Coordinate extends PairPublic<Integer, Integer> {

	/**
	 * Creates a new Coordinate.
	 * @param first  x-Coordinate
	 * @param second y-Coordinate
	 */
	public Coordinate(Integer first, Integer second) {
		super(first, second);
	}
	
	/**
	 * Converts a direction into an offset. For example, DOWN = (0, 1).
	 * @param dir The direction
	 * @return A coordinate representation of the offset.
	 */
	public static Coordinate fromOffset(Direction dir) {
		int x = 0, y = 0;
		
		// vertical
    	switch (dir) {
    	case UP:
    	case UPPER_LEFT:
    	case UPPER_RIGHT: y = -1; break;
    	case DOWN: 
    	case LOWER_LEFT: 
    	case LOWER_RIGHT: y = 1; break;
    	}
    	
    	// horizontal
    	switch (dir) {
    	case LEFT:
    	case UPPER_LEFT:
    	case LOWER_LEFT: x = -1; break;
    	case RIGHT: 
    	case UPPER_RIGHT: 
    	case LOWER_RIGHT: x = 1; break;
    	}
    	
    	return new Coordinate(x,y);
	}
	
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}
