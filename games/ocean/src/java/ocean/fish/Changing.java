package ocean.fish;

import ocean.core.FishMatrix;
/**
 * An interface describing Fish that can change after every iteration.  
 *
 */
public interface Changing {

	/**
	 * Tells the fish that a round has passed.
	 * @param matrix The current FishMatrix.
	 */
	void roundPassed(FishMatrix matrix);
	
}
