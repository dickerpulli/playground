package ocean.core;

import ocean.fish.*;

public class FishMatrixSimple extends FishMatrix {

	public FishMatrixSimple(int height, int width) {
		super(height, width);
	}

	/**
	 * Moves a single fish one step, possibly overwriting any fish the fish moves into.
	 * 
	 * @param fish
	 *            The fish to set at the new location.
	 * @param from
	 *            The coordinate where the fish should be removed.
	 * @param to
	 *            The coordinate where the fish should be moved to.
	 * 
	 * @return The new coordinate of the fish.
	 */
	protected Coordinate moveFish(Fish fish, Coordinate from, Coordinate to) {
		// TODO
		return null;
	}

	/**
	 * Moves all fish one iteration, the most important method of this applet. We iterate all rows from up to down and
	 * all columns from left to right.
	 * <p>
	 *
	 * We maintain an array <code>moved</code> in order to remember if a fish has already been moved. Otherwise some
	 * fish could move more than one step in each phase.
	 * <p>
	 */
	public void moveAllFish() {
		// TODO
	}

	/**
	 * Fills the matrix with the number of SimpleHerings and SimpleSharks.
	 * 
	 * @param numberHerrings
	 * @param numberSharks
	 */
	public void fillMatrix(int numberHerrings, int numberSharks) {
		// TODO
	}
}
