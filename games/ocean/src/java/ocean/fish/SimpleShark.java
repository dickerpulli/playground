package ocean.fish;

import ocean.core.Coordinate;
import ocean.core.Direction;
import ocean.core.FishMatrix;
import ocean.gui.ImageData;

public class SimpleShark extends Fish {

	private Direction lastDirection;
	private Direction nextToLastDirection;

	public SimpleShark() {
		this(ImageData.SHARK);
	}

	public SimpleShark(ImageData imageData) {
		super(imageData);
	}

	@Override
	public Direction getDirection(Coordinate from, FishMatrix matrix) {
		Direction move = Direction.STAY;

		// initial direction is UP
		if (lastDirection == null) {
			lastDirection = Direction.UP;
		}

		// if last direction was stay, then move in oposite direction, if possible
		if (lastDirection == Direction.STAY) {
			if (nextToLastDirection == Direction.UP) {
				Fish fishBelow = matrix.getFish(from, Direction.DOWN);
				boolean anotherSharkIsBelow = fishBelow != null && fishBelow instanceof SimpleShark;
				if (!anotherSharkIsBelow) {
					move = Direction.DOWN;
				} else {
					// we have to simulate the last direction to make sure that nextToLastDirection isn't set to STAY
					lastDirection = Direction.UP;
					move = Direction.STAY;
				}
			} else if (nextToLastDirection == Direction.DOWN) {
				Fish fishAbove = matrix.getFish(from, Direction.UP);
				boolean anotherSharkIsAbove = fishAbove != null && fishAbove instanceof SimpleShark;
				if (!anotherSharkIsAbove) {
					move = Direction.UP;
				} else {
					// we have to simulate the last direction to make sure that nextToLastDirection isn't set to STAY
					lastDirection = Direction.DOWN;
					move = Direction.STAY;
				}
			}
		}

		// last UP: then move up once more or STAY if another shark is there or the surface has been reached
		else if (lastDirection == Direction.UP) {
			if (matrix.canMove(from, Direction.UP)) {
				Fish fishAbove = matrix.getFish(from, Direction.UP);
				boolean anotherSharkIsAbove = fishAbove != null && fishAbove instanceof SimpleShark;
				if (anotherSharkIsAbove) {
					move = Direction.STAY;
				} else {
					move = Direction.UP;
				}
			} else {
				move = Direction.STAY;
			}
		}

		// last DOWN: then move up once more or STAY if another shark is there or the surface has been reached
		else if (lastDirection == Direction.DOWN) {
			if (matrix.canMove(from, Direction.DOWN)) {
				Fish fishAbove = matrix.getFish(from, Direction.DOWN);
				boolean anotherSharkIsAbove = fishAbove != null && fishAbove instanceof SimpleShark;
				if (anotherSharkIsAbove) {
					move = Direction.STAY;
				} else {
					move = Direction.DOWN;
				}
			} else {
				move = Direction.STAY;
			}
		}

		// save the last move
		nextToLastDirection = lastDirection;
		lastDirection = move;
		return move;
	}

	@Override
	public String toString() {
		return "This is a SimpleShark";
	}

}
