package ocean.fish;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import ocean.core.Coordinate;
import ocean.core.Direction;
import ocean.core.FishMatrix;
import ocean.gui.ImageData;

public class Herring extends Fish implements Eatable {

	public Herring() {
		super(ImageData.HERRING);
	}

	public Herring(ImageData imageData) {
		super(imageData);
	}

	@Override
	public Direction getDirection(Coordinate from, FishMatrix matrix) {
		// first of all, put all possible directions in a list - all, but STAY
		List<Direction> directions = new ArrayList<Direction>(Arrays.asList(Direction.values()));
		directions.remove(Direction.STAY);
		Iterator<Direction> it = directions.iterator();
		while (it.hasNext()) {
			// remove all directions that are illegal
			Direction direction = it.next();
			if (!matrix.canMove(from, direction)) {
				it.remove();
				continue;
			}
			// ... or if another fish is already there
			if (matrix.getFish(from, direction) != null) {
				it.remove();
				continue;
			}
		}
		// if there are possible directions left, choose one randomly
		if (!directions.isEmpty()) {
			return directions.get(new Random().nextInt(directions.size()));
		}
		return Direction.STAY;
	}

	@Override
	public String toString() {
		return "This is a Herring";
	}

}
