package ocean.fish;

import ocean.core.Coordinate;
import ocean.core.Direction;
import ocean.core.FishMatrix;
import ocean.gui.ImageData;

public class OldTuna extends RandomMovingFish {

	public OldTuna() {
		super(ImageData.TUNA_OLD);
	}

	public OldTuna(ImageData imageData) {
		super(imageData);
	}

	@Override
	public String doSomething(FishMatrix matrix) {
		// old fish dies - two young fish where born
		Coordinate coordinate = matrix.whereIsMyFish(this);
		matrix.setFish(coordinate, null);
		matrix.setFish(coordinate, new YoungTuna());
		// the other one is place in a free cell beside - we can reuse the moving method
		Direction direction = getDirection(coordinate, matrix);
		if (direction != Direction.STAY) {
			Coordinate otherCoordinate = matrix.getCoordinate(coordinate, direction);
			matrix.setFish(otherCoordinate, new YoungTuna());
		}
		return "Old Tuna died, young Tunas were born";
	}

	@Override
	public String toString() {
		return "This is an old Tuna";
	}

}
