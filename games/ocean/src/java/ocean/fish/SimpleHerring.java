package ocean.fish;

import ocean.core.Coordinate;
import ocean.core.Direction;
import ocean.core.FishMatrix;
import ocean.gui.ImageData;

public class SimpleHerring extends Fish {

	public SimpleHerring() {
		this(ImageData.HERRING);
	}

	public SimpleHerring(ImageData imageData) {
		super(imageData);
	}

	@Override
	public Direction getDirection(Coordinate from, FishMatrix matrix) {
		// always move right, if possible and no other fish is already there
		Direction move = Direction.RIGHT;
		if (matrix.canMove(from, move) && matrix.getFish(from, move) == null) {
			return move;
		}
		// else stay
		return Direction.STAY;
	}

	@Override
	public String toString() {
		return "This is a SimpleHerring";
	}

}
