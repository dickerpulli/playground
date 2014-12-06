package ocean.fish;

import ocean.core.Coordinate;
import ocean.core.FishMatrix;
import ocean.gui.ImageData;

public class YoungTuna extends RandomMovingFish implements Eatable, Changing {

	private int age = 0;

	public YoungTuna() {
		super(ImageData.TUNA_YOUNG);
	}

	@Override
	public void roundPassed(FishMatrix matrix) {
		age++;
		// at the age of 7 the fish 'gets old'
		if (age == 7) {
			Coordinate coordinate = matrix.whereIsMyFish(this);
			matrix.setFish(coordinate, null);
			matrix.setFish(coordinate, new OldTuna());
		}
	}

	@Override
	public String toString() {
		return "This is a young Tuna";
	}

}
