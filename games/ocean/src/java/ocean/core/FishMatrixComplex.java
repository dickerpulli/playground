package ocean.core;

import java.util.LinkedList;

import ocean.fish.Changing;
import ocean.fish.Eatable;
import ocean.fish.Feedable;
import ocean.fish.Fish;
import ocean.fish.Herring;
import ocean.fish.Shark;

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
	@Override
	protected Coordinate moveFish(Fish fish, Coordinate from, Coordinate to) {
		Fish fishTo = getFish(to);
		// if there is an eatable fish, eat it
		if (fishTo instanceof Eatable && fish instanceof Feedable) {
			((Feedable) fish).feed((Eatable) fishTo);
		}
		// if there is no fish, or the fish could be eaten
		if (fishTo == null || fishTo instanceof Eatable && fish instanceof Feedable) {
			setFish(from, null);
			setFish(to, fish);
			return to;
		}
		// not moved
		return from;
	}

	/**
	 * Moves all fish one iteration, the most important method of this class. This also handles Changing fish.
	 */
	@Override
	public void moveAllFish() {
		super.moveAllFish();
		// call 'roundPassed' for every fish that implements 'Changing'
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				Coordinate coordinate = new Coordinate(x, y);
				Fish fish = getFish(coordinate);
				if (fish instanceof Changing) {
					((Changing) fish).roundPassed(this);
				}
			}
		}
	}

	/**
	 * Fills the matrix with the number of Herrings and Sharks
	 * 
	 * @param numberHerrings
	 * @param numberSharks
	 */
	@Override
	public void fillMatrix(int numberHerrings, int numberSharks) {
		LinkedList<Fish> manyFish = new LinkedList<Fish>();
		for (int h = 0; h < numberHerrings; h++) {
			manyFish.add(new Herring());
		}
		for (int s = 0; s < numberSharks; s++) {
			manyFish.add(new Shark());
		}
		fillMatrix(manyFish);
	}
}
