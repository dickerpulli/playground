package ocean.core;

import java.util.LinkedList;

import ocean.fish.Fish;
import ocean.fish.Herring;
import ocean.fish.Shark;
import ocean.fish.YoungTuna;

public class FishMatrixOptional extends FishMatrixComplex {

	public FishMatrixOptional(int height, int width) {
		super(height, width);
	}

	/**
	 * Moves all fish one iteration, the most important method of this class.
	 * 
	 * In this implementation, all Fish first indicate the direction they want to go. They are then moved into that
	 * direction according to a random permutation (to resolve conflicts).
	 * 
	 * Like FishMatrixComplex, this handles Changing fish.
	 */
	@Override
	public void moveAllFish() {
		super.moveAllFish();
	}

	/**
	 * Fills the matrix with the number of Herrings, Sharks and YoungTuna.
	 * 
	 * @param numberHerrings
	 * @param numberSharks
	 * @param numberTuna
	 */

	public void fillMatrix(int numberHerrings, int numberSharks, int numberTuna) {
		LinkedList<Fish> manyFish = new LinkedList<Fish>();
		for (int h = 0; h < numberHerrings; h++) {
			manyFish.add(new Herring());
		}
		for (int s = 0; s < numberSharks; s++) {
			manyFish.add(new Shark());
		}
		for (int s = 0; s < numberTuna; s++) {
			manyFish.add(new YoungTuna());
		}
		fillMatrix(manyFish);
	}
}
