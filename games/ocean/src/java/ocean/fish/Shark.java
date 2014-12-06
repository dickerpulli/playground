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

public class Shark extends Fish implements Feedable, Changing {

	private int fat = 10;

	private Coordinate lastCoordinate;

	public Shark() {
		this(ImageData.SHARK);
	}

	public Shark(ImageData imageData) {
		super(imageData);
	}

	@Override
	public Direction getDirection(Coordinate from, FishMatrix matrix) {
		// save last coordinate for movement check in 'roundPassed'
		lastCoordinate = from;
		// first of all, put all possible directions in a list
		List<Direction> directions = new ArrayList<Direction>(Arrays.asList(Direction.values()));
		Iterator<Direction> it = directions.iterator();
		while (it.hasNext()) {
			Direction direction = it.next();
			// remove all directions that are illegal
			if (!matrix.canMove(from, direction)) {
				it.remove();
				continue;
			}
			// if there is an eatable fish in the next coordinate, go there
			Fish fish = matrix.getFish(from, direction);
			if (fish != null && fish instanceof Eatable) {
				return direction;
			}
		}
		// if there are possible directions left, choose one randomly, there must exist at least STAY
		return directions.get(new Random().nextInt(directions.size()));
	}

	@Override
	public void roundPassed(FishMatrix matrix) {
		// check if the shark really moved in last round
		Coordinate coordinate = matrix.whereIsMyFish(this);
		if (!coordinate.equals(lastCoordinate)) {
			fat--;
			// if fat is lower 0 the shark dies
			if (fat < 0) {
				matrix.setFish(coordinate, null);
			}
		}
	}

	@Override
	public void feed(Eatable fish) {
		fat = 20;
	}

	@Override
	public String doSomething(FishMatrix matrix) {
		return "The Shark has " + fat + " fat";
	}

	@Override
	public String toString() {
		return "This is a Shark";
	}

}
