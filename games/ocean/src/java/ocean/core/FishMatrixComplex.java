package ocean.core;

import ocean.fish.*;

public class FishMatrixComplex extends FishMatrixSimple {

	public FishMatrixComplex(int height, int width) {
		super(height, width);
	}

	/**
	 * Moves a single fish to a new field, if that field is empty or the fish is Feedable and moves onto an Eatable
	 * fish.
	 * 
	 * @param fish
	 *            The fish to set at the new location.
	 * @param from
	 *            The coordinate where the fish should be removed.
	 * @param to
	 *            The coordinate where the fish should be moved to.
	 * 
	 * @return The new coordinate of the fish. This does not necessarily have to be to.
	 */
	protected Coordinate moveFish(Fish fish, Coordinate from, Coordinate to) {
		// TODO

		return null;
	}

	/**
	 * Moves all fish one iteration, the most important method of this class. This also handles Changing fish.
	 */
	public void moveAllFish() {
		// TODO
	}

	/**
	 * Fills the matrix with the number of Herrings and Sharks
	 * 
	 * @param numberHerrings
	 * @param numberSharks
	 */
	public void fillMatrix(int numberHerrings, int numberSharks) {
		// TODO
	}
}
