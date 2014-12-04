package ocean.core;

import java.util.Arrays;
import java.util.LinkedList;

import ocean.fish.Fish;
import ocean.fish.SimpleHerring;
import ocean.fish.SimpleShark;

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
	@Override
	protected Coordinate moveFish(Fish fish, Coordinate from, Coordinate to) {
		setFish(from, null);
		setFish(to, fish);
		return to;
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
	@Override
	public void moveAllFish() {
		Fish[] moved = new Fish[0];
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				Coordinate from = new Coordinate(x, y);
				Fish fish = getFish(from);
				if (fish != null && !fishAlreadyMoved(fish, moved)) {
					// calculate next coordinate
					Direction direction = fish.getDirection(from, this);
					Coordinate offset = Coordinate.fromOffset(direction);
					Coordinate to = new Coordinate((from.x + offset.x) % getWidth(), from.y + offset.y);

					// move fish to next coordinate
					moveFish(fish, from, to);

					// save fish as already moved
					moved = Arrays.copyOf(moved, moved.length + 1);
					moved[moved.length - 1] = fish;
				}
			}
		}
	}

	private boolean fishAlreadyMoved(Fish fish, Fish[] allMovedFish) {
		for (Fish movedFish : allMovedFish) {
			if (movedFish.equals(fish)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Fills the matrix with the number of SimpleHerings and SimpleSharks.
	 * 
	 * @param numberHerrings
	 * @param numberSharks
	 */
	public void fillMatrix(int numberHerrings, int numberSharks) {
		LinkedList<Fish> manyFish = new LinkedList<Fish>();
		for (int h = 0; h < numberHerrings; h++) {
			manyFish.add(new SimpleHerring());
		}
		for (int s = 0; s < numberSharks; s++) {
			manyFish.add(new SimpleShark());
		}
		fillMatrix(manyFish);
	}
}
